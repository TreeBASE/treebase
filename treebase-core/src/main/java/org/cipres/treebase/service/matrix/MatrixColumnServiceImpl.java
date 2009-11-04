
package org.cipres.treebase.service.matrix;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.matrix.MatrixColumn;
import org.cipres.treebase.domain.matrix.MatrixColumnHome;
import org.cipres.treebase.domain.matrix.MatrixColumnService;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * MatrixColumnServiceImpl.java
 * 
 * Created on Jun 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class MatrixColumnServiceImpl extends AbstractServiceImpl implements MatrixColumnService {

	private MatrixColumnHome mMatrixColumnHome;

	/**
	 * Constructor.
	 */
	public MatrixColumnServiceImpl() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return getMatrixColumnHome();
	}

	/**
	 * Return the MatrixColumnHome field.
	 * 
	 * @return MatrixColumnHome mMatrixColumnHome
	 */
	private MatrixColumnHome getMatrixColumnHome() {
		return mMatrixColumnHome;
	}

	/**
	 * Set the MatrixColumnHome field.
	 */
	public void setMatrixColumnHome(MatrixColumnHome pNewMatrixColumnHome) {
		mMatrixColumnHome = pNewMatrixColumnHome;
	}

	@Override
	public Class defaultResultClass() {
		return MatrixColumn.class;
	}

}
