
package org.cipres.treebase.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mesquite.lib.characters.CharacterData;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.nexml.model.Matrix;
import org.springframework.jdbc.UncategorizedSQLException;

import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.ContinuousMatrix;
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
public abstract class MatrixJDBC {
	//Matrix element subtypes:
	public static final String TYPE_COMPOUND = "C";
	public static final String TYPE_CONINUOUS = "N";
	public static final String TYPE_DISCRETE = "D";

	//IBM JDBC driver has a 32k batch limit:
	public static final int JDBC_BATCH_LIMIT = 30000;
	
	private static final Logger LOGGER = Logger.getLogger(MatrixJDBC.class);

	//INSERT INTO MATRIXCOLUMN(MATRIXCOLUMN_ID, VERSION, PHYLOCHAR_ID, MATRIX_ID, STATEFORMAT_ID, COLUMN_ORDER) 
    //VALUES(default, 0, ?, ?, ?, ?)

	//INSERT INTO PHYLOCHAR(TYPE, PHYLOCHAR_ID, VERSION, DESCRIPTION, LOWERLIMIT, UPPERLIMIT) 
    //VALUES(?, default, 0, ?, null, null)
    
    // INSERT INTO MATRIXELEMENT(TYPE, MATRIXELEMENT_ID, VERSION, COMPOUNDVALUE, ANDLOGIC, VALUE,
	// GAP, MATRIXCOLUMN_ID, DISCRETECHARSTATE_ID, ITEMDEFINITION_ID, MATRIXROW_ID, ELEMENT_ORDER)
	// VALUES('D', default, 0, '', 0, 0, 0, 0, 0, 0, 0, 0)

	// INSERT INTO MATRIXELEMENT(TYPE, MATRIXELEMENT_ID, VERSION,
	// COMPOUNDVALUE, ANDLOGIC, VALUE, GAP, MATRIXCOLUMN_ID,
	// ITEMDEFINITION_ID, MATRIXROW_ID, DISCRETECHARSTATE_ID, ELEMENT_ORDER)
	// VALUES(?, default, 1,
	// '', 0, 0, 0, 0,
	// 0, 0, 0, 0)

	private CharacterMatrix mCharacterMatrix;
	private CharacterData mMesquiteCharacterData;

	private List<CompoundElementJDBC> mCompoundElements = new ArrayList<CompoundElementJDBC>();

	private long[] mRowIDs;
	private long[] mColIDs;
	private Long[] mPhyloCharIDs; //one phyloChar id for each column. 
	private List<MatrixColumnJDBC> mMatrixColumnJDBCs;

	private MesquiteMatrixConverter mMesqMatrixConverter;
	private NexmlMatrixConverter mNexmlMatrixConverter;
	private Matrix mNexmlCharacterData;

	/**
	 * Factory methods for creating a new MatrixJDBC instance based on the CharacterMatrix type.
	 * 
	 * @param pMatrix
	 * @param pMesquiteData
	 * @return
	 */
	public static MatrixJDBC factory(CharacterMatrix pMatrix, CharacterData pMesquiteData,
		MesquiteMatrixConverter pConverter) {
		
		if (pMatrix == null) {
			return null;
		}
		
		MatrixJDBC m = null;
		if (pMatrix instanceof ContinuousMatrix) {
			m = new ContinuousMatrixJDBC(pMatrix, pMesquiteData, pConverter);
		} else {
			m = new DiscreteMatrixJDBC(pMatrix, pMesquiteData, pConverter);
		}
		
//		m.setCharacterMatrix(pMatrix);
//		m.setMesquiteCharacterData(pMesquiteData);
//		m.setMesqMatrixConverter(pConverter);
		
		return m;
	}
	
