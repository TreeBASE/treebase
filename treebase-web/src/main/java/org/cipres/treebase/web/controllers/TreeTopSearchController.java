
package org.cipres.treebase.web.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cipres.treebase.domain.search.SearchResults;
import org.cipres.treebase.domain.search.SearchResultsType;
import org.cipres.treebase.domain.search.TreeSearchResults;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.web.Constants;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.z3950.zing.cql.CQLNode;
import org.z3950.zing.cql.CQLParseException;

/**
 * TreeTopSearchController.java
 * 
 * Created on March 13, 2008
 * 
 * @author mjd
 * 
 */
public class TreeTopSearchController extends SearchController {
	/**
	 * Logger for this class
	 */
	static final Logger LOGGER = LogManager.getLogger(TreeTopSearchController.class);

	private enum SearchType {
		topology3Search,
		topology4sSearch,
		topology4aSearch
	}
	
	protected ModelAndView onSubmit(
			HttpServletRequest request,
			HttpServletResponse response,
			Object command,
			BindException errors) throws Exception {

		LOGGER.info("in TreeTopSearchController.onSubmit");

		clearMessages(request);
		String formName = request.getParameter("formName");
		
		LOGGER.info("formName is '" + formName + "'");

		
		if (formName.equals("topology3")) {
			return doSearch(request, response, SearchType.topology3Search, errors);
		} else if (formName.equals("topology4a")) {
			return doSearch(request, response, SearchType.topology4aSearch, errors);
		} else if (formName.equals("topology4s")) {
			return doSearch(request, response, SearchType.topology4sSearch, errors);
		} else if ( formName.equals("resultsAction") ) {
			String action = request.getParameter("action");
			if (action.equals("discardResults")) {			
				discardSearchResults(request);
				return new ModelAndView("search/treeTopSearch", Constants.RESULT_SET, searchResults(request));
			} else if ( action.equals("refineSearch") ){
				onSubmitRefineSearch(request,response,command,errors);	
				return new ModelAndView("search/treeTopSearch", Constants.RESULT_SET, searchResults(request));				
			} else {
				throw new Error("Unknown action '" + action + "'");
			}			
		} else {
			return super.onSubmit(request, response, command, errors);
		}
	}
	
	protected ModelAndView doSearch(
			HttpServletRequest request,
			HttpServletResponse response,
			SearchType searchType,
			BindException errors) throws Exception {
		
		String[] taxon = new String[] {
				request.getParameter("taxon_a"),
				request.getParameter("taxon_b"),
				request.getParameter("taxon_c"),
				request.getParameter("taxon_d")
		};
		
		for (int i=0; i<taxon.length; i++) {
			if (taxon[i] != null) {
				taxon[i] = convertStars(taxon[i]);
			}
			LOGGER.debug("input " + i + " = '" + taxon[i] + "'");
		}

		TreeSearchResults oldRes;	

		{
			SearchResults<?> sr = (TreeSearchResults) searchResults(request);
			if (sr != null) {
				oldRes = (TreeSearchResults) sr; // (TreeSearchResults) sr.convertToTrees();
			} else {
				oldRes = new TreeSearchResults ();   // TODO: Convert existing search results to new type	
			}
		}
		
		LOGGER.info("doSearch old results contained " + oldRes.size() + " item(s)");
		Collection<PhyloTree> newRes;
		PhyloTreeService treeService = getSearchService().getPhyloTreeService();	
		
		TaxonVariant[] taxonVariant = new TaxonVariant[taxon.length];
		List<String> resolutionError = new LinkedList<String> ();
		for (int i=0; i<taxon.length; i++) {
			if (taxon[i] == null) continue;
			if (taxon[i].equals("")) 
				resolutionError.add("TaxonVariant name " + i + " missing");
			Set<TaxonVariant> tvSet = getTaxonLabelService().findTaxonVariantByName(taxon[i]);
			taxonVariant[i] = tvSet.isEmpty() ? null : tvSet.iterator().next(); // FIXME: This is not really the right behavior
			if (taxonVariant[i] == null)
				resolutionError.add("Couldn't resolve TaxonVariant '" + taxon[i] + "'");
			else 
				LOGGER.debug("input '" + taxon[i] + "' resolved to TV " + taxonVariant[i].getId() + " ('" + taxonVariant[i].getFullName() + "')");
		}
		if (! resolutionError.isEmpty()) {
			addMessages(request, resolutionError);
			return new ModelAndView("search/treeTopSearch", Constants.RESULT_SET, oldRes); 
		}
				
		switch (searchType) {
		case topology3Search:
			checkTaxa(taxonVariant, 3);
			newRes = treeService.findByTopology3(taxonVariant[0], taxonVariant[1], taxonVariant[2]);
			break;
		case topology4aSearch:
			checkTaxa(taxonVariant, 4);
			newRes = treeService.findByTopology4a(taxonVariant[0], taxonVariant[1], taxonVariant[2], taxonVariant[3]);
			break;
		case topology4sSearch:
			checkTaxa(taxonVariant, 4);
			newRes = treeService.findByTopology4s(taxonVariant[0], taxonVariant[1], taxonVariant[2], taxonVariant[3]);
			break;
		default:
			throw new Error ("Unknown search type '" + searchType + "'");
		}
		
		if (newRes != null) { LOGGER.info("doSearch '" + searchType + "' found " + newRes.size() + " result(s)"); }
		if (newRes == null || newRes.isEmpty()) {
			addMessage(request, "No matching trees found");
		} else {
			if (oldRes == null) { 
				oldRes = new TreeSearchResults(newRes);
			} else { oldRes.intersect(newRes); }
		}
		saveSearchResults(request, oldRes);
		LOGGER.info("doSearch '" + searchType + "' after intersection: " + oldRes.size() + " result(s)");

		return new ModelAndView("search/treeTopSearch", Constants.RESULT_SET, oldRes); 
	}

	
	/**
	 * Make sure the taxon array contains at least n items
	 * @param tv
	 * @param n 
	 * @author mjd
	 */
	private void checkTaxa(TaxonVariant[] tv, int n) {
		int j;
		for (j=0; j<n; j++) {
			if (tv[j] == null) {
				throw new Error("Missing taxon name #" + j);
			}
		}
	}
	
	SearchResultsType currentSearchType() {
		return SearchResultsType.TREE;
	}
	
	@Override
	public String getDefaultViewURL() {
		return "treeTopSearch.html";
	}

	@Override
	protected ModelAndView handleQueryRequest(HttpServletRequest request,
			HttpServletResponse response, BindException errors, String query)
			throws CQLParseException, IOException, InstantiationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CQLNode normalizeParseTree(CQLNode node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Map<String, String> getPredicateMapping() {
		// TODO Auto-generated method stub
		return null;
	}

}
