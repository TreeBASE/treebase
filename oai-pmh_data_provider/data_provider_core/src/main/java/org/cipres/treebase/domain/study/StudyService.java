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

import java.io.File;
import java.util.Collection;
import java.util.Set;

import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.service.AbstractService;

/**
 * StudyService.java
 * 
 * Created on Apr 20, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface StudyService extends AbstractService {

	/**
	 * Return a study based on the submission ID. Return null if not found.
	 * 
	 * @param pSubmissionID
	 */
	Study findBySubmissionID(Long pSubmissionID);

	/**
	 * Return a study. Return null if not found.
	 * 
	 * @param pStudyID
	 */
	Study findByID(Long pStudyID);

	/**
	 * Return a study based on the submission number. Return null if not found. The search is case
	 * sensitive.
	 * 
	 * @param pSubmissionNumber
	 */
	Study findBySubmissionNumber(String pSubmissionNumber);

	/**
	 * Find studies by name. Return an empty set if no match is found.
	 * 
	 * @param pName
	 * @param pCaseSensitive
	 * 
	 * @return Collection<Study>
	 */
	Collection<Study> findByName(String pStudyName, boolean pCaseSensitive);

	/**
	 * Advanced study query by criteria.
	 * 
	 * @param pCriteria
	 * @return
	 */
	Collection<Study> findByCriteria(StudyCriteria pCriteria);

	/**
	 * Delete a study and its associated objects. Only in progress study can be deleted.
	 * 
	 * Return true if the deletion is successful.
	 * 
	 * @deprecated Use deleteDubmission instead
	 * @param pStudy
	 * @return
	 */
	@Deprecated
	boolean deleteStudy(Study pStudy);
	@Deprecated
	boolean deleteStudy(Long pStudyID);

	/**
	 * Add the Nexus files as Clob to the study.
	 * 
	 * @param pStudy
	 * @param pNexusFiles
	 */
	void addNexusFiles(Study pStudy, Collection<File> pNexusFiles);

	/**
	 * FIXME Delegate to SubmissionService.getPermission() to check the user access rights.
	 * 
	 * @see org.cipres.treebase.domain.study.SubmissionService.getPermission(String, Long)
	 * @param pUsername
	 * @param pStudyId
	 * @return
	 */
	TBPermission getPermission(String pUsername, Long pStudyId);

	/*
	 * mjd
	 */
	void cleanCache();

	/**
	 * Search for a list of studies by the keyword parameter. Generate a xml string to represent the
	 * studies.
	 * 
	 * @param pKeyword
	 * @return
	 */
	String generateXMLStringByKeyword(String pKeyword);

	/**
	 * Reconstruct a nexus file from matrices, trees from the content in the original nexus file in
	 * the specified submission.
	 * 
	 * @param pSubmissionId
	 * @param pFileName
	 * @return
	 */
	String generateReconstructedNexusFile(Long pSubmissionId, String pFileName);

	/**
	 * Find studies whose text contains the keyword anywhere
	 * @param keyword - target word
	 * @return a collection of matching studies
	 * @author mjd
	 */
	Collection<Study> findByKeyword(String keyword);

	/**
	 * Find studies whose authors match the specified text
	 * @param pAuthorName
	 * @return a collection of matching studies
	 * @author mjd
	 */
	Collection<Study> findByAuthor(String pAuthorName);

	/**
	 * Find studies with the specified keyword in the title
	 * @param pTitle
	 * @return a collection of matching studies
	 * @author mjd
	 */
	Collection<Study> findByTitle(String pTitle);

	
	/**
	 * Find studies whose abstracts contain the specified keyword
	 * @param pAbstractWord
	 * @return a collection of matching studies
	 * @author mjd
	 */
	Collection<Study> findByAbstract(String pAbstractWord);

	/**
	 * Find studies with the specified keyword somewhere in the citation
	 * @param pCitationWord
	 * @return a collection of matching studies
	 * @author mjd
	 */
	Collection<Study> findByCitation(String pCitationWord);

	/**
	 * Find studies whose TB1 studyID is as specified
	 * @param pStudyID
	 * @return a collection of matching studies
	 * @author mjd
	 */
	Collection<Study> findByTBStudyID(String pStudyID);

	void setTBStudyID(Study theStudy, String studyID);

	/**
	 * Find all the studies that contain any of the specified trees
	 * 
	 * <p>For each tree in the specified set, find the containing study.  
	 * Return a collection of all such studies.</p>
	 * 
	 * @param trees
	 * @return the studies that contain these trees
	 * @author mjd
	 */
	Collection<Study> findByTrees(Set<PhyloTree> trees);
	
	/**
	 * Return all studies that contain a taxonlabel that looks like the argument
	 * 
	 * @param taxonLabel
	 * @return
	 * @author mjd 20080813
	 */
	Collection<Study> findByTaxonLabelName(String taxonLabel);
}