	/**
	 * Delete all columns and associated objects using direct SQL. 
	 * 
	 * @param pMatrix
	 * @param pSession
	 */
	public static void deleteMatrixColumnSQL(CharacterMatrix pMatrix, Session pSession) {		
		// delete all matrix columns by direct JDBC:
		// * delete all matrixColumn_itemDefinitions
		// * delete all columns
		
		String query = "DELETE FROM MATRIXCOLUMN_ITEMDEFINITION WHERE MATRIXCOLUMN_ID in (select col.MATRIXCOLUMN_ID from MATRIXCOLUMN col where col.MATRIX_ID = :matrixID)"; 
		Query q = pSession.createSQLQuery(query);
		q.setParameter("matrixID", pMatrix.getId());
		q.executeUpdate();

		query = "DELETE FROM MATRIXCOLUMN WHERE MATRIX_ID = :matrixID";
		q = pSession.createSQLQuery(query);
		q.setParameter("matrixID", pMatrix.getId());
		q.executeUpdate();
			
	}

	/**
	 * Delete all rows and associated objects using direct SQL. 
	 * 
	 * @param pMatrix
	 * @param pSession
	 */
	public static void deleteMatrixRowSQL(CharacterMatrix pMatrix, Session pSession) {		
		// delete all matrix rows by direct JDBC:
		// * delete all row segments
		// * delete all rows
		
		String query = "DELETE FROM ROWSEGMENT WHERE MATRIXROW_ID in (select row.MATRIXROW_ID from MATRIXROW row where row.MATRIX_ID = :matrixID)"; 
		Query q = pSession.createSQLQuery(query);
		q.setParameter("matrixID", pMatrix.getId());
		q.executeUpdate();

		query = "DELETE FROM MATRIXROW WHERE MATRIX_ID = :matrixID";
		q = pSession.createSQLQuery(query);
		q.setParameter("matrixID", pMatrix.getId());
		q.executeUpdate();
			
	}
	/**
	 * Delete all elements and associated objects in a matrix using direct SQL.
	 * 
	 * @param pTree
	 */
	public static void deleteMatrixElementSQL(CharacterMatrix pMatrix, Session pSession) {
		// delete all matrix elements by direct JDBC:
		// * delete all state modifiers
		// * delete all item values
		// * delete all compound_element
		// * delete all elements

		String query = "delete from STATEMODIFIER where STATEMODIFIER_id IN "
			+ "(select sf.STATEMODIFIER_ID from STATEMODIFIER sf, matrixelement m, MATRIXCOLUMN c where sf.ELEMENT_ID = m.MATRIXELEMENT_ID and m.MATRIXCOLUMN_ID = c.MATRIXCOLUMN_ID and c.MATRIX_ID = :matrixID)";
		Query q = pSession.createSQLQuery(query);
		q.setParameter("matrixID", pMatrix.getId());
		q.executeUpdate();

		query = "delete from ITEMVALUE where ITEMVALUE_id IN "
			+ "(select i.ITEMVALUE_id from ITEMVALUE i, matrixelement m, MATRIXCOLUMN c where i.ELEMENT_ID = m.MATRIXELEMENT_ID and m.MATRIXCOLUMN_ID = c.MATRIXCOLUMN_ID and c.MATRIX_ID = :matrixID)";
		q = pSession.createSQLQuery(query);
		q.setParameter("matrixID", pMatrix.getId());
		q.executeUpdate();

		query = "delete from Compound_element where element_id IN "
			+ "(select ce.element_id from Compound_element ce, matrixelement m, MATRIXCOLUMN c where ce.ELEMENT_ID = m.MATRIXELEMENT_ID and m.MATRIXCOLUMN_ID = c.MATRIXCOLUMN_ID and c.MATRIX_ID = :matrixID)";
		q = pSession.createSQLQuery(query);
		q.setParameter("matrixID", pMatrix.getId());
		q.executeUpdate();

		query = "DELETE FROM matrixelement WHERE matrixcolumn_id IN"
			+ "(SELECT c.matrixcolumn_id FROM matrixcolumn c WHERE c.matrix_id = :matrixID)";
		q = pSession.createSQLQuery(query);
		q.setParameter("matrixID", pMatrix.getId());
		q.executeUpdate();
	}

	/**
	 * Constructor.
	 */
	public MatrixJDBC() {
		super();
	}

