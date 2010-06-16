package org.cipres.treebase.web.controllers;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.search.SearchResults;
import org.cipres.treebase.domain.search.SearchResultsFrozen;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.z3950.zing.cql.CQLNode;
import org.z3950.zing.cql.CQLParser;

/**
 * @author rvosa
 *
 */
public class SearchResultsAsRDFController implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SearchResultsFrozen frozenResults = (SearchResultsFrozen) request.getSession().getAttribute("searchResults");
		if ( null != frozenResults ) {
			String phyloWSPath = request.getSession().getAttribute("originalSection") + "/find";
			SearchResults<?> searchResults = frozenResults.thaw();
			request.getSession().setAttribute("searchResultsThawed", searchResults);			
			request.getSession().setAttribute("recordSchema", request.getSession().getAttribute("recordSchema"));
			request.getSession().setAttribute("format", "rss1");
			request.getSession().setAttribute("baseURL", TreebaseUtil.getPurlBase());
			request.getSession().setAttribute("phyloWSPath", phyloWSPath);
			if ( null != request.getParameter("query") ) {			
				String query = request.getParameter("query");						
				if ( ! TreebaseUtil.isEmpty(query) ) {
					CQLParser parser = new CQLParser();
					CQLNode root = parser.parse(query);			
					request.getSession().setAttribute("normalizedCQLQuery", URLEncoder.encode(root.toCQL(),"UTF-8"));
				}
			}
		}
		return new ModelAndView("searchResultsAsRDF");
	}

}
