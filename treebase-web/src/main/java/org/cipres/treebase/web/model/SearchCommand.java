/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ 
 * All Rights Reserved. 
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for educational, research and non-profit purposes, without
 * fee, and without a written agreement is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies. 
 *
 * Permission to incorporate this software into commercial products may be
 * obtained by contacting us:
 *              http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event
 * shall the CIPRES project be liable to any party for direct, indirect,
 * special, incidental, or consequential damages, including lost profits, 
 * arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage.  
 * The CIPRES project specifically disclaims any warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for
 * a particular purpose. The CIPRES project has no obligations to provide 
 * maintenance, support, updates, enhancements, or modifications. 
 */

package org.cipres.treebase.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.cipres.treebase.domain.search.SearchResults;
import org.cipres.treebase.domain.search.SearchResultsType;
import org.cipres.treebase.web.exceptions.DeprecatedFeatureError;

/**
 * SearchCommand.java
 * 
 * @author mjd 2008
 *
 */
public class SearchCommand {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(SearchCommand.class);
	
	SearchResults<?> results;
	
	public SearchCommand() { results = null; }

	public SearchCommand(SearchResults<?> results) {
		this.results = results;
	}

	public SearchCommand(HttpServletRequest request) {
		SearchCommand old = (SearchCommand) request.getAttribute("searchResults");
		
		if (old != null )
			setResults(old.getResults());
	}

	public SearchResults<?> getResults() {
		return results;
	}

	public void setResults(SearchResults<?> results) {
		this.results = results;
	}


	public SearchResultsType getResultType() {
		return results.getResultType();
	}

	public void makeEmpty() {
		results.makeEmpty();
	}

	public void setAll() {
		results.setAll();
	}

	public int size() {
		return results.size();
	}

	@Deprecated
	public List<SearchCriteriaCommand> getCriterias() {
		throw new DeprecatedFeatureError("Method eliminated and should be unused - 20081218 mjd");
	}

	@Deprecated
	public void setCriterias(ArrayList<SearchCriteriaCommand> arrayList) {
		throw new DeprecatedFeatureError("Method eliminated and should be unused - 20081218 mjd");
	}

}
