/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ 
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
