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
import org.cipres.treebase.domain.study.CitationStatus;
import org.cipres.treebase.domain.study.CitationStatusHome;

/**
 * CitationStatusDAO.java
 * 
 * Created on Nov 20, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class CitationStatusDAO extends AbstractDAO implements CitationStatusHome {

	/**
	 * Constructor.
	 */
	public CitationStatusDAO() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.CitationStatusHome#findStatusAccepted()
	 */
	public CitationStatus findStatusAccepted() {
		return findStatusByDescription(CitationStatus.ACCEPTED_MINIOR_CHANGES);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.CitationStatusHome#findStatusInPrep()
	 */
	public CitationStatus findStatusInPrep() {
		return findStatusByDescription(CitationStatus.INPREP);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.CitationStatusHome#findStatusInPress()
	 */
	public CitationStatus findStatusInPress() {
		return findStatusByDescription(CitationStatus.INPRESS);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.CitationStatusHome#findStatusInReview()
	 */
	public CitationStatus findStatusInReview() {
		return findStatusByDescription(CitationStatus.INREVIEW);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.CitationStatusHome#findStatusPublished()
	 */
	public CitationStatus findStatusPublished() {
		return findStatusByDescription(CitationStatus.PUBLISHED);
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.CitationStatusHome#findStatusByDescription(java.lang.String)
	 */
	public CitationStatus findStatusByDescription(String pDescription) {
		CitationStatus returnVal = null;

		Criteria c = getSession().createCriteria(CitationStatus.class).add(
			org.hibernate.criterion.Expression.eq("description", pDescription));

		returnVal = (CitationStatus) c.uniqueResult();
		return returnVal;
	}

}
