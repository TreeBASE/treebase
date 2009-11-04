
package org.cipres.treebase.domain.study;

import org.cipres.treebase.service.AbstractService;

/**
 * CitationService.java
 * 
 * Created on May 26, 2006
 * 
 * @author Jin Ruan
 * 
 */
public interface CitationService extends AbstractService {

	/**
	 * Find the citation status based on the description.
	 * 
	 * @return CitationStatus
	 */
	CitationStatus findCitationStatusByDescription(String pDescription);

	/**
	 * Create a new citation by type. Return an ArticleCitation if the type parameter is null.
	 * 
	 * @param pCitationType
	 * @return Citation
	 */
	Citation createCitation(String pCitationType);

	/**
	 * Delete the instance from the database. Handles relationships.
	 * 
	 * Note: the caller is responsible to assign a new citation to the study before commit.
	 * 
	 * @param pCitation
	 */
	void deleteCitation(Citation pCitation);

	/**
	 * Citation type changed. Need to create a new citation object and copy the properties.
	 * 
	 * @param pStudy
	 * @param pCitation
	 */
	void replaceCitation(Study pStudy, Citation pCitation);

}
