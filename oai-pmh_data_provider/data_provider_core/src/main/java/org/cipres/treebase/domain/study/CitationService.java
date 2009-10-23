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
