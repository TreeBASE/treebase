
package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.model.LabelValue;
import org.cipres.treebase.web.model.SearchCommand;
import org.cipres.treebase.web.model.SearchCriteriaCommand;

/**
 * SearchFormController.java
 * 
 * Created on June 16, 2006
 * 
 * @author lcchan
 * 
 */
public class SearchFormController extends BaseFormController {
	private static final Logger LOGGER = Logger.getLogger(SearchFormController.class);

	private StudyService mStudyService;

	/**
	 * Return the StudyService field.
	 * 
	 * @return StudyService mStudyService
	 */
	public StudyService getStudyService() {
		return mStudyService;
	}

	/**
	 * Set the StudyService field.
	 */
	public void setStudyService(StudyService pNewStudyService) {
		mStudyService = pNewStudyService;
	}

	/**
	 * SEARCH_AUTHOR Data for drop down lists
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Map referenceData(HttpServletRequest request) {

		Map criteriaMap = new HashMap();

		List categories = new ArrayList();
		categories.add(new LabelValue(Constants.SEARCH_STUDY, Constants.SEARCH_STUDY));
		categories.add(new LabelValue(Constants.SEARCH_MATRIX, Constants.SEARCH_MATRIX));
		categories.add(new LabelValue(Constants.SEARCH_TREE, Constants.SEARCH_TREE));
		criteriaMap.put("categories", categories);

		List attributes = new ArrayList();
		attributes.add(new LabelValue(Constants.SEARCH_AUTHOR, Constants.SEARCH_AUTHOR));
		attributes.add(new LabelValue(
			Constants.SEARCH_CITATION_TITLE,
			Constants.SEARCH_CITATION_TITLE));
		attributes.add(new LabelValue(Constants.SEARCH_SOFTWARE, Constants.SEARCH_SOFTWARE));
		attributes.add(new LabelValue(Constants.SEARCH_ALGORITHM, Constants.SEARCH_ALGORITHM));
		Collections.sort(attributes, LabelValue.CASE_INSENSITIVE_ORDER);
		criteriaMap.put("attributes", attributes);

		List matches = new ArrayList();
		matches.add(new LabelValue("Equals", "Equals"));
		matches.add(new LabelValue("Contains", "Contains"));
		criteriaMap.put("matches", matches);

		return criteriaMap;
	}

	/**
	 * It's not a real submit unless "Submit" is entered (javascript triggered submit())
	 * 
	 * Creation date: June 5, 2006 12:25:28 PM
	 */
	@Override
	public boolean isFormChangeRequest(HttpServletRequest request) {
		/*
		 * The following are the real "Submit" name on the buttons
		 * 
		 */
		request.getSession().removeAttribute(Constants.SEARCH_COMMAND);

		return ((request.getParameter("And") == null) && (request.getParameter("OR") == null)
			&& (request.getParameter(ACTION_DELETE) == null) && (request
			.getParameter("Build Query") == null));
	}

	/**
	 * 
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) throws Exception {

		LOGGER.debug("onSubmit is called");

		SearchCommand searchCommand = (SearchCommand) command;
		List<SearchCriteriaCommand> criterias = searchCommand.getCriterias();

		if (request.getParameter("And") != null) {
			addCriterias(request, searchCommand, Constants.AND_OPERATOR);
		} else if (request.getParameter("OR") != null) {
			addCriterias(request, searchCommand, Constants.OR_OPERATOR);
		} else if (request.getParameter(ACTION_DELETE) != null) {
			if (searchCommand.getCriterias().size() > 0) {
				String[] deleteConditions = request.getParameterValues("deleteCondition");
				for (int i = 0; deleteConditions != null && i < deleteConditions.length; i++) {
					criterias.set(i, null);
				}// end for
				for (int i = 0; i <= criterias.size(); i++) {
					criterias.remove(null);
				}
			}
		} else if (request.getParameter("Build Query") != null) {
			// send this to session so next page can access the data
			addCriterias(request, searchCommand, null); // do not add operator
			return new ModelAndView(new RedirectView("study-query.html"));
		}
		// send data to request
		request.setAttribute(Constants.SEARCH_COMMAND, searchCommand);
		return showForm(request, response, errors);
	}

	/*
	 * add a search criteria to memory
	 */
	private void addCriterias(
		HttpServletRequest request,
		SearchCommand searchCommand,
		String operator) {

		LOGGER.debug("addCriterias() is called");

		if (request.getParameter("newValue").length() > 0) {
			if (searchCommand.getCriterias() == null) {
				searchCommand.setCriterias(new ArrayList<SearchCriteriaCommand>());
			}
			String value = request.getParameter("newValue");
			String match = request.getParameter("newMatch");
			String attribute = request.getParameter("newAttribute");

			boolean added = isAdded(searchCommand, attribute, match, value);
			if (!added)
				searchCommand.getCriterias().add(
					new SearchCriteriaCommand(attribute, match, value, operator));
		}
		request.getSession().setAttribute(Constants.SEARCH_COMMAND, searchCommand);
	}

	/*
	 * 
	 */
	private boolean isAdded(
		SearchCommand searchCommand,
		String attribute,
		String match,
		String value) {

		LOGGER.debug("isAdded() is called");

		List<SearchCriteriaCommand> criterias = searchCommand.getCriterias();
		boolean returnValue = false;
		for (SearchCriteriaCommand command : criterias) {
			if (command.getAttribute().equalsIgnoreCase(attribute)
				&& command.getMatch().equalsIgnoreCase(match)
				&& command.getValue().equalsIgnoreCase(value)) returnValue = true;
		}
		return returnValue;
	}

	/**
	 * 
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		LOGGER.debug("formBackingObject() is called");
		// retrieve SearchCommand object from request scope
		SearchCommand searchCommand = (SearchCommand) request.getSession().getAttribute(
			Constants.SEARCH_COMMAND);
		if (searchCommand == null) {
			searchCommand = new SearchCommand();
		}
		return searchCommand;
	}
}
