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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.SubmissionService;

/**
 * ReadyStateController.java
 * 
 * Created on July 23, 2007
 * 
 * @author Madhu
 * 
 */
public class ReadyStateController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(ReadyStateController.class);
	private SubmissionService mSubmissionService;
	private StudyService mStudyService;

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
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		String username = request.getRemoteUser();
		Long subId = (Long) command;

		if (request.getParameter(ACTION_SUBMIT) != null) {

			TBPermission perm2 = getSubmissionService().getPermission(username, subId);
			if (perm2 == TBPermission.WRITE) {
				getSubmissionService().updateStudyStatusReady(subId);
			} else {

				return setAttributeAndShowForm(
					request,
					response,
					bindExp,
					"errors",
					"Sorry Authorization Failure, you cannot change the status of this study.");
			}
		} else if (request.getParameter(ACTION_CANCEL) != null) {

		}
		return new ModelAndView(getSuccessView());

	}

	/**
	 * Retrieve Analysis object we are working with. Create a new object if it's a new form
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		String subid = ServletRequestUtils.getStringParameter(request, "submissionid", null);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("VALUE OF SUBMISSION ID in FORM BACKING =" + subid);
		}

		// return getSubmissionService().findSubmissionByID(Long.parseLong(subid));
		return Long.parseLong(subid);

	}

	protected Map<String, Boolean> referenceData(HttpServletRequest pRequest) {
		String subid = ServletRequestUtils.getStringParameter(pRequest, "submissionid", null);
		Study study = mStudyService.findBySubmissionID(Long.parseLong(subid));

		HashMap<String, Boolean> validatedProperties = new HashMap<String, Boolean>();
		validatedProperties.put("AnalysesStatus", study.getAnalysesStatus());

		return validatedProperties;
	}

	/**
	 * @return the studyService
	 */
	public StudyService getStudyService() {
		return mStudyService;
	}

	/**
	 * @param pStudyService the studyService to set
	 */
	public void setStudyService(StudyService pStudyService) {
		mStudyService = pStudyService;
	}
}
