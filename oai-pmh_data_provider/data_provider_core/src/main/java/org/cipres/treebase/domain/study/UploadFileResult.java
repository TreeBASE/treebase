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