
package org.cipres.treebase.web.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.TreebaseIDString.MalformedTreebaseIDString;
import org.cipres.treebase.domain.search.SearchResults;
import org.cipres.treebase.domain.search.SearchResultsType;
import org.cipres.treebase.domain.search.StudySearchResults;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
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
 * StudySearchController.java
 * 
 * Created on Feb 14, 2007
 * 
 * @author mjd
 * 
 */
public class StudySearchController extends SearchController {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(StudySearchController.class);
	protected String mValidateTaxaView;
	
	public String getValidateTaxaView() {
		return mValidateTaxaView;
	}

	public void setValidateTaxaView(String validateTaxaView) {
		mValidateTaxaView = validateTaxaView;
	}

	/**
	 * 
	 * The API right now supports only a list of authors, so we have to update the list instead of
	 * individual authors
	 * 
	 * Delete: remove associate of person from citation, but the person is _not_ deleted from the
	 * database
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @return
	 */
	
	private enum SearchType {
		inAbstract,
		inCitation,
		byAuthorName,
		byID,
		byLegacyID,
		byTitle,
		byKeyword,
	}

	protected ModelAndView onSubmit(
			HttpServletRequest request,
			HttpServletResponse response,
			Object command,
			BindException errors) throws Exception {

		LOGGER.info("in StudySearchController.onSubmit");

		clearMessages(request);
		String formName = request.getParameter("formName");
		String query = request.getParameter("query");
		
		LOGGER.info("formName is '" + formName + "'");
		
		if ( ! TreebaseUtil.isEmpty(query) && ! query.equals("")) {
			LOGGER.info("query is '" + query + "'");
			return this.handleQueryRequest(request, response, errors);
		}		
		
		if (formName.equals("searchKeyword")) {
			SearchType searchType;
			String buttonName = request.getParameter("searchButton");
			String searchTerm = convertStars(request.getParameter("searchTerm"));
			StudySearchResults oldRes;
			{
				SearchResults<?> sr = searchResults(request);
				if (sr != null) {
					oldRes = (StudySearchResults) sr.convertToStudies();
				} else {
					oldRes = new StudySearchResults ();   // TODO: Convert existing search results to new type	
				}
			}			
			if (buttonName.equals("authorKeyword")) {
				searchType = SearchType.byAuthorName;
			} else if (buttonName.equals("studyID")) {
				searchType = SearchType.byID;
			} else if (buttonName.equals("legacyStudyID")) {
				searchType = SearchType.byLegacyID;
			} else if (buttonName.equals("titleKeyword")) {
				searchType = SearchType.byTitle;
			} else if (buttonName.equals("textKeyword")) {
				searchType = SearchType.byKeyword;
			} else if (buttonName.equals("citationKeyword")) {
				searchType = SearchType.inCitation;
			} else if (buttonName.equals("abstractKeyword")) {
				searchType = SearchType.inAbstract;
			} else {
				throw new Error("Unknown search button name '" + buttonName + "'");
			}
			Collection<Study> matches = doSearch(request, response, searchType, errors,searchTerm);	
			if ( TreebaseUtil.isEmpty(request.getParameter("format")) || ! request.getParameter("format").equals("rss1") ) {				
				SearchResults<Study> newRes = intersectSearchResults(oldRes, new StudySearchResults(matches), 
				new RequestMessageSetter(request), "No matching studies found");	
				saveSearchResults(request, newRes);
				return new ModelAndView("search/studySearch", Constants.RESULT_SET, newRes); 		
			}
			else {
				return this.searchResultsAsRDF(new StudySearchResults(matches), request, null,"study","study");
			}
		} else {
			return super.onSubmit(request, response, command, errors);
		}
	}
	
