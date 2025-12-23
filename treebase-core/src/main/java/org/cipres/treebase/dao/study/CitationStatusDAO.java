
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

		Criteria c = getSessionFactory().getCurrentSession().createCriteria(CitationStatus.class).add(
			org.hibernate.criterion.Expression.eq("description", pDescription));

		returnVal = (CitationStatus) c.uniqueResult();
		return returnVal;
	}

}
