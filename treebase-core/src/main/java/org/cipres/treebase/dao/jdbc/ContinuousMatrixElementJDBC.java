
package org.cipres.treebase.dao.jdbc;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import org.springframework.jdbc.UncategorizedSQLException;

/**
 * MatrixElementJDBC.java
 * 
 * Created on Jan 16, 2008
 * 
 * @author Jin Ruan
 * 
 */
public class ContinuousMatrixElementJDBC {
	private static final Logger LOGGER = Logger.getLogger(ContinuousMatrixElementJDBC.class);

	private short mGap = 0;
	private double mValue;

	private long mMatrixColID;
	private long mMatrixRowID;
	private int mElementOrder;

	// MATRIXELEMENT_ID, VERSION,
	// TYPE, COMPOUNDVALUE, ANDLOGIC,

	// MATRIXCOLUMN_ID, MATRIXROW_ID, ELEMENT_ORDER

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
	public ContinuousMatrixElementJDBC() {
		super();
	}

	/**
	 * 
	 * Creation date: Mar 20, 2008 10:24:56 AM
	 */
	public static void batchContinuousElements(
		Collection<ContinuousMatrixElementJDBC> pElements,
		Connection pCon) {

		StringBuffer queryBuf = new StringBuffer(
			"INSERT INTO MATRIXELEMENT(TYPE, MATRIXELEMENT_ID, VERSION, ");
		queryBuf.append("VALUE, GAP, MATRIXCOLUMN_ID, MATRIXROW_ID, ELEMENT_ORDER) ").append(
			"VALUES('N', default, 1, ?, ?, ?, ?, ?)");

		try {
			PreparedStatement ps = pCon.prepareStatement(queryBuf.toString());

			int count = 0;
			for (ContinuousMatrixElementJDBC e : pElements) {

				// TYPE: N
				// MatrixElement_ID: default
				// Version: 1

				// // Not set here:
				// CompoundValue: ''
				// ANDLogic: 0
				// DISCRETECHARSTATE_ID

				ps.setDouble(1, e.getValue()); // Value
				ps.setShort(2, e.getGap()); // Gap
				ps.setLong(3, e.getMatrixColID()); // MATRIXCOLUMN_ID
				// ps.setLong(4, 0); // ITEMDEFINITION_ID //Only by compound elements. 
				ps.setLong(4, e.getMatrixRowID()); // MATRIXROW_ID
				ps.setInt(5, e.getElementOrder()); // ELEMENT_ORDER

				ps.addBatch();
				count++;
				
				//IBM JDBC driver has a 32k batch limit:
				if (count > MatrixJDBC.JDBC_BATCH_LIMIT) {
					count = 0;
					ps.executeBatch();
					pCon.commit();

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("committing count=" + count); //$NON-NLS-1$
					}
				}
			}

			ps.executeBatch();
			pCon.commit();
			ps.close();
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("committing count=" + count); //$NON-NLS-1$
			}

		} catch (SQLException ex) {
			throw new UncategorizedSQLException(
				"Failed to batch continuous matrix elements.",
				"",
				ex);
		}
	}

	/**
	 * Return the Gap field.
	 * 
	 * @return short mGap
	 */
	public short getGap() {
		return mGap;
	}

	/**
	 * Set the Gap field.
	 */
	public void setGap(short pNewGap) {
		mGap = pNewGap;
	}

	/**
	 * Return the Value field.
	 * 
	 * @return double mValue
	 */
	public double getValue() {
		return mValue;
	}

	/**
	 * Set the Value field.
	 */
	public void setValue(double pNewValue) {
		mValue = pNewValue;
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
