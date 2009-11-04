
package org.cipres.treebase.web.model;

import java.util.Collection;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.search.SearchResultsType;

/**
 * @author mjd 20081218
 *
 */
public class TaxonSearchCommand  extends SearchCommand {
	Collection<Taxon> tempSearchResults = new HashSet<Taxon> ();

	public Collection<Taxon> getTempSearchResults() {
		return tempSearchResults;
	}

	public void setTempSearchResults(Collection<Taxon> tempSearchResults) {
		this.tempSearchResults = tempSearchResults;
	}
	
	public void addToTempSearchResults(Taxon t) {
		this.tempSearchResults.add(t);
	}
	
	public void addAllToTempSearchResults(Collection<Taxon> ts) {
		this.tempSearchResults.addAll(ts);
	}

	public TaxonSearchCommand(Collection<Taxon> tempSearchResults) {
		super();
		this.tempSearchResults = tempSearchResults;
	}

	public TaxonSearchCommand() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TaxonSearchCommand(HttpServletRequest request) {
		super(request);
	}

	public TaxonSearchCommand(SearchCommand searchCommand) {
		super(searchCommand.getResults());
		if (searchCommand.getResultType() != SearchResultsType.TAXON) {
			this.setResults(this.getResults().convertToTaxa());
		}
	}

	private void setResultsType(SearchResultsType taxon) {
		// TODO Auto-generated method stub
		
	}
	
	
}