	/**
	 * Return the MesqMatrixConverter field.
	 * 
	 * @return MesquiteMatrixConverter mMesqMatrixConverter
	 */
	private MesquiteMatrixConverter getMesqMatrixConverter() {
		return mMesqMatrixConverter;
	}

	/**
	 * Set the MesqMatrixConverter field.
	 */
	public void setMesqMatrixConverter(MesquiteMatrixConverter pNewMesqMatrixConverter) {
		mMesqMatrixConverter = pNewMesqMatrixConverter;
	}
	
	/**
	 * Return the MatrixColumnJDBCs field.
	 * 
	 * @return List<MatrixColumnJDBC> mMatrixColumnJDBCs
	 */
	public List<MatrixColumnJDBC> getMatrixColumnJDBCs() {
		return mMatrixColumnJDBCs;
	}

	/**
	 * Set the MatrixColumnJDBCs field.
	 */
	public void setMatrixColumnJDBCs(List<MatrixColumnJDBC> pNewMatrixColumnJDBCs) {
		mMatrixColumnJDBCs = pNewMatrixColumnJDBCs;
	}
	
	/**
	 * Return the ColIDs field.
	 * 
	 * @return long[] mColIDs
	 */
	public long[] getColIDs() {
		return mColIDs;
	}

	/**
	 * Return the RowIDs field.
	 * 
	 * @return long[] mRowIDs
	 */
	public long[] getRowIDs() {
		return mRowIDs;
	}

	/**
	 * Return the PhyloCharIDs field.
	 * 
	 * @return 
	 */
	protected Long[] getPhyloCharIDs() {
		return mPhyloCharIDs;
	}

	/**
	 * Set the PhyloCharIDs field.
	 */
	protected void setPhyloCharIDs(Long[] pNewPhyloCharIDs) {
		mPhyloCharIDs = pNewPhyloCharIDs;
	}
	
	/**
	 * @return the compoundElements
	 */
	public List<CompoundElementJDBC> getCompoundElements() {
		return mCompoundElements;
	}

	/**
	 * Return the CharacterMatrix field.
	 * 
	 * @return CharacterMatrix mCharacterMatrix
	 */
	public CharacterMatrix getCharacterMatrix() {
		return mCharacterMatrix;
	}

	/**
	 * Set the CharacterMatrix field.
	 */
	public void setCharacterMatrix(CharacterMatrix pNewCharacterMatrix) {
		mCharacterMatrix = pNewCharacterMatrix;
	}

	/**
	 * Return the MesquiteCharacterData field.
	 * 
	 * @return CharacterData mMesquiteCharacterData
	 */
	public CharacterData getMesquiteCharacterData() {
		return mMesquiteCharacterData;
	}

	/**
	 * Set the MesquiteCharacterData field.
	 */
	public void setMesquiteCharacterData(CharacterData pNewMesquiteCharacterData) {
		mMesquiteCharacterData = pNewMesquiteCharacterData;
	}

	protected void setNexmlMatrixConverter(
			NexmlMatrixConverter nexmlMatrixConverter) {
		mNexmlMatrixConverter = nexmlMatrixConverter;				
	}

	protected void setNexmlCharacterData(
			org.nexml.model.Matrix xmlMatrix) {
		mNexmlCharacterData = xmlMatrix;		
	}	
	
	/**
	 * Return the discriminator value for the sub classes in MatrixElement class hierarchy.
	 * 
	 * @return
	 */
	protected abstract String getElementDiscriminator();

