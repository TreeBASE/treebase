
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
