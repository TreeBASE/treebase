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

package org.cipres.treebase.domain.study;

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
}
