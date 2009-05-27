/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.service.matrix;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.MatrixRowHome;
import org.cipres.treebase.domain.matrix.MatrixRowService;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * MatrixRowServiceImpl.java
 * 
 * Created on Jun 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class MatrixRowServiceImpl extends AbstractServiceImpl implements MatrixRowService {

	private MatrixRowHome mMatrixRowHome;

	/**
	 * Constructor.
	 */
	public MatrixRowServiceImpl() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return getMatrixRowHome();
	}

	/**
	 * Return the MatrixRowHome field.
	 * 
	 * @return MatrixRowHome mMatrixRowHome
	 */
	private MatrixRowHome getMatrixRowHome() {
		return mMatrixRowHome;
	}

	/**
	 * Set the MatrixRowHome field.
	 */
	public void setMatrixRowHome(MatrixRowHome pNewMatrixRowHome) {
		mMatrixRowHome = pNewMatrixRowHome;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixRowService#findByID(java.lang.Long)
	 */
	public MatrixRow findByID(Long pMatrixRowID) {
		if (pMatrixRowID == null) {
			return null;
		}
		return getMatrixRowHome().findPersistedObjectByID(MatrixRow.class, pMatrixRowID);
	}

	@Override
	public Class defaultResultClass() {
		return MatrixRow.class;
	}

}
