package org.cipres.treebase.web.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.search.MatrixSearchResults;
import org.cipres.treebase.domain.search.SearchResultsType;
import org.cipres.treebase.domain.search.StudySearchResults;
import org.cipres.treebase.domain.search.TaxonSearchResults;
import org.cipres.treebase.domain.search.TreeSearchResults;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.z3950.zing.cql.CQLNode;
import org.z3950.zing.cql.CQLParseException;
import org.z3950.zing.cql.CQLParser;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.web.controllers.TreeSearchController;
import org.cipres.treebase.web.controllers.TaxonSearchController;
import org.cipres.treebase.web.controllers.StudySearchController;
import org.cipres.treebase.web.controllers.MatrixSearchController;

public class MainSearchController extends SearchController {

	@Override
	protected ModelAndView handleQueryRequest(HttpServletRequest request,
			HttpServletResponse response, BindException errors, String query)
			throws CQLParseException, IOException, InstantiationException {
		String anyQuery = "dcterms.identifier=" + query + " or dcterms.title=" + query;
		CQLParser parser = new CQLParser();
		CQLNode root = parser.parse(anyQuery);	
		
		StudySearchResults studyResults = new StudySearchResults(doDelegatedStudyQuery(root, request, response, errors));
		request.setAttribute("studyResults", studyResults);
		
		MatrixSearchResults matrixResults = new MatrixSearchResults(doDelegatedMatrixQuery(root, request, response, errors));
		request.setAttribute("matrixResults", matrixResults);
		
		TaxonSearchResults taxonResults = new TaxonSearchResults(doDelegatedTaxonQuery(root, request, response, errors));
		request.setAttribute("taxonResults", taxonResults);
		
		TreeSearchResults treeResults = new TreeSearchResults(doDelegatedTreeQuery(root, request, response, errors));
		request.setAttribute("treeResults",treeResults);
		
		return new ModelAndView("search/mainSearch");
	}
	
	protected ModelAndView onSubmit(
			HttpServletRequest request,
			HttpServletResponse response,
			Object command,
			BindException errors) throws Exception {
		clearMessages(request);
		String query = request.getParameter("query");		
		if ( TreebaseUtil.isEmpty(query) || "".equals(query) ) {
			addMessage(request,"You provided an empty search query");
		}
		return this.handleQueryRequest(request, response, errors, query);
	}
	
	private Set<Study> doDelegatedStudyQuery(CQLNode node, HttpServletRequest request, HttpServletResponse response, BindException errors) throws InstantiationException {
		StudySearchController searcher = new StudySearchController();
		CQLNode normalized = searcher.normalizeParseTree(node);
		Set<Study> results = new HashSet<Study>();
		return searcher.doCQLQuery(normalized, results, request, response, errors);
	}
	
	private Set<Matrix> doDelegatedMatrixQuery(CQLNode node, HttpServletRequest request, HttpServletResponse response, BindException errors) throws InstantiationException {
		MatrixSearchController searcher = new MatrixSearchController();
		CQLNode normalized = searcher.normalizeParseTree(node);
		Set<Matrix> results = new HashSet<Matrix>();
		return searcher.doCQLQuery(normalized, results, request, response, errors);
	}	
	
	private Set<PhyloTree> doDelegatedTreeQuery(CQLNode node, HttpServletRequest request, HttpServletResponse response, BindException errors) throws InstantiationException {
		TreeSearchController searcher = new TreeSearchController();
		CQLNode normalized = searcher.normalizeParseTree(node);
		Set<PhyloTree> results = new HashSet<PhyloTree>();
		return searcher.doCQLQuery(normalized, results, request, response, errors);
	}	

	private Set<Taxon> doDelegatedTaxonQuery(CQLNode node, HttpServletRequest request, HttpServletResponse response, BindException errors) throws InstantiationException {
		TaxonSearchController searcher = new TaxonSearchController();
		CQLNode normalized = searcher.normalizeParseTree(node);
		Set<Taxon> results = new HashSet<Taxon>();
		return searcher.doCQLQuery(normalized, results, request);
	}	
	
	@Override
	protected Map<String, String> getPredicateMapping() {
		return new HashMap<String,String>();
	}

	@Override
	public String getDefaultViewURL() {
		return "mainSearch.html";
	}

	@Override
	SearchResultsType currentSearchType() {
		return SearchResultsType.MAIN;
	}

}
