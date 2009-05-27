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

import java.util.Collection;

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

}
