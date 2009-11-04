
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
