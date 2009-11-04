
package org.cipres.treebase.dao.matrix;

import org.hibernate.Criteria;

import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.matrix.MatrixDataType;
import org.cipres.treebase.domain.matrix.MatrixDataTypeHome;

/**
 * MatrixDataTypeDAO.java
 * 
 * Created on Jun 12, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class MatrixDataTypeDAO extends AbstractDAO implements MatrixDataTypeHome {

	/**
	 * Constructor.
	 */
	public MatrixDataTypeDAO() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.matrix.MatrixDataTypeHome#findByDescription(java.lang.String)
	 */
	public MatrixDataType findByDescription(String pDescription) {
		MatrixDataType returnVal = null;

		Criteria c = getSession().createCriteria(MatrixDataType.class).add(
			org.hibernate.criterion.Expression.eq("description", pDescription));

		returnVal = (MatrixDataType) c.uniqueResult();
		return returnVal;
	}

}
