package org.cipres.treebase.dao.study;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.study.Algorithm;
import org.cipres.treebase.domain.study.AlgorithmHome;

/**
 * @author madhu
 * 
 */
public class AlgorithmDAO extends AbstractDAO implements AlgorithmHome {

	/**
	 * Constructor.
	 */
	public AlgorithmDAO() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cipres.treebase.domain.study.AlgorithmHome#finalAllUniqueAlgorithmDescriptions()
	 */
	public List<String> findAllUniqueAlgorithmDescriptions() {

		// case insensitive search
		String sql = "select distinct lower(alg.description) from Algorithm alg";
		// String sql = "select distinct alg.description from Algorithm alg";
		Query q = getSessionFactory().getCurrentSession().createQuery(sql);
		List<String> returnVal = q.list();
		return returnVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cipres.treebase.domain.study.AlgorithmHome#findByDescription(java.lang.String)
	 */
	public Algorithm findByDescription(String pDesc) {
		Algorithm returnVal = null;

		if (!TreebaseUtil.isEmpty(pDesc)) {
			Criteria c = getSessionFactory().getCurrentSession().createCriteria(Algorithm.class);
			c.add(Expression.eq("description", pDesc).ignoreCase());

			returnVal = (Algorithm) c.uniqueResult();
		}
		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.AlgorithmHome#findAllUniqueOtherAlgorithmDescriptions()
	 */
	public List<String> findAllUniqueOtherAlgorithmDescriptions(String pPartialName) {
		// case insensitive search
		String sql = "select distinct description  from OtherAlgorithm where lower(description) like :mStr";
		// String sql = "select distinct alg.description from OtherAlgorithm alg";
		Query q = getSessionFactory().getCurrentSession().createQuery(sql);
		q.setString("mStr", pPartialName.trim().toLowerCase() + '%');
		List<String> returnVal = q.list();
		return returnVal;

	}

}
