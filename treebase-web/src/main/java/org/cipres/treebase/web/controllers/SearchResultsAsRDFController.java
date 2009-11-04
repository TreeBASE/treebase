package org.cipres.treebase.web.controllers;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.NamespacedGUID;
import org.cipres.treebase.PhyloWSPath;
import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.search.SearchResults;
import org.cipres.treebase.domain.search.SearchResultsFrozen;
import org.cipres.treebase.domain.search.SearchResultsType;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.tree.PhyloTree;
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
//			SearchResultsType searchResultsType = searchResults.getResultType();
//			switch(searchResultsType) {
//				case NONE   : phyloWSPath = ""; break;
//				case STUDY  : phyloWSPath = "study/find"; break;
//				case MATRIX : phyloWSPath = "matrix/find"; break;
//				case TREE   : phyloWSPath = "tree/find"; break;
//				case TAXON  : phyloWSPath = "taxon/find"; break;
//			}
			
			StringBuffer domainAddress = new StringBuffer("http://");
			domainAddress
				.append(request.getServerName())
				.append(':')
				.append(request.getServerPort());
			StringBuffer baseURL = new StringBuffer(domainAddress);
			baseURL.append("/treebase-web/phylows");
			request.getSession().setAttribute("recordSchema", request.getSession().getAttribute("recordSchema"));
			request.getSession().setAttribute("format", "rss1");
			request.getSession().setAttribute("baseURL", baseURL.toString());
			request.getSession().setAttribute("domainAddress", domainAddress.toString());
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

		
		/*
		
		NamespacedGUID namespacedGUID = new NamespacedGUID(request.getParameter("namespacedGUID"));
		TreebaseIDString treebaseIDString = null;
		PhyloWSPath phyloWSPath = null;
		request.setAttribute("hasWebPage", false);
		request.setAttribute("hasNexus", false);
		request.setAttribute("hasNeXML", false);
		request.setAttribute("hasRdf", false);
		try {
			treebaseIDString = namespacedGUID.getTreebaseIDString();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		if ( null != treebaseIDString ) {
			if ( null != treebaseIDString.getTypePrefix() ) {
				Class<?> theClass = TreebaseIDString.getClassForPrefix(treebaseIDString.getTypePrefix());
				if ( null != theClass ) {
					if ( theClass.isAssignableFrom(PhyloTree.class) 
							|| theClass.isAssignableFrom(Matrix.class) 
							|| theClass.isAssignableFrom(Study.class) ) {
						request.setAttribute("hasWebPage", true);
						request.setAttribute("hasNexus", true);
						request.setAttribute("hasNeXML", true);
						request.setAttribute("hasRdf", true);
					}
					phyloWSPath = new PhyloWSPath(theClass.getPackage(),namespacedGUID);
				}
			}
		}
		request.getSession().setAttribute("namespacedGUID", namespacedGUID.toString());
		// <c:set var="baseURL" value="http://localhost:8080/treebase-web/PhyloWS"/>
		StringBuffer domainAddress = new StringBuffer("http://");
		domainAddress
			.append(request.getServerName())
			.append(':')
			.append(request.getServerPort());
		StringBuffer baseURL = new StringBuffer(domainAddress);
		baseURL.append("/treebase-web/phylows");
		request.getSession().setAttribute("baseURL", baseURL.toString());
		request.getSession().setAttribute("domainAddress", domainAddress.toString());
		request.getSession().setAttribute("phyloWSPath", phyloWSPath);
		return new ModelAndView("anyObjectAsRDF");
		*/
	}

}
