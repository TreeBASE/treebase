/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ 
 * All Rights Reserved. 
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for educational, research and non-profit purposes, without
 * fee, and without a written agreement is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies. 
 *
 * Permission to incorporate this software into commercial products may be
 * obtained by contacting us:
 *              http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event
 * shall the CIPRES project be liable to any party for direct, indirect,
 * special, incidental, or consequential damages, including lost profits, 
 * arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage.  
 * The CIPRES project specifically disclaims any warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for
 * a particular purpose. The CIPRES project has no obligations to provide 
 * maintenance, support, updates, enhancements, or modifications. 
 */

package org.cipres.treebase.web.controllers;

import org.apache.log4j.Logger;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.model.SearchCommand;
import org.cipres.treebase.web.model.SearchCriteriaCommand;

/**
 * UpdateStudyQuery.java
 * 
 * Created on Aug 29, 2006
 * @author lcchan
 *
 */
public class UpdateStudyQueryController extends BaseFormController {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(UpdateStudyQueryController.class);

	
	public ModelAndView onSubmit (HttpServletRequest request,
				HttpServletResponse response,
				Object command,
				BindException errors) throws Exception {

		SearchCommand searchCommand = (SearchCommand) command;
		List<SearchCriteriaCommand> criterias = searchCommand.getCriterias();
		if (request.getParameter("Submit Query") != null) {
			if (criterias.size() == 0) {
				request.setAttribute("messages", getMessageSourceAccessor().getMessage("search.no.criteria.selected"));
				return showForm(request, response, errors);
			}
			return new ModelAndView(new RedirectView("searchStudyList.html"));
		} else if (request.getParameter("Modify Query") != null) {
			return new ModelAndView(new RedirectView("searchForm.html"));
		}
		return new ModelAndView(getSuccessView());
	}
/**
 *  
 * 
 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
 */
protected Object formBackingObject(HttpServletRequest request) throws ServletException {

	// retrieve SearchCommand object from request scope
	SearchCommand searchCommand = (SearchCommand) request.getSession().getAttribute(Constants.SEARCH_COMMAND);
	if (searchCommand == null) {
		searchCommand = new SearchCommand();
	}
	return searchCommand;
}

}