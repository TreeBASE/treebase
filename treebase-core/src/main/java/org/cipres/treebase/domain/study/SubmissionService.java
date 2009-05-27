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

import org.cipres.treebase.dao.jdbc.NexusDataSetJDBC;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.event.ProgressionListener;
import org.cipres.treebase.service.AbstractService;

/**
 * SubmissionService.java
 * 
 * Created on April 20, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface SubmissionService extends AbstractService {

	/**
	 * Get Submission object by its ID
	 * 
	 * @param pSubmissionID
	 * @return Submission
	 */
	Submission findSubmissionByID(Long pSubmissionID);

	/**
	 * Find all submissions in "Ready" to publish state.
	 * 
	 * @return
	 */
	Collection<Submission> findReadySubmissions();

	Collection<Submission> findInProgressSubmissions();

	Collection<Submission> findPublishedSubmissions();

	/**
	 * Create a submission, which associates with a new study. A submitter is required.
	 * 
	 * @param pSubmitter
	 * @param pStudy
	 * @return
	 */
	Submission createSubmission(User pSubmitter, Study pStudy);

	/**
	 * Delete a submission and its associated study object. Only in progress submissions can be
	 * deleted. Return true if the deletion is successful.
	 * 
	 * @param pSubmission
	 * @return
	 */
	boolean deleteSubmission(Submission pSubmission);

	/**
	 * Delete a submission and its associated study object. Only in progress submissions can be
	 * deleted. Return true if the deletion is successful.
	 * 
	 * @param pSubId
	 * @return
	 */
	boolean deleteSubmission(Long pSubId);

	/**
	 * Delete all submitted data for a submission: matrices, trees, and taxonlabels.
	 * 
	 * Return true if the deletion is successful.
	 * 
	 * @param pSubmission
	 * @return
	 */
	boolean deleteSubmittedData(Submission pSubmission);

	/**
	 * Parse the Nexus files, add the data to the submission, and persist to the database.
	 * 
	 * If a progress listener is specified, the parsing progress will be reported, and this method
	 * would return immediately without waiting for the parsing completion.
	 * 
	 * @param pSubmission
	 * @param pNexusFiles
	 * @param pListener
	 * 
	 * @return UploadFileResult
	 */
	UploadFileResult addNexusFilesJDBC(
		Submission pSubmission,
		Collection<File> pNexusFiles,
		ProgressionListener pListener);

	/**
	 * Parse the Nexus files, and add the data to the submission.
	 * 
	 * If a progress listener is specified, the parsing progress will be reported, and this method
	 * would return immediately without waiting for the parsing completion.
	 * 
	 * Warning: internal API. Use processNexusFiles() instead.
	 * 
	 * @param pSubmission
	 * @param pNexusFiles
	 * @param pListener
	 * @return the data to be processed by direct JDBC for performance reason.
	 */
	@Deprecated
	NexusDataSetJDBC addNexusFiles(
		Submission pSubmission,
		Collection<File> pNexusFiles,
		ProgressionListener pListener);

	/**
	 * Parse the Nexus file, and add the data to the submission.
	 * 
	 * Warning: internal API. Use processNexusFiles() instead.
	 * 
	 * @param pSubmission
	 * @param pNexusFile
	 * @return the data to be processed by direct JDBC for performance reason.
	 */
	// NexusDataSet addNexusFile(Submission pSubmission, File pNexusFile);
	/**
	 * Check the accession right for the given user and the submission.
	 * <ul>
	 * <li>** An admin user has full rights, otherwise <br>
	 * <li>** A submitter has full rights for associated in progress submissions, otherwise<br>
	 * <li>** A submitter has limited write right for associated submitted submissions, otherwise<br>
	 * <li>** A non-public study is hidden, otherwise
	 * <li>** Return read-only permission
	 * </ul>
	 * 
	 * @param pUsername
	 * @param pSubmissionID
	 * @return
	 */
	TBPermission getPermission(String pUsername, Long pSubmissionID);

	/**
	 * Label the study in progress in treebase.
	 * 
	 * @param pSubmissionID
	 */
	void updateStudyStatusInProgress(Long pSubmissionID);

	/**
	 * Label the study ready to be published in treebase.
	 * 
	 * @param pSubmissionID
	 */
	void updateStudyStatusReady(Long pSubmissionID);

	/**
	 * Publish the study in treebase.
	 * 
	 * @param pSubmissionID
	 */
	void updateStudyStatusPublish(Long pSubmissionID);

	Submission findByMatrix(Matrix pMatrix);
}
