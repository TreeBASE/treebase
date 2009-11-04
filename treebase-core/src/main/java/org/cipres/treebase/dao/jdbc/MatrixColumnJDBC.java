
package org.cipres.treebase.dao.jdbc;

import org.cipres.treebase.domain.matrix.PhyloChar;

/**
 * MatrixColumnJDBC.java
 * Helper class for directly JDBC operations. Represents a matrix column. 
 * 
 * Created on May 20, 2008
 * @author Jin Ruan
 *
 */
public class MatrixColumnJDBC {

	//INSERT INTO MATRIXCOLUMN(MATRIXCOLUMN_ID, VERSION, PHYLOCHAR_ID, MATRIX_ID, STATEFORMAT_ID, COLUMN_ORDER) 
    //VALUES(default, 0, ?, ?, ?, ?)

	private long mPhyloCharID;
	private PhyloChar mPhyloChar;

	/**
	 * Constructor.
	 */
	public MatrixColumnJDBC() {
		super();
	}

	/**
	 * Return the PhyloChar field.
	 * 
	 * @return PhyloChar mPhyloChar
	 */
	public PhyloChar getPhyloChar() {
		return mPhyloChar;
	}

	/**
	 * Set the PhyloChar field.
	 */
	public void setPhyloChar(PhyloChar pNewPhyloChar) {
		mPhyloChar = pNewPhyloChar;
	}
	
	/**
	 * Return the PhyloCharID field.
	 * 
	 * @return long mPhyloCharID
	 */
	public long getPhyloCharID() {
		return mPhyloCharID;
	}

	/**
	 * Set the PhyloCharID field.
	 */
	public void setPhyloCharID(long pNewPhyloCharID) {
		mPhyloCharID = pNewPhyloCharID;
		if (getPhyloChar() != null) {
			getPhyloChar().setId(pNewPhyloCharID);
		}
	}
	
}
