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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * DisplaySubmissionMenuController.java
 * 
 * Created on Nov 17, 2008
 * 
 * @author rvosa
 * 
 */
public class SubmissionToJsonController implements Controller {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(SubmissionToJsonController.class);

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
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		Long studyId = ControllerUtil.getStudyId(request);
		if (studyId != null) {
			Study study = mStudyService.findByID(studyId);
			ControllerUtil.saveStudy(request, study);
			if (study.isReady()) {
				request.getSession().setAttribute("publicationState", "Ready");
			} else if (study.isPublished()) {
				request.getSession().setAttribute("publicationState", "Published");
			} else {
				request.getSession().setAttribute("publicationState", "NotReady");
			}
			return new ModelAndView("submissionIsland", Constants.STUDY_KEY, study);

		} else {
			request.getSession().setAttribute("publicationState", "NotReady");
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("IN SubmissionToJsonController");
			LOGGER.debug("USER IS: " + request.getRemoteUser());
			LOGGER.debug("Submission ID: " + studyId);
			LOGGER.debug("Publication state: "
				+ request.getSession().getAttribute("publicationState"));
		}
		
		return new ModelAndView(new RedirectView("submissionMain.html"));
	}
}
