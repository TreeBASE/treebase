
package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import org.cipres.treebase.domain.admin.UserService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * SearchStudyController.java
 * 
 * Created on June 27, 2006
 * 
 * @author lcchan
 * 
 */
// TODO: please holder for search right now: create a study object
public class SearchStudyController implements Controller {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = LogManager.getLogger(SearchStudyController.class);

	private StudyService mStudyService;
	private UserService mUserService;

	/**
	 * Return the UserService field.
	 * 
	 * @return UserService mUserService
	 */
	public UserService getUserService() {
		return mUserService;
	}

	/**
	 * Set the UserService field.
	 */
	public void setUserService(UserService pNewUserService) {
		mUserService = pNewUserService;
	}

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
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String study_id = ServletRequestUtils.getStringParameter(request, "id", null);
		Study study = mStudyService.findByID(Long.parseLong(study_id));
		ControllerUtil.saveStudy(request, study, Constants.SEARCH_ACTION);
		request.getSession().setAttribute("search", "y");
		return new ModelAndView(new RedirectView("analysisDisplay.html"));
	}
}
