
package org.cipres.treebase.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mesquite.lib.characters.CharacterData;

import org.apache.log4j.Logger;
import org.springframework.jdbc.UncategorizedSQLException;

import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.ContinuousMatrix;
import org.cipres.treebase.domain.matrix.DiscreteChar;
import org.cipres.treebase.domain.matrix.DiscreteCharState;
import org.cipres.treebase.domain.nexus.mesquite.MesquiteMatrixConverter;
import org.cipres.treebase.domain.nexus.nexml.NexmlMatrixConverter;

/**
 * Helper class for direct Matrix related SQL operations. Bypass the hibernate framework for high
 * performance database operations.
 * 
 * Created on Jul 13, 2007
 * 
 * @author Jin Ruan
 * 
 */
public class DiscreteMatrixJDBC extends MatrixJDBC {

	private static final Logger LOGGER = Logger.getLogger(DiscreteMatrixJDBC.class);

	//UPDATE MATRIXROW 
    //SET MATRIXROW_ID=0, VERSION=0, MATRIX_ID=0, TAXONLABEL_ID=0, ROW_ORDER=0, SYMBOLSTRING=''
    //WHERE 
    
    // INSERT INTO PHYLOCHAR(TYPE, PHYLOCHAR_ID, VERSION, DESCRIPTION, LOWERLIMIT, UPPERLIMIT)
	// VALUES('D', default, 0, ?, 0, 0)

	// INSERT INTO DISCRETECHARSTATE(DISCRETECHARSTATE_ID, VERSION, DESCRIPTION, NOTES,
	// PHYLOCHAR_ID, STATESET_ID, ANCESTRALSTATE_ID)
	// VALUES(default, 0, ?, ?, ?, ?, ?)

	// insert into MATRIXELEMENT
	// (MATRIXELEMENT_ID, VERSION, MATRIXCOLUMN_ID, DISCRETECHARSTATE_ID, TYPE)
	// values
	// (default, ?, ?, ?, 'D')

	// INSERT INTO MATRIXELEMENT(TYPE, MATRIXELEMENT_ID, VERSION, COMPOUNDVALUE, ANDLOGIC, VALUE,
	// GAP, MATRIXCOLUMN_ID, DISCRETECHARSTATE_ID, ITEMDEFINITION_ID, MATRIXROW_ID, ELEMENT_ORDER)
	// VALUES('D', default, 0, '', 0, 0, 0, 0, 0, 0, 0, 0)

	// INSERT INTO MATRIXELEMENT(TYPE, MATRIXELEMENT_ID, VERSION,
	// COMPOUNDVALUE, ANDLOGIC, VALUE, GAP, MATRIXCOLUMN_ID,
	// ITEMDEFINITION_ID, MATRIXROW_ID, DISCRETECHARSTATE_ID, ELEMENT_ORDER)
	// VALUES(?, default, 1,
	// '', 0, 0, 0, 0,
	// 0, 0, 0, 0)

	// private CharacterMatrix mCharacterMatrix;
	// private CharacterData mMesquiteCharacterData;
	private Long mDefaultCharID;

	private Map<String, Long>[] mDiscreteStateMapsByCol;
	private Map<Long, Map> mPhyloCharIdToStateMapMap;
	private List<StringBuffer> mRowSymbolBufs;

	/**
	 * Constructor.
	 */
	private DiscreteMatrixJDBC() {
		super();
	}

	/**
	 * Constructor.
	 * @param pM
	 * @param pCategoricalData
	 * @param pMesquiteStandardMatrixConverter
	 */
	public DiscreteMatrixJDBC(
		CharacterMatrix pMatrix,
		CharacterData pMesqCategoricalData,
		MesquiteMatrixConverter pMesqMatrixConverter) {
		
		this();
		
		setCharacterMatrix(pMatrix);
		setMesquiteCharacterData(pMesqCategoricalData);
		setMesqMatrixConverter(pMesqMatrixConverter);
		
		// add string buffers for rows.		
		int rowCount = pMesqCategoricalData.getNumTaxa();
		mRowSymbolBufs = new ArrayList<StringBuffer>(rowCount);
		for (int i = 0; i < rowCount; i++) {
			mRowSymbolBufs.add(new StringBuffer());
		}
	}
	
