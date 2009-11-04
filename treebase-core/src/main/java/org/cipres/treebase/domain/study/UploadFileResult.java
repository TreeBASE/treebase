
package org.cipres.treebase.domain.study;

/**
 * Result for upload nexus files. 
 * 
 * Created on Jul 9, 2008
 * @author Jin Ruan
 *
 */
public class UploadFileResult {

	private int mMatrixCount;
	private int mTreeCount;

	/**
	 * Constructor.
	 */
	public UploadFileResult() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param pMatrixCount
	 * @param pTreeCount
	 * 
	 */
	public UploadFileResult(int pMatrixCount, int pTreeCount) {
		super();
		
		setMatrixCount(pMatrixCount);
		setTreeCount(pTreeCount);
	}

	/**
	 * Return the TreeCount field.
	 * 
	 * @return int mTreeCount
	 */
	public int getTreeCount() {
		return mTreeCount;
	}

	/**
	 * Set the TreeCount field.
	 */
	public void setTreeCount(int pNewTreeCount) {
		mTreeCount = pNewTreeCount;
	}
	
	/**
	 * Return the MatrixCount field.
	 * 
	 * @return int mMatrixCount
	 */
	public int getMatrixCount() {
		return mMatrixCount;
	}

	/**
	 * Set the MatrixCount field.
	 */
	public void setMatrixCount(int pNewMatrixCount) {
		mMatrixCount = pNewMatrixCount;
	}
	
}
