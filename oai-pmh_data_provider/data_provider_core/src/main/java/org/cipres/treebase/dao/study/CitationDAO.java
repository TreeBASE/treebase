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

import org.hibernate.Query;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.study.CitationHome;

/**
 * CitationDAO.java
 * 
 * Created on May 26, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class CitationDAO extends AbstractDAO implements CitationHome {

	/**
	 * Constructor.
	 */
	public CitationDAO() {
		super();
	}

	/**
	 * @param pPartialName
	 * @return a list of the matches.
	 */
	public List<String> findCompleteJournalName(String pPartialName) {
		List<String> alist = new ArrayList<String>();

		if (!TreebaseUtil.isEmpty(pPartialName) && pPartialName.trim().length() >= 1) {

			Query q = getSession().createQuery(
				"select distinct journal from Citation where lower(journal) like :mStr");

			q.setString("mStr", pPartialName.trim().toLowerCase() + '%');
			alist = q.list();
		}
		return alist;
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.study.CitationHome#replaceAuthorAndEditor(org.cipres.treebase.domain.admin.Person, org.cipres.treebase.domain.admin.Person)
	 */
	public int replaceAuthorAndEditor(Person pSrc, Person pTarget) {
		if (pSrc == null || pTarget == null) {
			return 0;
		}
		
		int changeCount = 0;
		
		String query = "UPDATE CITATION_EDITOR SET EDITORS_PERSON_ID= :targetId WHERE EDITORS_PERSON_ID=:srcId";
		changeCount += replaceQuery(pSrc, pTarget, query);

		query = "UPDATE CITATION_AUTHOR SET AUTHORS_PERSON_ID= :targetId WHERE AUTHORS_PERSON_ID=:srcId";
		changeCount += replaceQuery(pSrc, pTarget, query);
		
		return changeCount;
	}

	/**
	 * 
	 * @param pSrc
	 * @param pTarget
	 * @param query
	 */
	private int replaceQuery(Person pSrc, Person pTarget, String pQuery) {
		Query q = getSession().createSQLQuery(pQuery);
		
		q.setParameter("srcId", pSrc.getId());
		q.setParameter("targetId", pTarget.getId());
		
		return q.executeUpdate();
	}

}
