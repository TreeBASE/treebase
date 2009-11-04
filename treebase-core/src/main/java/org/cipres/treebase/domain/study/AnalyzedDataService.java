
package org.cipres.treebase.domain.study;

import java.util.Collection;

import org.cipres.treebase.service.AbstractService;

/**
 * AnalyzedDataService.java
 * 
 * Created on June 8, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface AnalyzedDataService extends AbstractService {

	/**
	 * Delete an analysis data and its associated objects.
	 * 
	 * Return true if the deletion is successful.
	 * 
	 * @param pData
	 * @return
	 */
	boolean deleteAnalyzedData(AnalyzedData pData);

	/**
	 * Delete a collection of analysis data and their associated objects.
	 * 
	 * @param pAnalysisSteps
	 * @return
	 */
	void deleteAnalyzedDataCollection(Collection<? extends AnalyzedData> pAnalyzedData);

}
