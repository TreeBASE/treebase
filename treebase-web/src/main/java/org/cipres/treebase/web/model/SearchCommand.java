
package org.cipres.treebase.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private static final Logger LOGGER = LogManager.getLogger(SearchCommand.class);
	
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