	public DiscreteMatrixJDBC(CharacterMatrix tbMatrix,
			org.nexml.model.CategoricalMatrix xmlMatrix,
			NexmlMatrixConverter nexmlMatrixConverter) {
		this();
		setCharacterMatrix(tbMatrix);
		setNexmlCharacterData(xmlMatrix);
		setNexmlMatrixConverter(nexmlMatrixConverter);
	}	

	/**
	 * Return the RowSymbolBufs field.
	 * 
	 * @return List<StringBuffer> mRowSymbolBufs
	 */
	public List<StringBuffer> getRowSymbolBufs() {
		return mRowSymbolBufs;
	}

	/**
	 * An array, index corresponding to column index. Element is a map for the corresponding
	 * discrete char states(key: state symbol, value: state id)
	 * 
	 * @return Map<String, Long>[] 
	 */
	private Map<String, Long>[] getDiscreteStateMapsByCol() {
		return mDiscreteStateMapsByCol;
	}

	/**
	 * Get all discrete states for a column. Return a map for the corresponding discrete char
	 * states(key: state symbol, value: state id)
	 * 
	 * @param pColIndex
	 * @return
	 */
	public Map<String, Long> getStateSymbolToIdMap(int pColIndex) {
		return getDiscreteStateMapsByCol()[pColIndex];
	}

	/**
	 * Map key: PhyloCharID Map value: a map of DiscreteStateMap (key: state symbol, value: state id)
	 * 
	 * @return Map<Long, Map> 
	 */
	public Map<Long, Map> getPhyloCharIdToStateMapMap() {
		return mPhyloCharIdToStateMapMap;
	}

	/**
	 * Return the DefaultCharID field.
	 * 
	 * @return Long mDefaultCharID
	 */
	public Long getDefaultCharID() {
		return mDefaultCharID;
	}

	/**
	 * Set the DefaultCharID field.
	 */
	public void setDefaultCharID(Long pNewDefaultCharID) {
		mDefaultCharID = pNewDefaultCharID;
	}

	/**
	 * Return the discriminator value for the sub classes in MatrixElement class hierarchy.
	 * 
	 * @return
	 */
	@Override
	protected String getElementDiscriminator() {
		return TYPE_DISCRETE;
	}

