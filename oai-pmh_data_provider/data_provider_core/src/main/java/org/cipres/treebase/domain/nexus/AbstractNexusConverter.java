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

package org.cipres.treebase.domain.nexus;

import org.cipres.treebase.domain.matrix.MatrixDataTypeHome;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;

/**
 * AbstractNexusConverter.java
 * 
 * Created on Aug 28, 2006
 * @author Jin Ruan
 *
 */
public abstract class AbstractNexusConverter {

	protected static final String NEWICK_DELIMITERS = "(),: ";

	private TaxonLabelHome mTaxonLabelHome;
	private MatrixDataTypeHome mMatrixDataTypeHome;

	private Study mStudy;
	
	/**
	 * Constructor.
	 */
	public AbstractNexusConverter() {
		super();
	}

	/**
	 * Return the Study field.
	 * 
	 * @return Study mStudy
	 */
	protected Study getStudy() {
		return mStudy;
	}

	/**
	 * Set the Study field.
	 */
	protected void setStudy(Study pNewStudy) {
		mStudy = pNewStudy;
	}

	/**
	 * Return the TaxonLabelHome field.
	 * 
	 * @return TaxonLabelHome mTaxonLabelHome
	 */
	protected TaxonLabelHome getTaxonLabelHome() {
		return mTaxonLabelHome;
	}

	/**
	 * 
	 * @param pTaxonLabelHome
	 */
	public void setTaxonLabelHome(TaxonLabelHome pTaxonLabelHome) {
		mTaxonLabelHome = pTaxonLabelHome;
	}

	/**
	 * Return the MatrixDataTypeHome field.
	 * 
	 * @return MatrixDataTypeHome mMatrixDataTypeHome
	 */
	protected MatrixDataTypeHome getMatrixDataTypeHome() {
		return mMatrixDataTypeHome;
	}

	/**
	 * Set the MatrixDataTypeHome field.
	 */
	public void setMatrixDataTypeHome(MatrixDataTypeHome pNewMatrixDataTypeHome) {
		mMatrixDataTypeHome = pNewMatrixDataTypeHome;
	}

	/**
	 * Clear all internal data and data references.
	 * 
	 */
	public void clearData() {
		setMatrixDataTypeHome(null);
		setTaxonLabelHome(null);
		setStudy(null);
	
	}

}