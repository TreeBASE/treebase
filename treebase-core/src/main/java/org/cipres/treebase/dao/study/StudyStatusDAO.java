
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
