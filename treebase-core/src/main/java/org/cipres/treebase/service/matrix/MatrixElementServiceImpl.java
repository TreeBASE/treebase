
package org.cipres.treebase.service.matrix;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.matrix.MatrixElement;
import org.cipres.treebase.domain.matrix.MatrixElementHome;
import org.cipres.treebase.domain.matrix.MatrixElementService;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * MatrixElementServiceImpl.java
 * 
 * Created on Jun 16, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class MatrixElementServiceImpl extends AbstractServiceImpl implements MatrixElementService {

	private MatrixElementHome mMatrixElementHome;

	/**
	 * Constructor.
	 */
	public MatrixElementServiceImpl() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.service.AbstractServiceImpl#getDomainHome()
	 */
	@Override
	protected DomainHome getDomainHome() {
		return getMatrixElementHome();
	}

	/**
	 * Return the MatrixElementHome field.
	 * 
	 * @return MatrixElementHome mMatrixElementHome
	 */
	private MatrixElementHome getMatrixElementHome() {
		return mMatrixElementHome;
	}

	/**
	 * Set the MatrixElementHome field.
	 */
	public void setMatrixElementHome(MatrixElementHome pNewMatrixElementHome) {
		mMatrixElementHome = pNewMatrixElementHome;
	}

	@Override
	public Class defaultResultClass() {
		return MatrixElement.class;
	}
}
