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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import org.cipres.treebase.domain.admin.UserService;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * 
 * ListSubmissionController.java
 * 
 * List all treebase submissions by a logged user using MultiActionController
 * 
 * Created on May 2, 2006
 * 
 * @author lcchan
 * 
 * Modified on July 2nd, 2007 By
 * @author Madhu
 */
public class ListSubmissionController extends MultiActionController {
	private static final Logger LOGGER = Logger.getLogger(ListSubmissionController.class);

	private UserService mUserService;
	private SubmissionService mSubmissionService;

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

	public SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	/**
	 * Set the SubmissionService
	 */
	public void setSubmissionService(SubmissionService pSubmissionService) {
		mSubmissionService = pSubmissionService;
	}

	/**
	 * Look up all user submissions for an user using User object. This is the default method
	 * 
	 * Creation date: May 22, 2006 2:39:17 PM
	 */
	public ModelAndView submissionsByUser(HttpServletRequest request, HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering submissionsByUser() for " + request.getRemoteUser()); //$NON-NLS-1$
		}

		request.setAttribute("method", request.getParameter("method"));
		request.getSession().setAttribute("search", "n");
		request.getSession().setAttribute("publicationState", "NotReady");

		if (LOGGER.isDebugEnabled()) {

			LOGGER
				.debug("Setting the session variable 'publicationState in submissionByUser method"); //$NON-NLS-1$
			LOGGER.debug("Value of the session variable 'publicationState is: "
				+ request.getSession().getAttribute("publicationState"));

			LOGGER.debug("Boolean Value for User: " + request.isUserInRole("User"));

		}

		boolean isUser = request.isUserInRole("User");
		request.getSession().setAttribute("isUser", Boolean.valueOf(isUser));

		return new ModelAndView("submissionList", Constants.SUBMISSION_LIST, ControllerUtil
			.getUser(request, mUserService).getSubmissionIterator());
	}

	/**
	 * Retrieve submission by status "In Progress" for an user Creation date: May 22, 2006 3:10:49
	 * PM
	 */
	public ModelAndView submissionsInProgress(
		HttpServletRequest request,
		HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering submissionsInProgress()"); //$NON-NLS-1$
		}
		/*
		 * send method so jsp page remembers it
		 */

		request.setAttribute("method", request.getParameter("method"));
		return new ModelAndView("submissionList", Constants.SUBMISSION_LIST, ControllerUtil
			.getUser(request, mUserService).getInProgressSubmissions());
	}

	/**
	 * Retrieve submissions by status "Submitted" for an user
	 * 
	 * Creation date: May 22, 2006 3:28:59 PM
	 */
	public ModelAndView submissionsSubmitted(
		HttpServletRequest request,
		HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering submissionsSubmitted()"); //$NON-NLS-1$
		}
		/*
		 * send "method" to jsp page so we can remember it
		 */

		request.setAttribute("method", request.getParameter("method"));

		return new ModelAndView("submissionList", Constants.SUBMISSION_LIST, ControllerUtil
			.getUser(request, mUserService).getReadySubmissions());

	}

	/**
	 * Retrieve submissions by status "Published"
	 * 
	 * Creation date: May 22, 2006 3:34:44 PM
	 */
	public ModelAndView submissionsPublished(
		HttpServletRequest request,
		HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering submissionsPublished"); //$NON-NLS-1$
		}
		/*
		 * send "method" to jsp page so we can remember it
		 */

		request.setAttribute("method", request.getParameter("method"));
		return new ModelAndView("submissionList", Constants.SUBMISSION_LIST, ControllerUtil
			.getUser(request, mUserService).getPublishedSubmissions());
	}

	/**
	 * This method has become redundant -- Madhu
	 * 
	 * @author madhu
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView changeToReadyState(HttpServletRequest request, HttpServletResponse response) {

		// System.out.println("Inside changeToReadyState method " + request.getParameter("method"));
		getSubmissionService().updateStudyStatusReady(
			Long.parseLong(request.getParameter("submissionid")));
		request.setAttribute("method", "submissionsByUser");
		// request.setAttribute("method", null);
		return new ModelAndView("submissionList", Constants.SUBMISSION_LIST, ControllerUtil
			.getUser(request, mUserService).getSubmissionIterator());

	}

	/**
	 * @author madhu
	 * 
	 * This method has become redundant -- Madhu
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	public ModelAndView deleteSubmission(HttpServletRequest request, HttpServletResponse response) {

		if (request.getParameter("submissionid") != null) {
			getSubmissionService().deleteSubmission(
				getSubmissionService().findSubmissionByID(
					Long.parseLong(request.getParameter("submissionid"))));
		}
		request.setAttribute("method", "submissionsByUser");
		return new ModelAndView("submissionList", Constants.SUBMISSION_LIST, ControllerUtil
			.getUser(request, mUserService).getSubmissionIterator());
	}
}
