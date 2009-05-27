/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

package org.cipres.treebase.dao.admin;

import java.util.List;

import org.apache.log4j.Logger;
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
	private static final Logger LOGGER = Logger.getLogger(HelpDAO.class);

	/**
	 * Constructor.
	 */
	public HelpDAO() {
		super();
	}

	public Help findByID(Long helpId) {
		List<Help> results;

		Criteria c = getSession().createCriteria(Help.class);
		c.add(Expression.eq("id", helpId));
		results = c.list();

		return oneOnly(results);
	}

	public Help findByTag(String tag) {
		List<Help> results;

		Criteria c = getSession().createCriteria(Help.class);
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
