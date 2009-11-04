
package org.cipres.treebase.domain.study;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.tree.PhyloTree;

/**
 * AnalyzedMatrix.java
 * 
 * Created on Feb 23, 2006
 * 
 * @author Jin Ruan
 * 
 */
@Entity
@DiscriminatorValue("M")
public class AnalyzedMatrix extends AnalyzedData {

	private static final long serialVersionUID = 485965817710183708L;

	private Matrix mMatrix;

	/**
	 * Constructor.
	 */
	public AnalyzedMatrix() {
		super();
	}

	/**
	 * Return the Matrix field.
	 * 
	 * @return Matrix
	 */
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name = "MATRIX_ID")
	public Matrix getMatrix() {
		return mMatrix;
	}

	/**
	 * Set the Matrix field.
	 */
	public void setMatrix(Matrix pNewMatrix) {
		mMatrix = pNewMatrix;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.AnalyzedData#registerAnalysisStep(org.cipres.treebase.domain.study.AnalysisStep)
	 */
	@Override
	public void registerAnalysisStep(AnalysisStep pNewAnalysisStep) {
		super.registerAnalysisStep(pNewAnalysisStep);

		// matrix should not be null:
		Study s = null;
		if (pNewAnalysisStep != null) {
			s = pNewAnalysisStep.getAnalysis().getStudy();
		}
		
		if (getMatrix() != null) {
			getMatrix().setStudy(s);
		}
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.study.AnalyzedData#getMatrixData()
	 */
	@Override
	@Transient
	public Matrix getMatrixData() {
		return getMatrix();
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.study.AnalyzedData#getTreeData()
	 */
	@Override
	@Transient
	public PhyloTree getTreeData() {
		return null;
	}

	@Override
	@Transient
	public String getDataType() {
		return "matrix";
	}

	@Override
	@Transient
	public String getDisplayName() {
		// TODO Auto-generated method stub
		Matrix theMatrix = getMatrix();
		return theMatrix == null ? "(none)" : theMatrix.getTitle();
	}

}