	/**
	 * Prepare for batch insert elements: <br>
	 * populate the following: <br>* mRowIDs: row ids, index corresponding to row index <br>*
	 * mColIDs: column ids <br>* mDiscreteStateMapsByCol: an array, index corresponding to column
	 * index element is a map for the corresponding discrete char states(key: state, value: state
	 * id)
	 * 
	 * @param pCon
	 */
	public void prepElementBatchInsert(Connection pCon) {

		CharacterData mesqData = getMesquiteCharacterData();
		int numCols = mesqData.getNumChars();
		int numRows = mesqData.getNumTaxa();
		long matrixID = getCharacterMatrix().getId(); 

		try {
			// populate the row ids:
			mRowIDs = new long[numRows];

			StringBuffer queryBuf = new StringBuffer(
				"select matrixrow_id, row_order from matrixrow where matrix_id = ?");
			PreparedStatement ps = pCon.prepareStatement(queryBuf.toString());
			ps.setLong(1, matrixID); // matrix id

			ResultSet rs = ps.executeQuery();
			while (rs != null && rs.next()) {

				long rowId = rs.getLong(1); // matrixrow
				int rowOrder = rs.getInt(2); // row order

				mRowIDs[rowOrder] = rowId;

				//LOGGER.debug("rowId ="+rowId + " rowOrder=" + rowOrder);

			}

			rs.close();
			ps.close();

			if (LOGGER.isDebugEnabled()) {
//				LOGGER
//					.debug("prepElementBatchInsert() : rowIDs size=" + mRowIDs.length + " :=" + TreebaseUtil.printElement(mRowIDs, false)); //$NON-NLS-1$
				LOGGER
				.debug("matrixid :=" + matrixID + " numRows=" +numRows + " numCols=" + numCols ); //$NON-NLS-1$
			}

			// populate the col ids:
			mColIDs = new long[numCols];

			// the index is the column index, the value is the corresponding phylochar id.
			mPhyloCharIDs = new Long[numCols];

			queryBuf = new StringBuffer(
				"select MATRIXCOLUMN_ID, COLUMN_ORDER, PHYLOCHAR_ID from matrixcolumn where MATRIX_ID = ?");
			ps = pCon.prepareStatement(queryBuf.toString());
			ps.setLong(1, matrixID); // matrix id

			rs = ps.executeQuery();
			while (rs != null && rs.next()) {

				long colId = rs.getLong(1); // matrix col
				int colOrder = rs.getInt(2); // col order
				long phyloCharId = rs.getLong(3); // phylochar id

				mColIDs[colOrder] = colId;
				mPhyloCharIDs[colOrder] = phyloCharId;
				
				//LOGGER.debug("colid ="+colId + " colOrder=" + colOrder + " phyloCharId=" + phyloCharId);
			}

			rs.close();
			ps.close();

//			if (LOGGER.isDebugEnabled()) {
//				LOGGER
//					.debug("colIDs size=" + mColIDs.length + " :=" + TreebaseUtil.printElement(mColIDs, false)); //$NON-NLS-1$
//				LOGGER
//					.debug("phyloCharIDs size=" + mPhyloCharIDs.length + " :=" + TreebaseUtil.printElement(mPhyloCharIDs, false)); //$NON-NLS-1$
//			}
			
		} catch (SQLException ex) {
			throw new UncategorizedSQLException(
				"Failed to prepare for batch matrix elements.",
				"",
				ex);
		}

	}

	/**
	 * Use JDBC direct batch inserts to first insert all elements, then one by one insert the
	 * compound elements.
	 * 
	 * @param pElements
	 * @param pMatrixJDBC
	 * @param pCon
	 */
	public void batchInsert(List<DiscreteMatrixElementJDBC> pElements, Connection pCon) {

//		if (pElements == null || pCon == null) {
//			return;
//		}
//
//		try {
//			if (TYPE_CONINUOUS.equals(getElementDiscriminator())) {
//				ContinuousMatrixElementJDBC.batchContinuousElements(pElements, pCon);
//			}else {
//				DiscreteMatrixElementJDBC.batchDiscreteElements(pElements, pCon);
//			}
//			
//			// FIXME: add compound elements:
//
//		} catch (SQLException ex) {
//			throw new UncategorizedSQLException("Failed to add matrix elements.", "", ex);
//		}
	}

	/**
	 * 
	 * @param pConn
	 */
	public abstract void batchInsertCompoundElements(Connection pConn);

	/**
	 * Batch insert column.
	 * 
	 * @param pCon
	 */
	public abstract void batchInsertColumn(Connection pCon);
	
	/**
	 * 
	 * @param pConn
	 */
	public void processMatrixElements(Connection pConn) {
		getMesqMatrixConverter().processMatrixElements(this, pConn);
	}

}
