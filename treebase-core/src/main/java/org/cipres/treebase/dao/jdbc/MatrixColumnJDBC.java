/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ 
 * All Rights Reserved. 
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for educational, research and non-profit purposes, without
 * fee, and without a written agreement is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies. 
 *
 * Permission to incorporate this software into commercial products may be
 * obtained by contacting us:
 *              http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event
 * shall the CIPRES project be liable to any party for direct, indirect,
 * special, incidental, or consequential damages, including lost profits, 
 * arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage.  
 * The CIPRES project specifically disclaims any warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for
 * a particular purpose. The CIPRES project has no obligations to provide 
 * maintenance, support, updates, enhancements, or modifications. 
 */

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
