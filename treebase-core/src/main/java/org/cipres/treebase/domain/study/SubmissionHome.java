
package org.cipres.treebase.domain.study;

import java.util.Date;
import java.util.Collection;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.TreeBlock;

/**
 * SubmissionHome.java
 * 
 * Created on Apr 21, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface SubmissionHome extends DomainHome {

	/**
	 * Find by the submission number. Return null if no match is found. The search is case
	 * sensitive.
	 * 
	 * @param pSubmissionNumber String
	 * @return Submission
	 */
	Submission findBySubmissionNumber(String pSubmissionNumber);

	/**
	 * Find by the study accession number. 
	 * Return null if no match is found. The search is case insensitive.
	 * 
	 * @param pAccNumber String
	 * @return Submission
	 */
	Submission findByStudyAccessionNumber(String pAccNumber);

	/**
	 * Find by the associated matrix. Returns null if no match is found.
	 * 
	 * @param pMatrix
	 * @return
	 */
	Submission findByMatrix(Matrix pMatrix);

	/**
	 * Find by the associated tree. Returns null if no match is found.
	 * 
	 * @param pTree
	 * @return
	 */
	Submission findByTree(PhyloTree pTree);

	/**
	 * Find by the associated tree block. Returns null if no match is found.
	 * 
	 * @param pBlock
	 * @return
	 */
	Submission findByTreeBlock(TreeBlock pBlock);

	/**
	 * Find all submissions in "Ready" to publish state.
	 * 
	 * @return
	 */
	Collection<Submission> findByReadyState();

	Collection<Submission> findByPublishedState();

	Collection<Submission> findByInProgressState();
	Collection<Submission> findByCreateDateRange(Date from, Date util);
}
