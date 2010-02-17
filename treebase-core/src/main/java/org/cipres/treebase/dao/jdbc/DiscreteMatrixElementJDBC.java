
package org.cipres.treebase.dao.jdbc;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.UncategorizedSQLException;

/**
 * MatrixElementJDBC.java
 * 
 * Created on Jan 16, 2008
 * 
 * @author Jin Ruan
 * 
 */
public class DiscreteMatrixElementJDBC {
	private static final Logger LOGGER = Logger.getLogger(DiscreteMatrixElementJDBC.class);

	// public static final char TYPE_COMPOUND = 'C';
	// public static final char TYPE_CONINUOUS = 'N';
	// public static final char TYPE_DISCRETE = 'D';

	private short mGap = 0;
	private long mDiscreteCharState_ID;

	private long mMatrixColID;
	private long mMatrixRowID;
	private int mElementOrder;

	// MATRIXELEMENT_ID, VERSION,
	// MATRIXCOLUMN_ID, MATRIXROW_ID, ELEMENT_ORDER

	// TYPE, COMPOUNDVALUE, ANDLOGIC,
	// for compound element

	// DISCRETECHARSTATE_ID, GAP
	// for discrete state

	// VALUE, ITEMDEFINITION_ID,
	// Value is double, for continuous element

	// ******************************************************/
	// INSERT INTO MATRIXELEMENT(TYPE, MATRIXELEMENT_ID, VERSION,
	// COMPOUNDVALUE, ANDLOGIC, VALUE, GAP, MATRIXCOLUMN_ID,
	// ITEMDEFINITION_ID, MATRIXROW_ID, DISCRETECHARSTATE_ID, ELEMENT_ORDER)
	// VALUES(?, default, 1,
	// '', 0, 0, 0, 0,
	// 0, 0, 0, 0)

	/**
	 * Constructor.
	 */
	public DiscreteMatrixElementJDBC() {
		super();
	}

	/**
	 * 
	 * Creation date: Mar 20, 2008 10:24:56 AM
	 */
	public static void batchDiscreteElements(
		List<DiscreteMatrixElementJDBC> pElements,
		Connection pCon) {

		StringBuffer queryBuf = new StringBuffer(
			"INSERT INTO MATRIXELEMENT(TYPE, MATRIXELEMENT_ID, VERSION, ");
		queryBuf
			.append("GAP, MATRIXCOLUMN_ID, MATRIXROW_ID, DISCRETECHARSTATE_ID, ELEMENT_ORDER) ")
			.append("VALUES('D', default, 1, ?, ?, ?, ?, ?)");

		try {
			PreparedStatement ps = pCon.prepareStatement(queryBuf.toString());

			int elementSize = pElements.size();
			int count = 0;
			for (int i = 0; i < elementSize; i++) {
				
				DiscreteMatrixElementJDBC e = pElements.get(i);
				
				// TYPE: D
				// MatrixElement_ID: default
				// Version: 1

				// // not set here:
				// CompoundValue: '' (for compound)
				// ANDLogic: 0 (for compound)
				// Value: (continuous)
				// ITEMDEFINITION_ID: (continuous)

				ps.setBoolean(1, e.getGap()); // Gap
				ps.setLong(2, e.getMatrixColID()); // MATRIXCOLUMN_ID
				ps.setLong(3, e.getMatrixRowID()); // MATRIXROW_ID

				if (e.getDiscreteCharState_ID() > 0) {
					ps.setLong(4, e.getDiscreteCharState_ID()); // DISCRETECHARSTATE_ID
				} else {
					ps.setNull(4, Types.BIGINT);
				}

				ps.setInt(5, e.getElementOrder()); // ELEMENT_ORDER

				ps.addBatch();
				count++;
				
				//IBM JDBC driver has a 32k batch limit:
				if (count > MatrixJDBC.JDBC_BATCH_LIMIT) {
					ps.executeBatch();
					pCon.commit();

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("committing count=" + count); //$NON-NLS-1$
					}
					count = 0;
				}
			}

			ps.executeBatch();
			pCon.commit();
			ps.close();
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("committing count=" + count); //$NON-NLS-1$
			}
			
		} catch (SQLException ex) {
			throw new UncategorizedSQLException("Failed to batch discrete matrix elements.", "", ex);
		}
	}

	/**
	 * Return the Gap field.
	 * 
	 * @return short mGap
	 */
	public boolean getGap() {
		return (mGap!=0);
	}

	/**
	 * Set the Gap field.
	 */
	public void setGap(short pNewGap) {
		mGap = pNewGap;
	}

	/**
	 * Return the DiscreteCharState_ID field.
	 * 
	 * @return long mDiscreteCharState_ID
	 */
	public long getDiscreteCharState_ID() {
		return mDiscreteCharState_ID;
	}

	/**
	 * Set the DiscreteCharState_ID field.
	 */
	public void setDiscreteCharState_ID(long pNewDiscreteCharState_ID) {
		mDiscreteCharState_ID = pNewDiscreteCharState_ID;
	}

	/**
	 * Return the MatrixRowID field.
	 * 
	 * @return long mMatrixRowID
	 */
	public long getMatrixRowID() {
		return mMatrixRowID;
	}

	/**
	 * Set the MatrixRowID field.
	 */
	public void setMatrixRowID(long pNewMatrixRowID) {
		mMatrixRowID = pNewMatrixRowID;
	}

	/**
	 * Return the MatrixColID field.
	 * 
	 * @return long mMatrixColID
	 */
	public long getMatrixColID() {
		return mMatrixColID;
	}

	/**
	 * Set the MatrixColID field.
	 */
	public void setMatrixColID(long pNewMatrixColID) {
		mMatrixColID = pNewMatrixColID;
	}

	/**
	 * Return the ElementOrder field.
	 * 
	 * @return int mElementOrder
	 */
	public int getElementOrder() {
		return mElementOrder;
	}

	/**
	 * Set the ElementOrder field.
	 */
	public void setElementOrder(int pNewElementOrder) {
		mElementOrder = pNewElementOrder;
	}

}
