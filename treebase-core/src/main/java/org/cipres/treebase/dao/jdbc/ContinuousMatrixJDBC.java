/*
 * Copyright 2007 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

package org.cipres.treebase.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import mesquite.cont.lib.ContinuousData;
import mesquite.lib.characters.CharacterData;

import org.apache.log4j.Logger;
import org.springframework.jdbc.UncategorizedSQLException;

import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.ContinuousChar;
import org.cipres.treebase.domain.matrix.ContinuousMatrix;
import org.cipres.treebase.domain.matrix.ItemDefinition;
import org.cipres.treebase.domain.nexus.mesquite.MesquiteMatrixConverter;

/**
 * Helper class for direct Matrix related SQL operations. Bypass the hibernate framework for high
 * performance database operations.
 * 
 * Created on Jul 13, 2007
 * 
 * @author Jin Ruan
 * 
 */
public class ContinuousMatrixJDBC extends MatrixJDBC {

	private static final Logger LOGGER = Logger.getLogger(ContinuousMatrixJDBC.class);

	// INSERT INTO ITEMDEFINITION(ITEMDEFINITION_ID, VERSION, DESCRIPTION)
	// VALUES(default, 0, ?)

	// INSERT INTO MATRIXCOLUMN_ITEMDEFINITION(MATRIXCOLUMN_ID, ITEMDEFINITION_ID)
	// VALUES(?, ?)

	// INSERT INTO PHYLOCHAR(TYPE, PHYLOCHAR_ID, VERSION, DESCRIPTION, LOWERLIMIT, UPPERLIMIT)
	// VALUES('N', default, 0, ?, ?, ?)

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

	private Map<String, Long> mItemDefNameToIDMap;

	// Note: currently we support only one set of item definitions for the entire matrix.
	// It is modeled in continuousMatrix.itemDefinitions.
	// The matrixColumn.itemDefinitions are not supported at this time since Mesquite (and Nexus)
	// does not
	// support it.
	// private List<ItemDefinitionJDBC> mItemDefJDBCs;

	/**
	 * Constructor.
	 */
	private ContinuousMatrixJDBC() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param pMatrix
	 * @param pMesquiteData
	 * @param pConverter
	 */
	public ContinuousMatrixJDBC(
		CharacterMatrix pMatrix,
		CharacterData pMesquiteData,
		MesquiteMatrixConverter pConverter) {

		this();

		setCharacterMatrix(pMatrix);
		setMesquiteCharacterData(pMesquiteData);
		setMesqMatrixConverter(pConverter);

	}

	// /**
	// * Return the ItemDefJDBCs field.
	// *
	// * @return List<ItemDefinitionJDBC> mItemDefJDBCs
	// */
	// public List<ItemDefinitionJDBC> getItemDefJDBCs() {
	// return mItemDefJDBCs;
	// }
	//
	// /**
	// * Set the ItemDefJDBCs field.
	// */
	// public void setItemDefJDBCs(List<ItemDefinitionJDBC> pNewItemDefJDBCs) {
	// mItemDefJDBCs = pNewItemDefJDBCs;
	// }
	//	
	/**
	 * (key: Item definition name, value: id)
	 * 
	 * @return Map<String, Long>
	 */
	public Map<String, Long> getItemDefNameToIDMap() {
		return mItemDefNameToIDMap;
	}

	/**
	 * Return the discriminator value for the sub classes in MatrixElement class hierarchy.
	 * 
	 * @return
	 */
	@Override
	protected String getElementDiscriminator() {
		return TYPE_CONINUOUS;
	}

