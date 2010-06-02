
package org.cipres.treebase.web.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.RangeExpression.MalformedRangeExpression;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.search.MatrixSearchResults;
import org.cipres.treebase.domain.search.SearchResults;
import org.cipres.treebase.domain.search.SearchResultsType;
import org.cipres.treebase.domain.search.TreeSearchResults;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.RequestMessageSetter;
import org.cipres.treebase.web.util.SearchMessageSetter;
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
 * MatrixSearchController.java
 * 
 * Created on July 16, 2008
 * 
 * @author mjd
 * 
 */
public class MatrixSearchController extends SearchController {
	/**
	 * Logger for this class
	 */
	static final Logger LOGGER = Logger.getLogger(MatrixSearchController.class);

	private enum SearchType {
		byID,
		byTitle,
		byType,
		byNTAX,
		byNCHAR,
	}
	
	protected ModelAndView onSubmit(
			HttpServletRequest request,
			HttpServletResponse response,
			Object command,
			BindException errors) throws Exception {

		LOGGER.info("in matrixSearchController.onSubmit");

		clearMessages(request);
		String formName = request.getParameter("formName");
		String query = request.getParameter("query");
		
		LOGGER.info("formName is '" + formName + "'");
		
		if ( ! TreebaseUtil.isEmpty(query) ) {
			return this.handleQueryRequest(request, response, errors, query);
		}		
		
		if (formName.equals("matrixSimple")) {
			String buttonName = request.getParameter("searchButton");
			Set<Matrix> matches = new HashSet<Matrix>();
			String searchTerm = convertStars(request.getParameter("searchTerm"));
		 	SearchMessageSetter mSetter = new RequestMessageSetter(request);
			MatrixSearchResults oldRes;	
			{
				SearchResults<?> sr = searchResults(request);
				if (sr != null) {
					oldRes = (MatrixSearchResults) sr.convertToMatrices();
				} else {
					oldRes = new MatrixSearchResults ();   // TODO: Convert existing search results to new type	
				}
			}			
			if (buttonName.equals("matrixID")) {
				matches.addAll(doSearch(request, response, SearchType.byID, errors,searchTerm));
			} else if  (buttonName.equals("matrixTitle")) {
				matches.addAll(doSearch(request, response, SearchType.byTitle, errors,searchTerm));
			} else if  (buttonName.equals("matrixType")) {
				matches.addAll(doSearch(request, response, SearchType.byType, errors,searchTerm));
			} else if  (buttonName.equals("matrixNTAX")) {
				matches.addAll(doSearch(request, response, SearchType.byNTAX, errors,searchTerm));	
			} else if  (buttonName.equals("matrixNCHAR")) {
				matches.addAll(doSearch(request, response, SearchType.byNCHAR, errors,searchTerm));
			} else {
				throw new Error("Unknown search button name '" + buttonName + "'");
			}
			if ( TreebaseUtil.isEmpty(request.getParameter("format")) || ! request.getParameter("format").equals("rss1") ) {
				SearchResults<Matrix> newRes = intersectSearchResults(oldRes, 
						new MatrixSearchResults(matches), mSetter, "No matching matrices found");
				saveSearchResults(request, newRes);	
				return new ModelAndView("search/matrixSearch", Constants.RESULT_SET, newRes); 
			}
			else {
				return this.searchResultsAsRDF(new MatrixSearchResults(matches), request, null, "matrix", "matrix");
			}			
		} 
		
		else { 
			return super.onSubmit(request, response, command, errors);
		}
	}
	
