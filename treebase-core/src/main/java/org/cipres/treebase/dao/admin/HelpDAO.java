
package org.cipres.treebase.dao.admin;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.admin.Help;
import org.cipres.treebase.domain.admin.HelpHome;
import org.hibernate.query.Query;

/**
 * HelpDAO.java
 * 
 * Created on November 17, 2008
 * 
 * @author mjd 20081117
 * 
 */
public class HelpDAO extends AbstractDAO implements HelpHome {
	private static final Logger LOGGER = LogManager.getLogger(HelpDAO.class);

	/**
	 * Constructor.
	 */
	public HelpDAO() {
		super();
	}

	public Help findByID(Long helpId) {
		Query<Help> q = getSessionFactory().getCurrentSession()
			.createQuery("FROM Help WHERE id = :id", Help.class);
		q.setParameter("id", helpId);
		return oneOnly(q.getResultList());
	}

	public Help findByTag(String tag) {
		Query<Help> q = getSessionFactory().getCurrentSession()
			.createQuery("FROM Help WHERE tag = :tag", Help.class);
		q.setParameter("tag", tag);
		return oneOnly(q.getResultList());
	}

	private Help oneOnly(List<Help> results) {
		if (results.size() == 0)
			return null;
		else if (results.size() == 1)
			return results.get(0);
		else // XXX diagnose and log error here 
			return results.get(0);
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.admin.HelpHome#makeHelp(java.lang.String)
	 */
	public Help createHelp(String helpTag) {
		Help newHelp = new Help(helpTag);
		this.save(newHelp);
		return newHelp;
	}
}
