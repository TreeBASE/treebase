
package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyCriteria;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.model.SearchCommand;
import org.cipres.treebase.web.model.SearchCriteriaCommand;

/**
 * ListSearchStudyController.java
 * 
 * Created on Aug 25, 2006
 * @author lcchan
 *
 */
public class ListSearchStudyController implements Controller {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(ListSearchStudyController.class);

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
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		SearchCommand searchCommand = (SearchCommand) request.getSession().getAttribute(Constants.SEARCH_COMMAND);
		StudyCriteria studyCriteria = new StudyCriteria();
		List<SearchCriteriaCommand> criterias = searchCommand.getCriterias();
		
		Collection<Study> studyList = new ArrayList<Study>();
		// no longer needed
		request.getSession().removeAttribute(Constants.SEARCH_COMMAND);
		if (searchCommand == null) {
			return new ModelAndView("studyList", Constants.STUDY_LIST, studyList);
		}
		
		//	TODO:
		// need a better way to handle this - hardcode it for now
		List<String> authors = new ArrayList<String>();
		List<String> citationTitles = new ArrayList<String>();
		for (SearchCriteriaCommand criteria : criterias) {
			String attribute = criteria.getAttribute();
			String value = criteria.getValue();
			if (attribute == null || value == null) continue;
			// treat all search as equal to
			if (attribute.equals(Constants.SEARCH_AUTHOR)) {
				authors.add(value);
			}  else if (attribute.equals(Constants.SEARCH_CITATION_TITLE)) {
				citationTitles.add(Constants.SEARCH_CITATION_TITLE);
			} else if (attribute.equals(Constants.SEARCH_ALGORITHM)) {
				studyCriteria.setAlgorithm(value); 
			} else if (attribute.equals(Constants.SEARCH_SOFTWARE)) {
				studyCriteria.setSoftware(value);
			}
			// end if
		} // end for
		studyCriteria.setAuthorLastNames(authors);
		studyCriteria.setCitationTitles(citationTitles);
		studyList = mStudyService.findByCriteria(studyCriteria);
		
		return new ModelAndView("studyList", Constants.STUDY_LIST, studyList);
	}
}
