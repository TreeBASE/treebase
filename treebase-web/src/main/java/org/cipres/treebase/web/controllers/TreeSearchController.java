/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.cipres.treebase.RangeExpression.MalformedRangeExpression;
import org.cipres.treebase.domain.search.SearchResults;
import org.cipres.treebase.domain.search.SearchResultsType;
import org.cipres.treebase.domain.search.StudySearchResults;
import org.cipres.treebase.domain.search.TreeSearchResults;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.RequestMessageSetter;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * TreeSearchController.java
 * 
 * Created on March 13, 2008
 * 
 * @author mjd
 * 
 */
public class TreeSearchController extends SearchController {
	/**
	 * Logger for this class
	 */
	static final Logger LOGGER = Logger.getLogger(TreeSearchController.class);

	private enum SearchType {
		byID,
		byTitle,
		byType,
		byKind,
		byQuality,
		byNTAX
	}
	
	protected ModelAndView onSubmit(
			HttpServletRequest request,
			HttpServletResponse response,
			Object command,
			BindException errors) throws Exception {

		LOGGER.info("in TreeSearchController.onSubmit");

		clearMessages(request);
		String formName = request.getParameter("formName");
		
		LOGGER.info("formName is '" + formName + "'");

		
		if (formName.equals("treeSimple")) {
			String buttonName = request.getParameter("searchButton");

			if (buttonName.equals("treeID")) {
				return doSearch(request, response, SearchType.byID, errors);
			} else if  (buttonName.equals("treeTitle")) {
				return doSearch(request, response, SearchType.byTitle, errors);
			} else if  (buttonName.equals("treeType")) {
				return doSearch(request, response, SearchType.byType, errors);
			} else if  (buttonName.equals("treeKind")) {
				return doSearch(request, response, SearchType.byKind, errors);
			} else if  (buttonName.equals("treeQuality")) {
				return doSearch(request, response, SearchType.byQuality, errors);
			} else if  (buttonName.equals("treeNTAX")) {
				return doSearch(request, response, SearchType.byNTAX, errors);
			} else {
				throw new Error("Unknown search button name '" + buttonName + "'");
			}
		} else { 
			return super.onSubmit(request, response, command, errors);
		}
	}

	private ModelAndView doSearch(
			HttpServletRequest request,
			HttpServletResponse response,
			SearchType searchType,
			BindException errors) throws InstantiationException {

		String searchTerm = convertStars(request.getParameter("searchTerm"));
		String keywordSearchTerm = "%" + searchTerm + "%";
		TreeSearchResults oldRes;	
		{
			SearchResults<?> sr = searchResults(request);
			if (sr != null) {
				oldRes = (TreeSearchResults) sr.convertToTrees();
			} else {
				oldRes = new TreeSearchResults ();   // TODO: Convert existing search results to new type	
			}
		}

		Collection<PhyloTree> matches = null;
		PhyloTreeService phyloTreeService = getSearchService().getPhyloTreeService();	


		switch(searchType) {
		case byID:
			matches = (Collection<PhyloTree>) doSearchByIDString(request, phyloTreeService, PhyloTree.class, searchTerm);
			break;

		case byKind:
			matches = phyloTreeService
	  		.findSomethingByItsDescription(PhyloTree.class, "treeKind", searchTerm, true);
			break;
			
		case byQuality:
			matches = phyloTreeService
	  		.findSomethingByItsDescription(PhyloTree.class, "quality", searchTerm, true);
			break;

		case byTitle:
			matches = phyloTreeService
	  		.findSomethingBySubstring(PhyloTree.class, "title", searchTerm);
			break;

		case byType:
			matches = phyloTreeService
	  		.findSomethingByItsDescription(PhyloTree.class, "treeType", searchTerm, true);
			break;

		case byNTAX:
			try {
					matches = phyloTreeService
					.findSomethingByRangeExpression(PhyloTree.class, "nTax", searchTerm);
				} catch (MalformedRangeExpression e) {
					addMessage(request, "Malformed range expression: " + e.getMessage());
				}
		}
	
		SearchResults<PhyloTree> newRes = intersectSearchResults(oldRes, new TreeSearchResults(matches), 
				new RequestMessageSetter(request), "No matching trees found");
		
		saveSearchResults(request, newRes);
		
		return new ModelAndView("search/treeSearch", Constants.RESULT_SET, newRes); 

	}

	SearchResultsType currentSearchType() {
		return SearchResultsType.TREE;
	}
	
	@Override
	public String getDefaultViewURL() {
		return "treeSearch.html";
	}

}
