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
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.search.SearchResults;
import org.cipres.treebase.domain.search.SearchResultsType;
import org.cipres.treebase.domain.search.TreeSearchResults;
import org.cipres.treebase.domain.taxon.Taxon;
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
import org.z3950.zing.cql.CQLTermNode;

public class ClassificationSearchController extends SearchController {
	/**
	 * Logger for this class
	 */
	static final Logger LOGGER = Logger.getLogger(ClassificationSearchController.class);
	
	@SuppressWarnings("unchecked")
	private Collection<PhyloTree> doSearch(
			String searchTerm
		) {

		Collection<PhyloTree> matches = null;
		PhyloTreeService phyloTreeService = getSearchService().getPhyloTreeService();	
		matches = phyloTreeService.findTreesByNCBINodes(searchTerm);
		return matches;

	}
	
	@Override
	protected ModelAndView handleQueryRequest(HttpServletRequest request,
			HttpServletResponse response, BindException errors, String query)
			throws CQLParseException, IOException, InstantiationException {
		//
		CQLParser parser = new CQLParser();
		CQLNode root = parser.parse(query);
		root = normalizeParseTree(root);
		HashSet<PhyloTree> queryResults = doCQLQuery(root, new HashSet<PhyloTree>(),request);
		TreeSearchResults tsr = new TreeSearchResults(queryResults);
		saveSearchResults(request, tsr);
		
		if ( TreebaseUtil.isEmpty(request.getParameter("format")) || ! request.getParameter("format").equals("rss1") ) {			
			return new ModelAndView("search/classificationSearch", Constants.RESULT_SET, tsr);
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
	
	protected HashSet<PhyloTree> doCQLQuery(CQLNode node, HashSet<PhyloTree> results, HttpServletRequest request) {
		if ( node instanceof CQLBooleanNode ) {
			HashSet<PhyloTree> resultsLeft = doCQLQuery(((CQLBooleanNode)node).left,results, request);
			HashSet<PhyloTree> resultsRight = doCQLQuery(((CQLBooleanNode)node).right,results, request);
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
				HashSet<PhyloTree> resultsUnion = new HashSet<PhyloTree>();
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
				if ( index.startsWith("tb.identifier") ) {
					if ( index.endsWith("ncbi") ) {
						results.addAll(doSearch(term.getTerm()));
					}
					else {
						// issue warnings
						addMessage(request, "Unsupported index: " + index);
					}
				} else {
					// issue warnings
					addMessage(request, "Unsupported index: " + index);
				}
			}
		logger.debug(node);
		return results;	
	}
		
	@Override
	public String getDefaultViewURL() {
		return "classificationSearch.html";
	}
	
	SearchResultsType currentSearchType() {
		return SearchResultsType.TREE;
	}


	@Override
	protected Map<String, String> getPredicateMapping() {
		Map<String,String> mapping = new HashMap<String,String>();
		mapping.put("dcterms.title", "tb.title.taxon");
		mapping.put("dcterms.identifier", "tb.identifier.taxon");		
		return mapping;
	}

}
