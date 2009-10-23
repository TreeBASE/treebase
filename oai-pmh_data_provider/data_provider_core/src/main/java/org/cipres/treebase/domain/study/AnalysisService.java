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

import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.service.AbstractService;

/**
 * AnalysisService.java
 * 
 * Created on Jun 6, 2006
 * 
 * @author lcchan
 * 
 */
public interface AnalysisService extends AbstractService {

	/**
	 * Return an Analysis object by id.
	 * 
	 * @param pAnalysisID
	 * @return
	 */
	Analysis findByID(Long pAnalysisID);

	/**
	 * Delete an analysis and its assoicated objects.
	 * 
	 * Return true if the deletion is successful.
	 * 
	 * @param pAnalysis
	 * @return
	 */
	boolean deleteAnalysis(Analysis pAnalysis);

	/**
	 * Delete analyses and their associated objects.
	 * 
	 * @param pAnalyses
	 * @return
	 */
	void deleteAnalyses(Collection<Analysis> pAnalyses);

	/**
	 * Delete analyses and their associated objects for a study.
	 * 
	 * @param pStudy
	 * @return
	 */
	void deleteAnalyses(Study pStudy);

	/**
	 * Delegate to SubmissionService.getPermission() to check the user access rights. 
	 *
	 * @see org.cipres.treebase.domain.study.SubmissionService.getPermission(String, Long)
	 * @param pUsername
	 * @param pAnalysisId
	 * @return
	 */
	TBPermission getPermission(String pUsername, Long pAnalysisId);

}
