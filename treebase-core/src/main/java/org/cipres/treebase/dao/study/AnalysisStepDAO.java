
package org.cipres.treebase.dao.study;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.study.AnalysisStepHome;

/**
 * AnalysisStepDAO.java
 * 
 * Created on May 26, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class AnalysisStepDAO extends AbstractDAO implements AnalysisStepHome {

	private static final Logger LOGGER = LogManager.getLogger(AnalysisStepDAO.class);

	/**
	 * Constructor.
	 */
	public AnalysisStepDAO() {
		super();
	}

	/**
	 * @param pPartialName
	 * @return
	 */
	public List<String> findCompleteSoftwareName(String pPartialName) {
		List<String> alist = new ArrayList<String>();

		if (!TreebaseUtil.isEmpty(pPartialName) && pPartialName.trim().length() >= 1) {

			Query q = getSessionFactory().getCurrentSession().createQuery(
				"select distinct name from Software where lower(name) like :mStr");

			q.setString("mStr", pPartialName.trim().toLowerCase() + '%');
			alist = q.list();
		}
		return alist;
	}

}