	private Set<Matrix> doCQLQuery(
			CQLNode node, 
			Set<Matrix> results, 
			HttpServletRequest request, 
			HttpServletResponse response, 
			BindException errors
		) throws InstantiationException {
		if ( node instanceof CQLBooleanNode ) {
			Set<Matrix> resultsLeft = doCQLQuery(((CQLBooleanNode)node).left,results, request, response, errors);
			Set<Matrix> resultsRight = doCQLQuery(((CQLBooleanNode)node).right,results, request, response, errors);
			if ( node instanceof CQLNotNode ) {
				for ( Matrix rightMatrix : resultsRight ) {
					if ( resultsLeft.contains(rightMatrix) )
						resultsLeft.remove(rightMatrix);
				}
			}
			else if ( node instanceof CQLOrNode ) {
				resultsLeft.addAll(resultsRight);
			}
			else if ( node instanceof CQLAndNode ) {
				Set<Matrix> resultsUnion = new HashSet<Matrix>();
				for ( Matrix leftMatrix : resultsLeft ) {
					if ( resultsRight.contains(leftMatrix) )
						resultsUnion.add(leftMatrix);
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
			} else if ( index.startsWith("tb.ntax") ) {
				results.addAll(doSearch(request, response, SearchType.byNTAX, errors, term.getTerm()));
			} else if ( index.startsWith("tb.nchar") ) {
				results.addAll(doSearch(request, response, SearchType.byNTAX, errors, term.getTerm()));
			} else {
				// issue warnings
				addMessage(request, "Unsupported index: " + index);
			}	
		}
		logger.debug(node);
		return results;		
	}
	
	@SuppressWarnings("unchecked")
	private Collection<Matrix> doSearch(
			HttpServletRequest request,
			HttpServletResponse response,
			SearchType searchType,
			BindException errors,
			String searchTerm) throws InstantiationException {

		Collection<Matrix> matches = null;
		MatrixService matrixService = getSearchService().getMatrixService();	

		switch(searchType) {
		case byID:
			matches = (Collection<Matrix>) doSearchByIDString(request, matrixService, Matrix.class, searchTerm);
			break;

		case byTitle:
			matches = matrixService
	  		.findSomethingBySubstring(Matrix.class, "title", searchTerm);
			break;

		case byType:
			matches = matrixService
	  		.findSomethingByItsDescription(Matrix.class, "matrixKind", searchTerm, false);
			break;

		case byNCHAR:
			try {
				matches = matrixService
				.findSomethingByRangeExpression(CharacterMatrix.class, "nChar", searchTerm);
			} catch (MalformedRangeExpression e) {
				addMessage(request, "Malformed range expression: " + e.getMessage());
			}
			break;


		case byNTAX:
			try {
				matches = matrixService
				.findSomethingByRangeExpression(CharacterMatrix.class, "nTax", searchTerm);
			} catch (MalformedRangeExpression e) {
				addMessage(request, "Malformed range expression: " + e.getMessage());
			}
			break;

		}
		
		// XXX need to filter out orphaned matrices
		Collection<Matrix> orphanedMatrices = new HashSet<Matrix>();
		for ( Matrix m : matches ) {
			if ( m.getStudy() == null ) {
				orphanedMatrices.add(m);
			}
		}
		matches.removeAll(orphanedMatrices);
		return matches;

	}

	SearchResultsType currentSearchType() {
		return SearchResultsType.MATRIX;
	}

	@Override
	public String getDefaultViewURL() {
		return "matrixSearch.html";
	}

	@Override
	protected ModelAndView handleQueryRequest(HttpServletRequest request,HttpServletResponse response,BindException errors, String query) throws CQLParseException, IOException, InstantiationException {
		//String query = request.getParameter("query");				
		CQLParser parser = new CQLParser();
		CQLNode root = parser.parse(query);
		root = normalizeParseTree(root);
		Set<Matrix> queryResults = doCQLQuery(root, new HashSet<Matrix>(),request, response, errors);
		MatrixSearchResults tsr = new MatrixSearchResults(queryResults);
		saveSearchResults(request, tsr);
		if ( TreebaseUtil.isEmpty(request.getParameter("format")) || ! request.getParameter("format").equals("rss1") ) {
			return new ModelAndView("search/matrixSearch", Constants.RESULT_SET, tsr); 			
		}
		else {
			SearchResults<?> res = tsr;
			String schema = null, original = "matrix";
			if ( ! TreebaseUtil.isEmpty(request.getParameter("recordSchema")) ) {
				schema = request.getParameter("recordSchema");
				if ( schema.equals("tree") ) {
					res = tsr.convertToTrees();
				}
				else if ( schema.equals("taxon") ) {
					res = tsr.convertToTaxa();
				}
				else if ( schema.equals("study") ) {
					res = tsr.convertToStudies();
				}
			}
			this.saveSearchResults(request, res);
			return this.searchResultsAsRDF(res, request, root, schema, original);
		}
	}

	@Override
	protected Map<String, String> getPredicateMapping() {
		Map<String,String> mapping = new HashMap<String,String>();
		mapping.put("dcterms.title", "tb.title.matrix");
		mapping.put("dcterms.identifier", "tb.identifier.matrix");
		mapping.put("dcterms.extent", "tb.ntax.matrix");
		return mapping;
	}

}
