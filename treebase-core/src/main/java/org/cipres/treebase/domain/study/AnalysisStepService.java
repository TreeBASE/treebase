
package org.cipres.treebase.domain.study;

import java.util.Collection;
import java.util.List;

import org.cipres.treebase.service.AbstractService;

/**
 * AnalysisStepService.java
 * 
 * Created on Jun 8, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface AnalysisStepService extends AbstractService {

	/**
	 * Return an Analysis step object by id.
	 * 
	 * @param pAnalysisStepID
	 * @return
	 */
	AnalysisStep findByID(Long pAnalysisStepID);

	/**
	 * Delete an analysis step and its associated objects.
	 * 
	 * Return true if the deletion is successful.
	 * 
	 * @param pAnalysisStep
	 * @return
	 */
	boolean deleteAnalysisStep(AnalysisStep pAnalysisStep);

	/**
	 * Delete analysis steps and its associated objects.
	 * 
	 * @param pAnalysisSteps
	 * @return
	 */
	void deleteAnalysisSteps(Collection<AnalysisStep> pAnalysisSteps);

	/**
	 * Find all unique algorithm descriptions.
	 * 
	 * @return
	 */
	List<String> findUniqueAlgorithmDescriptions();

}
