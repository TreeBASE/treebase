
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