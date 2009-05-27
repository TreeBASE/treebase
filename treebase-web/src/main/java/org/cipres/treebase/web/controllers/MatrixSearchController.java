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

import java.util.Collection;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.cipres.treebase.RangeExpression.MalformedRangeExpression;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.search.MatrixSearchResults;
import org.cipres.treebase.domain.search.SearchResults;
import org.cipres.treebase.domain.search.SearchResultsType;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.RequestMessageSetter;
import org.cipres.treebase.web.util.SearchMessageSetter;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

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
		
		LOGGER.info("formName is '" + formName + "'");

		
		if (formName.equals("matrixSimple")) {
			String buttonName = request.getParameter("searchButton");

			if (buttonName.equals("matrixID")) {
				return doSearch(request, response, SearchType.byID, errors);
			} else if  (buttonName.equals("matrixTitle")) {
				return doSearch(request, response, SearchType.byTitle, errors);
			} else if  (buttonName.equals("matrixType")) {
				return doSearch(request, response, SearchType.byType, errors);
			} else if  (buttonName.equals("matrixNTAX")) {
				return doSearch(request, response, SearchType.byNTAX, errors);	
			} else if  (buttonName.equals("matrixNCHAR")) {
				return doSearch(request, response, SearchType.byNCHAR, errors);
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

		SearchResults<Matrix> newRes = intersectSearchResults(oldRes, 
				new MatrixSearchResults(matches), mSetter, "No matching matrices found");

		saveSearchResults(request, newRes);
		
		return new ModelAndView("search/matrixSearch", Constants.RESULT_SET, newRes); 

	}

	SearchResultsType currentSearchType() {
		return SearchResultsType.MATRIX;
	}

	@Override
	public String getDefaultViewURL() {
		return "matrixSearch.html";
	}

}
