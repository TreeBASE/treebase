
package org.cipres.treebase.dao.study;

import java.util.Collection;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.dao.AbstractDAO;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.StudyStatus;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.TreeBlock;

/**
 * SubmissionDAO.java
 * 
 * Created on Apr 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
public class SubmissionDAO extends AbstractDAO implements SubmissionHome {

	/**
	 * Constructor.
	 */
	public SubmissionDAO() {
		super();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionHome#findBySubmissionNumber(java.lang.String)
	 */
	public Submission findBySubmissionNumber(String pSubmissionNumber) {
		Submission returnVal = null;

		if (!TreebaseUtil.isEmpty(pSubmissionNumber)) {
			Criteria c = getSession().createCriteria(Submission.class);
			c.add(Expression.eq("submissionNumber", pSubmissionNumber));

			returnVal = (Submission) c.uniqueResult();
		}
		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionHome#delete(org.cipres.treebase.domain.study.Submission)
	 */
	public void delete(Submission pSubmission) {
		if (pSubmission != null && pSubmission.getId() != null) {

			// TODO: cascade delete submitted data: tree block, matrix, taxon
			pSubmission.clearSubmittedData();

			getHibernateTemplate().delete(pSubmission);
		}
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionHome#findByMatrix(org.cipres.treebase.domain.matrix.Matrix)
	 */
	public Submission findByMatrix(Matrix pMatrix) {
		Submission returnVal = null;

		if (pMatrix != null) {
			Long mID = pMatrix.getId();
			Query q = getSession()
				.createQuery(
					"select sub from Submission as sub join sub.submittedMatrices as matrix where matrix.id = :mID");

			q.setLong("mID", mID);
			Object o = q.uniqueResult();

			returnVal = (Submission) o;
		}
		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionHome#findByTree(org.cipres.treebase.domain.tree.PhyloTree)
	 */
	public Submission findByTree(PhyloTree pTree) {
		Submission returnVal = null;

		if (pTree != null) {
			Long mID = pTree.getId();
			Query q = getSession()
				.createQuery(
					"select sub from Submission sub join sub.submittedTreeBlocks block join block.treeList tree where tree.id = :mID");

			q.setLong("mID", mID);
			Object o = q.uniqueResult();

			returnVal = (Submission) o;
		}
		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionHome#findByTreeBlock(org.cipres.treebase.domain.tree.TreeBlock)
	 */
	public Submission findByTreeBlock(TreeBlock pBlock) {
		Submission returnVal = null;

		if (pBlock != null) {
			Long mID = pBlock.getId();
			Query q = getSession()
				.createQuery(
					"select sub from Submission as sub join sub.submittedTreeBlocks as treeblock where treeblock.id = :mID");

			q.setLong("mID", mID);
			Object o = q.uniqueResult();

			returnVal = (Submission) o;
		}
		return returnVal;
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionHome#findByReadyState()
	 */
	public Collection<Submission> findByReadyState() {
		Query q = getSession().createQuery(
			"select sub from Submission as sub where sub.study.studyStatus.description = :state");

		q.setString("state", StudyStatus.READY);

		return q.list();
	}

	
	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionHome#findByReadyState()
	 */
	public Collection<Submission> findByPublishedState() {
		Query q = getSession().createQuery(
			"select sub from Submission as sub where sub.study.studyStatus.description = :state");

		q.setString("state", StudyStatus.PUBLISHED);

		return q.list();
	}

	/**
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionHome#findByReadyState()
	 */
	public Collection<Submission> findByInProgressState() {
		Query q = getSession().createQuery(
			"select sub from Submission as sub where sub.study.studyStatus.description = :state");

		q.setString("state", StudyStatus.INPROGRESS);

		return q.list();
	}

	/** 
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionHome#findByStudyAccessionNumber(java.lang.String)
	 */
	public Submission findByStudyAccessionNumber(String pAccNumber) {
		if (TreebaseUtil.isEmpty(pAccNumber)) {
			return null;
		}
		
		Query q = getSession().createQuery(
		"select sub from Submission as sub where lower(sub.study.accessionNumber) = :accNumber");

		q.setString("accNumber", pAccNumber.toLowerCase());

		return (Submission) q.uniqueResult();
	}
	
	public Collection<Submission> findByCreateDateRange(Date from, Date util) {
		Query q = getSession().createQuery(
			"from Submission as sub where sub.createDate between :begin and :end");

		q.setDate("begin", from);
		q.setDate("end", util);
		return q.list();
	}

}
