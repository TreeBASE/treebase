/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.dao.study;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
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

	private static final Logger LOGGER = Logger.getLogger(AnalysisStepDAO.class);

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

			Query q = getSession().createQuery(
				"select distinct name from Software where lower(name) like :mStr");

			q.setString("mStr", pPartialName.trim().toLowerCase() + '%');
			alist = q.list();
		}
		return alist;
	}

}