	/**
	 * Batch update symbols field in each row.
	 * 
	 * @param pCon
	 */
	public void batchUpdateRowSymbol(Connection pCon) {

		int numRows = getRowIDs().length;

		try {
			
			//update symbols for each row:
			String rowSymbolStr = "UPDATE MATRIXROW SET SYMBOLSTRING= ? WHERE MATRIXROW_ID= ?";
			PreparedStatement rowSymbolBatch = pCon.prepareStatement(rowSymbolStr);
			int count = 0;
			for (int i = 0; i < numRows; i++) {
				
				rowSymbolBatch.setString(1, getRowSymbolBufs().get(i).toString());
				rowSymbolBatch.setLong(2, getRowIDs()[i]);
				
				rowSymbolBatch.addBatch();
				count++;
				
				// IBM JDBC driver has a 32k batch limit:
				if (count > MatrixJDBC.JDBC_BATCH_LIMIT) {
					count = 0;
					rowSymbolBatch.executeBatch();
					pCon.commit();

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("  commit count=" + count); //$NON-NLS-1$
					}
				}
			}

			rowSymbolBatch.executeBatch();
			pCon.commit();
			rowSymbolBatch.close();

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("rowSymbolbatch committing count=" + count); //$NON-NLS-1$
			}
			

		} catch (SQLException ex) {
			throw new UncategorizedSQLException(
				"Failed to batch row symbols.",
				"",
				ex);
		}

	}

	/**
	 * Prepare for batch insert elements: <br>
	 * 
	 * Update symbols field in each row.
	 * 
	 * populate the following:
	 * <p>* mRowIDs: row ids, index corresponding to row index
	 * <p>* mColIDs: column ids
	 * <p>* mDiscreteStateMapsByCol: an array, index corresponding to column index element is a map
	 * for the corresponding discrete char states(key: state, value: state id)
	 * 
	 * @param pCon
	 */
	@Override
	public void prepElementBatchInsert(Connection pCon) {

		super.prepElementBatchInsert(pCon);

		CharacterData mesqData = getMesquiteCharacterData();
		int numCols = mesqData.getNumChars();
		int numRows = getRowIDs().length;
		// int numRows = mesqData.getNumTaxa();

		try {
			
			// populate the col ids:
			mDiscreteStateMapsByCol = (Map<String, Long>[]) new HashMap[numCols];

			// the index is the column index, the value is the corresponding phylochar id.
			Long[] phyloCharIDs = getPhyloCharIDs();

			// populate the column array (element is a map of discrete symbol - stateID)

			List<Long> phyloCharList = Arrays.asList(phyloCharIDs);
			Set<Long> phyloCharIDSet = new HashSet(phyloCharList);
			mPhyloCharIdToStateMapMap = new HashMap<Long, Map>();

			StringBuffer queryBuf = new StringBuffer(
				"select DISCRETECHARSTATE_ID, symbol from discretecharstate where PHYLOCHAR_ID = ?");
			PreparedStatement ps = pCon.prepareStatement(queryBuf.toString());

			for (Long charId : phyloCharIDSet) {

				if (charId != null) {
					Map<String, Long> stateSymbolToIdMap = new HashMap<String, Long>();

					ps.setLong(1, charId); // phyloChar id
					ResultSet rs = ps.executeQuery();
					while (rs != null && rs.next()) {

						long stateId = rs.getLong(1); // discreteCharState id
						String symbol = rs.getString(2); // symbol

						stateSymbolToIdMap.put(symbol, stateId);
					}

					rs.close();

					mPhyloCharIdToStateMapMap.put(charId, stateSymbolToIdMap);
				}
			}
			ps.close();

			// populate mDiscreteStateMapsByCol:
			for (int i = 0; i < numCols; i++) {
				Long charId = phyloCharIDs[i];
				Map<String, Long> stateMap = getPhyloCharIdToStateMapMap().get(charId);

				mDiscreteStateMapsByCol[i] = stateMap;
			}

		} catch (SQLException ex) {
			throw new UncategorizedSQLException(
				"Failed to prepare for batch matrix elements.",
				"",
				ex);
		}

	}

	/**
	 * Batch insert column. Also need to insert new phylochar and discreteCharStates.
	 * 
	 * @param pCon
	 */
	@Override
	public void batchInsertColumn(Connection pCon) {

		if (getDefaultCharID() != null) {
			// chase efficiency at the cost of code duplication:
			batchInsertColumnDefaultChar(pCon);
			return;
		}
		// CharacterData mesqData = getMesquiteCharacterData();
		// int numCols = mesqData.getNumChars();
		// int numRows = mesqData.getNumTaxa();
		long matrixID = getCharacterMatrix().getId();
		List<MatrixColumnJDBC> columns = getMatrixColumnJDBCs();

		try {
			// insert new discrete phylochar.
			//Note: this is db2 specific code (faster), see EnvrionmentTest.testGetGeneratedKey() for generic jdbc code.
			//VG-- String queryBuf = "select phylochar_id from final table(INSERT INTO PHYLOCHAR(TYPE, PHYLOCHAR_ID, VERSION, DESCRIPTION) VALUES('D', default, 0, ?))";
			//VG 2010-02-10 Replacing above DB2 sql with this PostreSQL:
			String queryBuf = "INSERT INTO PHYLOCHAR(TYPE, PHYLOCHAR_ID, VERSION, DESCRIPTION) VALUES('D', default, 0, ?) RETURNING phylochar_id";
			
			PreparedStatement ps = pCon.prepareStatement(queryBuf);

			// String stateStr = "INSERT INTO DISCRETECHARSTATE(DISCRETECHARSTATE_ID, VERSION,
			// DESCRIPTION, NOTES, PHYLOCHAR_ID, STATESET_ID, ANCESTRALSTATE_ID) VALUES(default, 0,
			// ?, ?, ?, ?, ?)";
			// Not consider at this time: stateSet id and ANCESTRALSTATE_ID
			String stateStr = "INSERT INTO DISCRETECHARSTATE(DISCRETECHARSTATE_ID, VERSION, DESCRIPTION, NOTES, PHYLOCHAR_ID, SYMBOL) VALUES(default, 0, ?, ?, ?, ?)";
			PreparedStatement stateBatch = pCon.prepareStatement(stateStr);

			for (MatrixColumnJDBC columnJDBC : columns) {

				stateBatch.clearBatch();
				DiscreteChar phyloChar = (DiscreteChar) columnJDBC.getPhyloChar();

				// Need to test: symbolChar is shared for the matrix.
				if (phyloChar.getId() == null) {
					ps.setString(1, phyloChar.getDescription()); // phylochar description

					// Not apply to discrete phylo character:
					// ps.setDouble(2, phyloChar.getLowerLimit());
					// ps.setDouble(3, phyloChar.getUpperLimit());

					ResultSet rs = ps.executeQuery();
					if (rs != null && rs.next()) {

						long phyloCharId = rs.getLong(1); // phylochar_id
//						if (LOGGER.isDebugEnabled()) {
//							LOGGER.debug("phylocharId =" + phyloCharId);
//						}

						columnJDBC.setPhyloCharID(phyloCharId);

						// insert new states:
						for (DiscreteCharState aState : phyloChar.getCharStates()) {

							// DISCRETECHARSTATE_ID: default
							// Version: 0

							// Not set here:
							// stateSet_id: null
							// ANCESTRALSTATE_ID

							stateBatch.setString(1, aState.getDescription()); // state description
							stateBatch.setString(2, aState.getNotes()); // state notes
							stateBatch.setLong(3, phyloCharId); // phyloChar id
							stateBatch.setString(4, aState.getSymbol().toString()); //symbol
							
							stateBatch.addBatch();

							// IBM JDBC driver has a 32k batch limit:
							// Don't think a char will have 32k states!!
							// if (count > MatrixJDBC.JDBC_BATCH_LIMIT) {
							// count = 0;
							// psBatch.executeBatch();
							// pCon.commit();
							//
							// if (LOGGER.isDebugEnabled()) {
							// LOGGER.debug("committing count=" + count); //$NON-NLS-1$
							// }
							// }
						}

						stateBatch.executeBatch();
						pCon.commit();

//						if (LOGGER.isDebugEnabled()) {
//							LOGGER.debug("committing state batch ="); //$NON-NLS-1$
//						}

					}
					rs.close();

				} else {
					columnJDBC.setPhyloCharID(phyloChar.getId());
				}

			}
			pCon.commit();
			stateBatch.close();
			ps.close();

			// batch insert columns
			// String insertStr = "INSERT INTO MATRIXCOLUMN(MATRIXCOLUMN_ID, VERSION, PHYLOCHAR_ID,
			// MATRIX_ID, STATEFORMAT_ID, COLUMN_ORDER) VALUES(default, 0, ?, ?, ?, ?)";
			// For now, no stateFormat id:
			String insertStr = "INSERT INTO MATRIXCOLUMN(MATRIXCOLUMN_ID, VERSION, PHYLOCHAR_ID, MATRIX_ID, COLUMN_ORDER) VALUES(default, 0, ?, ?, ?)";
			PreparedStatement psBatch = pCon.prepareStatement(insertStr);

			int count = 0;
			for (int i = 0; i < columns.size(); i++) {
				MatrixColumnJDBC columnJDBC = columns.get(i);

				// MatrixColumn_ID: default
				// Version: 0

				// // Not set here:
				// stateFormat_id: null

				psBatch.setLong(1, columnJDBC.getPhyloCharID()); // phyloChar Id
				psBatch.setLong(2, matrixID); // matrix id
				psBatch.setInt(3, i); // column order

				psBatch.addBatch();
				count++;
				
				// IBM JDBC driver has a 32k batch limit:
				if (count > MatrixJDBC.JDBC_BATCH_LIMIT) {
					count = 0;
					psBatch.executeBatch();
					pCon.commit();

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" commit count=" + count); //$NON-NLS-1$
					}
				}
			}

			psBatch.executeBatch();
			pCon.commit();
			psBatch.close();

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" columnBatch committing count=" + count); //$NON-NLS-1$
			}

		} catch (SQLException ex) {
			throw new UncategorizedSQLException(
				"Failed to batch insert columns.",
				"",
				ex);
		}

	}

	/**
	 * Batch insert columns with matrix id and preset char id.
	 * 
	 * @param pCon
	 */
	private void batchInsertColumnDefaultChar(Connection pCon) {
		// CharacterData mesqData = getMesquiteCharacterData();
		// int numCols = mesqData.getNumChars();
		long matrixID = getCharacterMatrix().getId();
		long charId = getDefaultCharID();
		List<MatrixColumnJDBC> columns = getMatrixColumnJDBCs();

		try {
			// batch insert columns
			// String insertStr = "INSERT INTO MATRIXCOLUMN(MATRIXCOLUMN_ID, VERSION, PHYLOCHAR_ID,
			// MATRIX_ID, STATEFORMAT_ID, COLUMN_ORDER) VALUES(default, 0, ?, ?, ?, ?)";
			// For now, no stateFormat id:
			String insertStr = "INSERT INTO MATRIXCOLUMN(MATRIXCOLUMN_ID, VERSION, PHYLOCHAR_ID, MATRIX_ID, COLUMN_ORDER) VALUES(default, 0, ?, ?, ?)";
			PreparedStatement psBatch = pCon.prepareStatement(insertStr);

			int count = 0;
			for (int i = 0; i < columns.size(); i++) {
				// MatrixColumnJDBC columnJDBC = columns.get(i);

				// MatrixColumn_ID: default
				// Version: 0

				// // Not set here:
				// stateFormat_id: null

				psBatch.setLong(1, charId); // phyloChar Id
				psBatch.setLong(2, matrixID); // matrix id
				psBatch.setInt(3, i); // column order

				psBatch.addBatch();
				count++;
				
				// IBM JDBC driver has a 32k batch limit:
				if (count > MatrixJDBC.JDBC_BATCH_LIMIT) {
					count = 0;
					psBatch.executeBatch();
					pCon.commit();

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" commit count=" + count); //$NON-NLS-1$
					}
				}
			}

			psBatch.executeBatch();
			pCon.commit();
			psBatch.close();

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" columnDefaultCharBatch committing count=" + count); //$NON-NLS-1$
			}

		} catch (SQLException ex) {
			throw new UncategorizedSQLException(
				"Failed to batch insert columns with preset character.",
				"",
				ex);
		}

	}

	/**
	 * 
	 * @see org.cipres.treebase.dao.jdbc.MatrixJDBC#batchInsertCompoundElements(java.sql.Connection)
	 */
	@Override
	public void batchInsertCompoundElements(Connection pConn) {

		// FIXME: batchInsertCompoundElements
		getCompoundElements();

	}

}
