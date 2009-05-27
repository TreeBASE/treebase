/*
 * Copyright 2007 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseIDString.MalformedTreebaseIDString;
import org.cipres.treebase.domain.search.SearchResults;
import org.cipres.treebase.domain.search.SearchResultsType;
import org.cipres.treebase.domain.search.StudySearchResults;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.RequestMessageSetter;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

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
		
		LOGGER.info("formName is '" + formName + "'");
		
		if (formName.equals("searchKeyword")) {
			SearchType searchType;
			String buttonName = request.getParameter("searchButton");
			
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

			return doSearch(request, response, searchType, errors);	
		} else {
			return super.onSubmit(request, response, command, errors);
		}
	}
	
	protected ModelAndView doSearch(
			HttpServletRequest request,
			HttpServletResponse response,
			SearchType searchType,
			BindException errors) throws InstantiationException {
		
		String searchTerm = convertStars(request.getParameter("searchTerm"));
		String keywordSearchTerm = "%" + searchTerm + "%";
		StudySearchResults oldRes;	

		{
			SearchResults<?> sr = searchResults(request);
			if (sr != null) {
				oldRes = (StudySearchResults) sr.convertToStudies();
			} else {
				oldRes = new StudySearchResults ();   // TODO: Convert existing search results to new type	
			}
		}
		
		LOGGER.info("doSearch old results contained " + oldRes.size() + " item(s)");
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
		
		SearchResults<Study> newRes = intersectSearchResults(oldRes, new StudySearchResults(matches), 
				new RequestMessageSetter(request), "No matching studies found");
		
		saveSearchResults(request, newRes);

		return new ModelAndView("search/studySearch", Constants.RESULT_SET, newRes); 
	}

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

	@Override
	SearchResultsType currentSearchType() {
		return SearchResultsType.STUDY;
	}

	@Override
	public String getDefaultViewURL() {
		return "studySearch.html";
	}
}
