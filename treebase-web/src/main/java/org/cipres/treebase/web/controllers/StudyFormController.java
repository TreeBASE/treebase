/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.TreebaseUtil;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * StudyFormController.java
 * 
 * Created on June 5, 2006
 * 
 * @author lcchan
 * 
 */
public class StudyFormController extends BaseFormController {
	private static final Logger LOGGER = Logger.getLogger(StudyFormController.class);

	private StudyService mStudyService;
	private SubmissionService mSubmissionService;
	private UserService mUserService;

	/**
	 * Return the SubmissionService field.
	 * 
	 * @return SubmissionService mSubmissionService
	 */
	public SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	/**
	 * Set the SubmissionService field.
	 */
	public void setSubmissionService(SubmissionService pNewSubmissionService) {
		mSubmissionService = pNewSubmissionService;
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
	 * 
	 * Creation date: June 5, 2006 5:28:42 PM
	 */
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) throws Exception {

		Study study = (Study) command;
		User user = ControllerUtil.getUser(request, mUserService);

		if (request.getParameter(ACTION_SUBMIT) != null) {
			// Study must be submitted with citation together
			// here we are just saving the data to the session
			// request.getSession().setAttribute(Constants.STUDY_KEY, study);

			// BeanUtils.copyProperties(citationCommand.getCitationMap(citationType),
			// citationCommand);

			// FIXME citation
			// Citation c = new ArticleCitation();
			// c.setStudy(study);
			// study.setCitation(c);

			// retrieve Study object from session to be submitted with citation
			// Study study = (Study) request.getSession().getAttribute(Constants.STUDY_KEY);
			// citationCommand.getCitationMap(citationType).setStudy(study);
			Submission submission = mSubmissionService.createSubmission(user, study);

			// save Study object to session and remove
			ControllerUtil.saveStudy(request, submission.getStudy());

		} else if (request.getParameter(ACTION_UPDATE) != null) {
			mStudyService.update(study);
		} else if (request.getParameter(ACTION_DELETE) != null) {

			study = getStudyService().update(study);
			getSubmissionService().deleteSubmission(study.getSubmission());
			return new ModelAndView(new RedirectView("submissionList.html"));
		} else if (request.getParameter(ACTION_CANCEL) != null) {
			return new ModelAndView(getCancelView());
		}
		return new ModelAndView(getSuccessView());
	}

	/**
	 * There are 3 different ways a StudyForm can be accessed
	 * 
	 * i) from the list of submission (submission_id is indicated) ii) from the right RHS menu (no
	 * submission_id) iii)new submission
	 * 
	 * Creation date: June 5, 2006 5:19:18 PM
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		Study study = new Study();
		String submission_id = ServletRequestUtils.getStringParameter(request, "id", null);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("In studyFormController--");
		}

		if (!TreebaseUtil.isEmpty(submission_id)) {
			study = mStudyService.findBySubmissionID(Long.parseLong(submission_id));
			ControllerUtil.saveStudy(request, study); // user has made selection
			return study;
		}
		// if we start a new form (indicated by URL)
		String form = request.getParameter("form");
		if (form != null && form.equals("new")) {
			// start a new form, remove what's in the session
			request.getSession().removeAttribute(Constants.STUDY_KEY);
			request.getSession().removeAttribute(Constants.STUDY_MAP);
			return new Study();
		}
		// if we are updating a data that's already in the db (access from RHS menu)
		study = ControllerUtil.findStudy(request, mStudyService);
		return study;
	}
}
