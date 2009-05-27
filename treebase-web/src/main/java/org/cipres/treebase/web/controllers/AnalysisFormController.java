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

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * AnalysisFormController.java
 * 
 * Created on June 6, 2006
 * 
 * @author lcchan
 * 
 */
public class AnalysisFormController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(AnalysisFormController.class);

	public AnalysisService mAnalysisService;

	private StudyService mStudyService;

	private SubmissionService mSubmissionService;

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
	};

	/**
	 * Return the SubmissionService field.
	 * 
	 * @return SubmissionService mSubmissionService
	 */
	private SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	/**
	 * Set the SubmissionService field.
	 */
	public void setSubmissionService(SubmissionService pNewSubmissionService) {
		mSubmissionService = pNewSubmissionService;
	}

	/**
	 * Return the AnalysisService field.
	 * 
	 * @return AnalysisService mAnalysisService
	 */
	public AnalysisService getAnalysisService() {
		return mAnalysisService;
	}

	/**
	 * Set the AnalysisService field.
	 */
	public void setAnalysisService(AnalysisService pNewAnalysisService) {
		mAnalysisService = pNewAnalysisService;
	}

	/**
	 * 
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) throws Exception {

		Analysis analysis = (Analysis) command;

		Long study_id = ControllerUtil.getStudyId(request);
		Study study = mStudyService.findByID(study_id);
		String redirectView = request.getParameter("redirect");

		if (request.getParameter(ACTION_SUBMIT) != null) {
			study.addAnalysis(analysis);
			mStudyService.update(study);
			// TODO:
			// should return the newly created analysis object
			// ControllerUtil.saveAnalysis(request, analysis);
		} else if (request.getParameter(ACTION_UPDATE) != null) {
			mAnalysisService.update(analysis);
			// request.setAttribute("messages",
			// getMessageSourceAccessor().getMessage("citation.update.success"));
			// return showForm(request, response, errors);
		} else if (request.getParameter(ACTION_DELETE) != null) {
			try {
				mAnalysisService.deleteAnalysis(analysis);
			} catch ( Exception e ) { LOGGER.error(e); }
			try {
				analysis = getAnalysisService().update(analysis); // XXX exception here, if analysis contains steps
			} catch ( Exception e ) { LOGGER.error(e); }
			try {
				mAnalysisService.deleteAnalysis(analysis);
			} catch ( Exception e ) { LOGGER.error(e); }			
			try {
				analysis = getAnalysisService().update(analysis);
			} catch ( Exception e ) { LOGGER.error(e); }
			if (request.getSession().getAttribute(Constants.ANALYSIS_MAP) != null) { 
				// delete the attribute only if it exists
				request.getSession().removeAttribute(Constants.ANALYSIS_MAP);
			}

		}
		if ( redirectView == null ) {
			return new ModelAndView(getSuccessView());
		}
		else {
			return new ModelAndView(redirectView);
		}
	}

	/**
	 * Retrieve Analysis object we are working with. Create a new object if it's a new form
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		String analysis_id = ServletRequestUtils.getStringParameter(request, "id", null);
		String username = request.getRemoteUser();

		Long study_id = ControllerUtil.getStudyId(request);
		// Study study = mStudyService.findByID(study_id);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ANALYSIS ID = " + analysis_id + " study id = " + study_id);
		}

		Long submissionId = null;

		Study study = getStudyService().findByID(study_id);
		if (study != null) {
			Submission sub = study.getSubmission();
			if (sub != null) {
				submissionId = sub.getId();
			}
		}

		// FIXME: move the method to studyService, handle the case study w/o submission
		TBPermission perm = getSubmissionService().getPermission(username, submissionId);

		if (TreebaseUtil.isEmpty(analysis_id)) {
			if (perm == TBPermission.WRITE) {
				setAuthorizationChecked(true);
				return new Analysis();
			} else {
				setAuthorizationChecked(false);
				return null;
			}
		} else {
			long analysis_id_long = Long.parseLong(analysis_id);
			TBPermission perm2 = getAnalysisService().getPermission(username, analysis_id_long);
			if (perm2 == TBPermission.WRITE || perm2 == TBPermission.READ_ONLY
				|| perm2 == TBPermission.SUBMITTED_WRITE) {

				Analysis analysis = mAnalysisService.findByID(analysis_id_long);
				ControllerUtil.saveAnalysis(request, analysis);

				if (perm2 == TBPermission.READ_ONLY) {
					// FIXME set a session variable to hide the edit buttons.

				}

				setAuthorizationChecked(true);
				return analysis;
			} else {
				LOGGER.debug("NULL CONDITION SATISFIED");
				setAuthorizationChecked(false);
				return new Analysis();
			}
		}
	}

}
