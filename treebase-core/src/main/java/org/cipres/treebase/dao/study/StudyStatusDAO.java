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

import org.hibernate.Criteria;

import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.study.StudyStatus;
import org.cipres.treebase.domain.study.StudyStatusHome;

/**
 * StudyStatusDAO.java
 * 
 * Created on Apr 20, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class StudyStatusDAO extends AbstractDAO implements StudyStatusHome {

	/**
	 * Constructor.
	 */
	public StudyStatusDAO() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyStatusHome#findStatusInProgress()
	 */
	public StudyStatus findStatusInProgress() {
		return findByDescription(StudyStatus.INPROGRESS);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyStatusHome#findStatusReady()
	 */
	public StudyStatus findStatusReady() {
		return findByDescription(StudyStatus.READY);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.StudyStatusHome#findStatusPublished()
	 */
	public StudyStatus findStatusPublished() {
		return findByDescription(StudyStatus.PUBLISHED);
	}

	/**
	 * Find by status description.
	 * 
	 * Creation date: Apr 20, 2006 2:34:56 PM
	 */
	private StudyStatus findByDescription(String pDesc) {
		StudyStatus returnVal = null;

		Criteria c = getSession().createCriteria(StudyStatus.class).add(
			org.hibernate.criterion.Expression.eq("description", pDesc));

		returnVal = (StudyStatus) c.uniqueResult();
		return returnVal;
	}
}
