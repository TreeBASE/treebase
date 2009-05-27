/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ 
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

package org.cipres.treebase.web.model;

import org.cipres.treebase.domain.matrix.MatrixRow;

/**
 * MatrixRowCommand.java
 * 
 * Created on Jun 25, 2006
 * @author lcchan
 *
 */
public class MatrixRowCommand {

	private MatrixRow mMatrixRow;
	private String mColumnData;

	/**
	 * Constructor.
	 */
	public MatrixRowCommand() {
	}
	public MatrixRowCommand(MatrixRow matrixRow) {
		this.mMatrixRow = matrixRow;
	}
	public MatrixRowCommand(MatrixRow matrixRow, String data) {
		this.mMatrixRow = matrixRow;
		this.mColumnData = data;
	}
	/**
	 * Return the ColumnData field.
	 * 
	 * @return String mColumnData
	 */
	public String getColumnData() {
		return mColumnData;
	}

	/**
	 * Set the ColumnData field.
	 */
	public void setColumnData(String pNewColumnData) {
		mColumnData = pNewColumnData;
	}
	/**
	 * Return the MatrixRow field.
	 * 
	 * @return MatrixRow mMatrixRow
	 */
	public MatrixRow getMatrixRow() {
		return mMatrixRow;
	}

	/**
	 * Set the MatrixRow field.
	 */
	public void setMatrixRow(MatrixRow pNewMatrixRow) {
		mMatrixRow = pNewMatrixRow;
	}
}