	private Set<Study> doCQLQuery(
			CQLNode node, 
			Set<Study> results, 
			HttpServletRequest request, 
			HttpServletResponse response, 
			BindException errors
		) throws InstantiationException {
		if ( node instanceof CQLBooleanNode ) {
			Set<Study> resultsLeft = doCQLQuery(((CQLBooleanNode)node).left,results, request, response, errors);
			Set<Study> resultsRight = doCQLQuery(((CQLBooleanNode)node).right,results, request, response, errors);
			if ( node instanceof CQLNotNode ) {
				Set<Study> resultsDifference = new HashSet<Study>();
				for ( Study leftStudy : resultsLeft ) {
					if ( ! resultsRight.contains(leftStudy) )
						resultsDifference.add(leftStudy);
				}
				resultsLeft = resultsDifference;
			}
			else if ( node instanceof CQLOrNode ) {
				resultsLeft.addAll(resultsRight);
			}
			else if ( node instanceof CQLAndNode ) {
				Set<Study> resultsUnion = new HashSet<Study>();
				for ( Study leftStudy : resultsLeft ) {
					if ( resultsRight.contains(leftStudy) )
						resultsUnion.add(leftStudy);
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
			} else if ( index.equals("tb.identifier.study") ) {
				results.addAll(doSearch(request, response, SearchType.byID, errors, term.getTerm()));
			} else if ( index.startsWith("dcterms.contributor") ) {
				results.addAll(doSearch(request, response, SearchType.byAuthorName, errors, term.getTerm()));
			} else if ( index.startsWith("dcterms.abstract") ) {
				results.addAll(doSearch(request, response, SearchType.inAbstract, errors, term.getTerm()));
			} else if ( index.startsWith("dcterms.subject") ) {
				results.addAll(doSearch(request, response, SearchType.byKeyword, errors, term.getTerm()));
			} else if ( index.startsWith("dcterms.bibliographicCitation") ) {
				results.addAll(doSearch(request, response, SearchType.inCitation, errors, term.getTerm()));				
			} else if ( index.equals("tb.identifier.study.tb1") ) {
				results.addAll(doSearch(request, response, SearchType.byLegacyID, errors, term.getTerm()));
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
			index = index.replaceAll("dcterms.title", "tb.title.study");
			index = index.replaceAll("dcterms.identifier", "tb.identifier.study");
			return new CQLTermNode(index,relation,term);
		}
		logger.debug(node);
		return node;
	}		
	
	@SuppressWarnings("unchecked")
	protected Collection<Study> doSearch(
			HttpServletRequest request,
			HttpServletResponse response,
			SearchType searchType,
			BindException errors,
			String searchTerm) throws InstantiationException {
		
//		String searchTerm = convertStars(request.getParameter("searchTerm"));
		String keywordSearchTerm = "%" + searchTerm + "%";
//		StudySearchResults oldRes;	
//
//		{
//			SearchResults<?> sr = searchResults(request);
//			if (sr != null) {
//				oldRes = (StudySearchResults) sr.convertToStudies();
//			} else {
//				oldRes = new StudySearchResults ();   // TODO: Convert existing search results to new type	
//			}
//		}
//		
//		LOGGER.info("doSearch old results contained " + oldRes.size() + " item(s)");
		Collection<Study> matches;
		StudyService studyService = getSearchService().getStudyService();	
				
		switch (searchType) {
			case byID:
				matches = (Collection<Study>) doSearchByIDString(request, studyService, Study.class, searchTerm);
				break;
			case byLegacyID:
			{
				TreebaseIDString legacyID = null;
				boolean malformed = false;
				try {
					legacyID = new TreebaseIDString(searchTerm, Study.class);
				} catch (MalformedTreebaseIDString e) {
					malformed = true;
				}
				if (malformed || legacyID.getTBClass() != Study.class) {
					addMessage(request, "Legacy ID number '" + searchTerm + "' is not valid; try S#### or just ####");
					matches = null;
					break;
				}
				matches = (Collection<Study>) studyService.findByTBStudyID("S" + legacyID.getId().toString());
				break;
			}
			case inAbstract:
				matches = studyService.findByAbstract(keywordSearchTerm);
				break;
			case inCitation:
				matches = studyService.findByCitation(keywordSearchTerm);
				break;
			case byAuthorName:
				matches = studyService.findByAuthor(searchTerm);
				break;
			case byTitle:
				matches = studyService.findByTitle(keywordSearchTerm);
				break;
			case byKeyword:
				matches = studyService.findByKeyword(keywordSearchTerm);
				break;
			default:
				throw new Error ("Unknown search type '" + searchType + "'");
		}
		
		return matches;
//		SearchResults<Study> newRes = intersectSearchResults(oldRes, new StudySearchResults(matches), 
//				new RequestMessageSetter(request), "No matching studies found");
//		
//		saveSearchResults(request, newRes);
//
//		return new ModelAndView("search/studySearch", Constants.RESULT_SET, newRes); 
	}

	/*
	private void validateTaxonSet(HttpServletRequest request,
			HttpServletResponse response, String searchTerm,
			BindException errors) {
		String[] taxonStrings = getTaxonStrings(searchTerm);
		List<String> unrecognizedTaxa = new LinkedList<String> ();
		List<String> recognizedTaxa = new LinkedList<String> ();

		Collection<TaxonLabel> taxonLabels = stringsToTaxonLabels(taxonStrings);
		request.setAttribute("taxonLabelSetOverride", taxonLabels);
		
		for (TaxonLabel tl : taxonLabels) {
			TaxonVariant variant = getTaxonLabelService().findTaxonVariant(tl);
			if (variant == null) {
				variant = getTaxonLabelService().createFromUBIOService(tl);
				if (variant == null) {
					unrecognizedTaxa.add(tl.getTaxonLabel());
				} else {
					recognizedTaxa.add(tl.getTaxonLabel());
				}
			} else {
				recognizedTaxa.add(variant.getName());
			}
		}
		
		LOGGER.info("validateTaxonSet: of " 
				+ taxonStrings.length 
				+ " entered taxa, "
				+ unrecognizedTaxa.size()
				+ " was/were unrecognized: "
				+ unrecognizedTaxa.toString());
		
		request.setAttribute("unrecognizedTaxa", unrecognizedTaxa);
		request.setAttribute("taxonLabels", joinStrings(recognizedTaxa));
		return;
	}

	private String joinStrings(Collection<String> strings) {
		String result = "";
		for (String s : strings) {
			result = result + s + "\n";
		}
		return result;
	}

	private Collection<TaxonLabel> stringsToTaxonLabels(String[] taxonStrings) {
		Collection<TaxonLabel> tls = new LinkedList<TaxonLabel>();
		for (String s : taxonStrings) {
			tls.add(new TaxonLabel(titleCase(s)));
		}
		return tls;
	}

	private String titleCase(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
	*/

	@Override
	SearchResultsType currentSearchType() {
		return SearchResultsType.STUDY;
	}

	@Override
	public String getDefaultViewURL() {
		return "studySearch.html";
	}

	@Override
	protected ModelAndView handleQueryRequest(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws CQLParseException, IOException, InstantiationException {
		String query = request.getParameter("query");						
		CQLParser parser = new CQLParser();
		CQLNode root = parser.parse(query);
		root = normalizeParseTree(root);
		Set<Study> queryResults = doCQLQuery(root, new HashSet<Study>(),request, response, errors);
		StudySearchResults tsr = new StudySearchResults(queryResults);
		saveSearchResults(request, tsr);
		if ( TreebaseUtil.isEmpty(request.getParameter("format")) || ! request.getParameter("format").equals("rss1") ) {			
			return new ModelAndView("search/studySearch", Constants.RESULT_SET, tsr);
		}
		else {
			SearchResults<?> res = tsr;
			String schema = null;
			if ( ! TreebaseUtil.isEmpty(request.getParameter("recordSchema")) ) {
				schema = request.getParameter("recordSchema");
				if ( schema.equals("tree") ) {
					res = tsr.convertToTrees();
				}
				else if ( schema.equals("matrix") ) {
					res = tsr.convertToMatrices();
				}
				else if ( schema.equals("taxon") ) {
					res = tsr.convertToTaxa();
				}
			}
			this.saveSearchResults(request, res);
			return this.searchResultsAsRDF(res, request, root, schema, "study");
		}		
	}
}
