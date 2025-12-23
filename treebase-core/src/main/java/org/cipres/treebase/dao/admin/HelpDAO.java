
package org.cipres.treebase.dao.admin;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.admin.Help;
import org.cipres.treebase.domain.admin.HelpHome;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

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
		List<Help> results;

		Criteria c = getSessionFactory().getCurrentSession().createCriteria(Help.class);
		c.add(Expression.eq("id", helpId));
		results = c.list();

		return oneOnly(results);
	}

	public Help findByTag(String tag) {
		List<Help> results;

		Criteria c = getSessionFactory().getCurrentSession().createCriteria(Help.class);
		c.add(Expression.eq("tag", tag));
		results = c.list();

		return oneOnly(results);
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
