
package org.cipres.treebase.domain.study;

import java.io.File;
import java.util.Collection;
import java.util.Date;
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
	 * Find studies by journal name. Return an empty set if no match is found.
	 * @param pJournal
	 * @param pCaseSensitive
	 * @return Collection<Study>
	 */
	Collection<Study> findByJournal(String pJournal, boolean pCaseSensitive);

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
	 * Find studies whose citation publication date is within the range
	 * @param from - a starting date
	 * @param until - a closing date
	 * @return
	 */
	Collection<Study> findByPublicationDateRange(Date from, Date until);
	
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
	
	/**
	 * Return the study for publication with provided DOI
	 * @param doi
	 * @return
	 */
	Study findByDOI(String doi);
}
