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

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.RangeExpression.MalformedRangeExpression;
import org.cipres.treebase.domain.search.SearchResults;
import org.cipres.treebase.domain.search.SearchResultsType;
import org.cipres.treebase.domain.search.TreeSearchResults;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.RequestMessageSetter;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.z3950.zing.cql.CQLAndNode;
import org.z3950.zing.cql.CQLBooleanNode;
import org.z3950.zing.cql.CQLNode;
import org.z3950.zing.cql.CQLNotNode;
import org.z3950.zing.cql.CQLOrNode;
import org.z3950.zing.cql.CQLParseException;
import org.z3950.zing.cql.CQLParser;
import org.z3950.zing.cql.CQLRelation;
import org.z3950.zing.cql.CQLTermNode;

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
		String query = request.getParameter("query");
		
		LOGGER.info("formName is '" + formName + "'");
		
		if ( ! TreebaseUtil.isEmpty(query) ) {
			/*
			CQLParser parser = new CQLParser();
			CQLNode root = parser.parse(query);
			root = normalizeParseTree(root);
			Set<PhyloTree> queryResults = doCQLQuery(root, new HashSet<PhyloTree>(),request, response, errors);
			TreeSearchResults tsr = new TreeSearchResults(queryResults);
			saveSearchResults(request, tsr);
			if ( TreebaseUtil.isEmpty(request.getParameter("format")) || ! request.getParameter("format").equals("rss1") ) {			
				return new ModelAndView("search/treeSearch", Constants.RESULT_SET, tsr);
			}
			else {
				return this.searchResultsAsRDF(tsr, request, root);
			}
			*/
			return this.handleQueryRequest(request, response, errors);
		}
		
		if (formName.equals("treeSimple")) {
			String buttonName = request.getParameter("searchButton");
			String searchTerm = convertStars(request.getParameter("searchTerm"));
			Collection<PhyloTree> matches = null;
			TreeSearchResults oldRes;	
			{
				SearchResults<?> sr = searchResults(request);
				if (sr != null) {
					oldRes = (TreeSearchResults) sr.convertToTrees();
				} else {
					oldRes = new TreeSearchResults ();   // TODO: Convert existing search results to new type	
				}
			}			
			if (buttonName.equals("treeID")) {
				matches = doSearch(request, response, SearchType.byID, errors, searchTerm);
			} else if  (buttonName.equals("treeTitle")) {
				matches =  doSearch(request, response, SearchType.byTitle, errors, searchTerm);
			} else if  (buttonName.equals("treeType")) {
				matches = doSearch(request, response, SearchType.byType, errors, searchTerm);
			} else if  (buttonName.equals("treeKind")) {
				matches = doSearch(request, response, SearchType.byKind, errors, searchTerm);
			} else if  (buttonName.equals("treeQuality")) {
				matches = doSearch(request, response, SearchType.byQuality, errors, searchTerm);
			} else if  (buttonName.equals("treeNTAX")) {
				matches = doSearch(request, response, SearchType.byNTAX, errors, searchTerm);
			} else {
				throw new Error("Unknown search button name '" + buttonName + "'");
			}
			SearchResults<PhyloTree> newRes = intersectSearchResults(oldRes, new TreeSearchResults(matches), 
			new RequestMessageSetter(request), "No matching trees found");	
			saveSearchResults(request, newRes);	
			if ( TreebaseUtil.isEmpty(request.getParameter("format")) || ! request.getParameter("format").equals("rss1") ) {			
				return new ModelAndView("search/treeSearch", Constants.RESULT_SET, newRes); 			
			}
			else {
				return this.searchResultsAsRDF(newRes, request, null,"tree","tree");
			}
		} else { 
			return super.onSubmit(request, response, command, errors);
		}
	}

	private Set<PhyloTree> doCQLQuery(
			CQLNode node, 
			Set<PhyloTree> results, 
			HttpServletRequest request, 
			HttpServletResponse response, 
			BindException errors
		) throws InstantiationException {
		if ( node instanceof CQLBooleanNode ) {
			Set<PhyloTree> resultsLeft = doCQLQuery(((CQLBooleanNode)node).left,results, request, response, errors);
			Set<PhyloTree> resultsRight = doCQLQuery(((CQLBooleanNode)node).right,results, request, response, errors);
			if ( node instanceof CQLNotNode ) {
				for ( PhyloTree rightTree : resultsRight ) {
					if ( resultsLeft.contains(rightTree) )
						resultsLeft.remove(rightTree);
				}
			}
			else if ( node instanceof CQLOrNode ) {
				resultsLeft.addAll(resultsRight);
			}
			else if ( node instanceof CQLAndNode ) {
				Set<PhyloTree> resultsUnion = new HashSet<PhyloTree>();
				for ( PhyloTree leftTree : resultsLeft ) {
					if ( resultsRight.contains(leftTree) )
						resultsUnion.add(leftTree);
				}				
				resultsLeft = resultsUnion;
			}
			results = resultsLeft;
		}		
		else if ( node instanceof CQLTermNode ) {
			CQLTermNode term = (CQLTermNode)node;
			String index = term.getIndex();
			if ( index.startsWith("tb.title") ) {
				results.addAll(doSearch(request, response, SearchType.byTitle, errors, term.getTerm()));
			} else if ( index.startsWith("tb.identifier") ) {
				results.addAll(doSearch(request, response, SearchType.byID, errors, term.getTerm()));
			} else if ( index.startsWith("tb.type") ) {
				results.addAll(doSearch(request, response, SearchType.byType, errors, term.getTerm()));
			} else if ( index.startsWith("tb.kind") ) {
				results.addAll(doSearch(request, response, SearchType.byKind, errors, term.getTerm()));
			} else if ( index.startsWith("tb.quality") ) {
				results.addAll(doSearch(request, response, SearchType.byQuality, errors, term.getTerm()));
			} else if ( index.startsWith("tb.ntax") ) {
				results.addAll(doSearch(request, response, SearchType.byNTAX, errors, term.getTerm()));
			} else {
				// issue warnings
				addMessage(request, "Unsupported index: " + index);
			}
		}
		logger.debug(node);
		return results;		
	}	
	
	private CQLNode normalizeParseTree(CQLNode node) {
		if ( node instanceof CQLBooleanNode ) {
			((CQLBooleanNode)node).left = normalizeParseTree(((CQLBooleanNode)node).left);
			((CQLBooleanNode)node).right = normalizeParseTree(((CQLBooleanNode)node).right);
			return node;
		}
		else if ( node instanceof CQLTermNode ) {
			String index = ((CQLTermNode)node).getIndex();
			String term = ((CQLTermNode)node).getTerm();
			CQLRelation relation = ((CQLTermNode)node).getRelation();
			index = index.replaceAll("dcterms.title", "tb.title.tree");
			index = index.replaceAll("dcterms.identifier", "tb.identifier.tree");
			index = index.replaceAll("dcterms.extent", "tb.ntax.tree");
			return new CQLTermNode(index,relation,term);
		}
		logger.debug(node);
		return node;
	}	

	@SuppressWarnings("unchecked")
	private Collection<PhyloTree> doSearch(
			HttpServletRequest request,
			HttpServletResponse response,
			SearchType searchType,
			BindException errors,
			String searchTerm) throws InstantiationException {

//		String searchTerm = convertStars(request.getParameter("searchTerm"));
//		String keywordSearchTerm = "%" + searchTerm + "%";
//		TreeSearchResults oldRes;	
//		{
//			SearchResults<?> sr = searchResults(request);
//			if (sr != null) {
//				oldRes = (TreeSearchResults) sr.convertToTrees();
//			} else {
//				oldRes = new TreeSearchResults ();   // TODO: Convert existing search results to new type	
//			}
//		}

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
	
		return matches;
//		SearchResults<PhyloTree> newRes = intersectSearchResults(oldRes, new TreeSearchResults(matches), 
//				new RequestMessageSetter(request), "No matching trees found");
//		
//		saveSearchResults(request, newRes);
//		
//		return new ModelAndView("search/treeSearch", Constants.RESULT_SET, newRes); 

	}

	SearchResultsType currentSearchType() {
		return SearchResultsType.TREE;
	}
	
	@Override
	public String getDefaultViewURL() {
		return "treeSearch.html";
	}

	@Override
	protected ModelAndView handleQueryRequest(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws CQLParseException, IOException, InstantiationException {
		String query = request.getParameter("query");
		CQLParser parser = new CQLParser();
		CQLNode root = parser.parse(query);
		root = normalizeParseTree(root);
		Set<PhyloTree> queryResults = doCQLQuery(root, new HashSet<PhyloTree>(),request, response, errors);
		TreeSearchResults tsr = new TreeSearchResults(queryResults);
		saveSearchResults(request, tsr);
		if ( TreebaseUtil.isEmpty(request.getParameter("format")) || ! request.getParameter("format").equals("rss1") ) {			
			return new ModelAndView("search/treeSearch", Constants.RESULT_SET, tsr);
		}
		else {
			SearchResults<?> res = tsr;
			String schema = null;
			if ( ! TreebaseUtil.isEmpty(request.getParameter("recordSchema")) ) {
				schema = request.getParameter("recordSchema");
				if ( schema.equals("matrix") ) {
					res = tsr.convertToMatrices();
				}
				else if ( schema.equals("taxon") ) {
					res = tsr.convertToTaxa();
				}
				else if ( schema.equals("study") ) {
					res = tsr.convertToStudies();
				}
			}
			this.saveSearchResults(request, res);
			return this.searchResultsAsRDF(res, request, root,schema,"tree");
		}
	}

}
