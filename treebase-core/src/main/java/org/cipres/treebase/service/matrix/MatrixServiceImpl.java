
package org.cipres.treebase.service.matrix;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixHome;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * MatrixServiceImpl.java
 * 
 * Created on Jun 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class MatrixServiceImpl extends AbstractServiceImpl implements MatrixService {

	private MatrixHome mMatrixHome;

	/**
	 * Constructor.
	 */
	public MatrixServiceImpl() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return getMatrixHome();
	}

	/**
	 * Return the MatrixHome field.
	 * 
	 * @return MatrixHome mMatrixHome
	 */
	private MatrixHome getMatrixHome() {
		return mMatrixHome;
	}

	/**
	 * Set the MatrixHome field.
	 */
	public void setMatrixHome(MatrixHome pNewMatrixHome) {
		mMatrixHome = pNewMatrixHome;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixService#deleteMatrix(org.cipres.treebase.domain.matrix.Matrix)
	 */
	public boolean deleteMatrix(Matrix pMatrix) {

		if (pMatrix == null) {
			return false;
		}

		// Let the home object handle the cascade delete.
		getMatrixHome().delete(pMatrix);

		return true;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixService#findByID(java.lang.Long)
	 */
	public Matrix findByID(Long pMatrixID) {
		if (pMatrixID == null) {
			return null;
		}
		return getMatrixHome().loadMatrixEagerFetch(pMatrixID);
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixService#findRowAsString(org.cipres.treebase.domain.matrix.CharacterMatrix, int, int)
	 */
	public List<String> findRowAsString(CharacterMatrix pMatrix, int pStart, int pEnd) {
		
		return getMatrixHome().findRowAsString(pMatrix, pStart, pEnd);
		
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.matrix.MatrixService#findMatricesByTitle(java.lang.String)
	 */
	public Set<Matrix> findMatricesByTitle(String title) {
		return getMatrixHome().findMatricesByTitle(title);
	}

	public Collection<Matrix> findByNexusFile(String fn) {
		return getMatrixHome().findByNexusFile(fn);
	}

	public Collection<Matrix> findByStudies(Set<Study> results) {
		return getMatrixHome().findByStudies(results);
	}
	
	/**
	 * @param pStudy - a study
	 * @return the matrices in that study
	 * @author mjd 20080723
	 * 
	 * XXX - a better implementation is in r5447, but right now
	 * the TB1 data is garbled, and that implementation doesn't work.
	 * It would work if the database were in a consistent state.
	 */
	public Collection<Matrix> findByStudy(Study pStudy) {
		Collection<Matrix> matrices = new HashSet<Matrix>();
		for (Analysis an : pStudy.getAnalysesReadOnly()) {
			for (AnalysisStep step : an.getAnalysisStepsReadOnly()) {
				for (AnalyzedData data : step.getDataSetReadOnly()) {
					Matrix matrixData = data.getMatrixData();
					if (matrixData != null) {
						matrices.add(matrixData);
					}
				}
			}
		}
		return matrices;
	}

	public String getIDPrefix() {
		return "M";
	}

	@Override
	public Class defaultResultClass() {
		return Matrix.class;
	}
}
