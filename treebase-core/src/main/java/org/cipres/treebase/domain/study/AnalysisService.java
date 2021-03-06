
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