	/**
	 * Batch insert column. Also need to insert new phylochar and discreteCharStates.
	 * 
	 * @param pCon
	 */
	@Override
	public void batchInsertColumn(Connection pCon) {

		// CharacterData mesqData = getMesquiteCharacterData();
		// int numCols = mesqData.getNumChars();
		// int numRows = mesqData.getNumTaxa();
		long matrixID = getCharacterMatrix().getId();
		List<MatrixColumnJDBC> columns = getMatrixColumnJDBCs();

		try {
			// insert new phylochar.
			// Note: this is db2 specific code (faster), see EnvrionmentTest.testGetGeneratedKey()
			// for generic jdbc code.
			String queryBuf = "select phylochar_id from final table(INSERT INTO PHYLOCHAR(TYPE, PHYLOCHAR_ID, VERSION, DESCRIPTION, LOWERLIMIT, UPPERLIMIT) VALUES('N', default, 0, ?, ?, ?))";

			PreparedStatement ps = pCon.prepareStatement(queryBuf);
			for (MatrixColumnJDBC columnJDBC : columns) {

				ContinuousChar phyloChar = (ContinuousChar) columnJDBC.getPhyloChar();
				ps.setString(1, phyloChar.getDescription()); // phylochar description

				if (phyloChar.getLowerLimit() != null) {
					ps.setDouble(2, phyloChar.getLowerLimit());
				} else {
					ps.setNull(2, Types.DOUBLE);
				}

				if (phyloChar.getUpperLimit() != null) {
					ps.setDouble(3, phyloChar.getUpperLimit());
				} else {
					ps.setNull(3, Types.DOUBLE);
				}

				ResultSet rs = ps.executeQuery();
				if (rs != null && rs.next()) {

					long phyloCharId = rs.getLong(1); // phylochar_id
					// if (LOGGER.isDebugEnabled()) {
					// LOGGER.debug("phylocharId =" + phyloCharId);
					// }

					columnJDBC.setPhyloCharID(phyloCharId);
				}

				rs.close();
			}
			pCon.commit();
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
						LOGGER.debug("committing count=" + count); //$NON-NLS-1$
					}
				}
			}

			psBatch.executeBatch();
			pCon.commit();
			psBatch.close();

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("committing count=" + count); //$NON-NLS-1$
			}

		} catch (SQLException ex) {
			throw new UncategorizedSQLException(
				"Failed to prepare for batch matrix elements.",
				"",
				ex);
		}

	}

	/**
	 * Insert item definition and matrixColumn_itemDefinition.
	 * 
	 * @param pCon
	 */
	public void insertItemDefinition(Connection pCon) {
	// Note: currently we support only one set of item definitions for the entire matrix.
	// It is modeled in continuousMatrix.itemDefinitions.
	// The matrixColumn.itemDefinitions are not supported at this time since Mesquite (and Nexus)
	// does not
	// support it.

	}

	/**
	 * Prepare for batch insert elements: <br>
	 * populate the following: <br>* mRowIDs: row ids, index corresponding to row index <br>*
	 * mColIDs: column ids <br>* mDiscreteStateMapsByCol: an array, index corresponding to column
	 * index element is a map for the corresponding discrete char states(key: state, value: state
	 * id)
	 * 
	 * @param pCon
	 */
	@Override
	public void prepElementBatchInsert(Connection pCon) {

		super.prepElementBatchInsert(pCon);

		ContinuousData continuousData = (ContinuousData) getMesquiteCharacterData();
		// int numCols = continuousData.getNumChars();
		// int numItems = continuousData.getNumItems();

		// Set<String> itemDefNameSet = new HashSet<String>();
		//
		// for (int j = 0; j < numItems; j++) {
		// String itemName = continuousData.getItemName(j);
		// itemDefNameSet.add(itemName);
		// }

		// populate itemDefNameToIDMap based on continuousMatrix.itemDefinitions
		ContinuousMatrix matrix = (ContinuousMatrix) getCharacterMatrix();

		for (ItemDefinition anItemDef : matrix.getItemDefinitions()) {

			mItemDefNameToIDMap.put(anItemDef.getDescription(), anItemDef.getId());
		}

		// The following code is used to populate itemDefNameToIDMap based on
		// matrixColumn.itemDefinitions.
		// It is not needed right now since we don't support column based item definition at this
		// time.

		// long matrixID = getCharacterMatrix().getId(); // TODO: need to refresh matrix first??
		//
		// try {
		//
		// // populate the itemname - itemdef id map:
		// mItemDefNameToIDMap = new HashMap<String, Long>();
		//
		// StringBuffer queryBuf = new StringBuffer(
		// "select i.ITEMDEFINITION_ID, i.DESCRIPTION from ItemDefinition i, ");
		// queryBuf
		// .append("matrixcolumn_itemdefinition mi, matrixcolumn c where ")
		// .append(
		// "c.MATRIXCOLUMN_ID = mi.MATRIXCOLUMN_ID and mi.ITEMDEFINITION_ID = i.ITEMDEFINITION_ID
		// and c.MATRIX_ID = ?");
		//
		// PreparedStatement ps = pCon.prepareStatement(queryBuf.toString());
		//
		// ps.setLong(1, matrixID); // matrix id
		// ResultSet rs = ps.executeQuery();
		// while (rs != null && rs.next()) {
		//
		// long itemDefId = rs.getLong(1); // item def id
		// String desc = rs.getString(2); // item def description
		//
		// mItemDefNameToIDMap.put(desc, itemDefId);
		// }
		//
		// rs.close();
		// ps.close();
		//
		// } catch (SQLException ex) {
		// throw new UncategorizedSQLException(
		// "Failed to prepare for batch continuous matrix elements.",
		// "",
		// ex);
		// }

	}

	/**
	 * 
	 * @see org.cipres.treebase.dao.jdbc.MatrixJDBC#batchInsertCompoundElements(java.sql.Connection)
	 */
	@Override
	public void batchInsertCompoundElements(Connection pConn) {
		// FIXME: batchInsertCompoundElements
		List<CompoundElementJDBC> compoundElements = getCompoundElements();

	}

}
