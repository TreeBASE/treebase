
package org.cipres.treebase.dao.study;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;

import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.AnalyzedDataHome;
import org.cipres.treebase.domain.tree.PhyloTree;

/**
 * AnalyzedDataDAO.java
 * 
 * Created on May 26, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class AnalyzedDataDAO extends AbstractDAO implements AnalyzedDataHome {

	/**
	 * Constructor.
	 */
	public AnalyzedDataDAO() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.AnalyzedDataHome#findByMatrix(org.cipres.treebase.domain.matrix.Matrix)
	 */
	public Collection<AnalyzedData> findByMatrix(Matrix pMatrix) {
		Collection<AnalyzedData> returnVal = new ArrayList<AnalyzedData>();

		if (pMatrix != null) {
			Long mID = pMatrix.getId();
			Query q = getSession().createQuery("from AnalyzedMatrix where matrix.id = :mID");

			q.setLong("mID", mID);
			List results = q.list();

			returnVal = results;
		}
		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.AnalyzedDataHome#findByTree(org.cipres.treebase.domain.tree.PhyloTree)
	 */
	public Collection<AnalyzedData> findByTree(PhyloTree pTree) {
		Collection<AnalyzedData> returnVal = new ArrayList<AnalyzedData>();

		if (pTree != null) {
			Long mID = pTree.getId();
			Query q = getSession().createQuery("from AnalyzedTree where tree.id = :mID");

			q.setLong("mID", mID);
			List results = q.list();

			returnVal = results;
		}
		return returnVal;
	}

}
