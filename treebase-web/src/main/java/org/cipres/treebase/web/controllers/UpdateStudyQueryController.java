
package org.cipres.treebase.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	private static final Logger LOGGER = LogManager.getLogger(UpdateStudyQueryController.class);

	
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