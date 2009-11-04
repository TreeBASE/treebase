
package org.cipres.treebase.web.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * DisplaySubmissionController.java
 * 
 * Created on May 5, 2006
 * @author lcchan
 *
 */
public class DisplaySubmissionController extends BaseFormController {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(DisplaySubmissionController.class);
	
	private StudyService mStudyService;
	private SubmissionService mSubmissionService;
	
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
	 *  
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	public ModelAndView onSubmit(HttpServletRequest request,
								 HttpServletResponse response,
								 Object command,
								 BindException errors) throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering onSubmit()");
		}
		/*
		 * Retrieve Study object by submissoin_id
		 */
		Study study = ControllerUtil.findStudy(request, mStudyService);
		
		if (request.getParameter(ACTION_DELETE) != null) {
			boolean val = mSubmissionService.deleteSubmission(study.getSubmission());
			request.getSession().removeAttribute(Constants.STUDY_MAP);
			return new ModelAndView(getSuccessView());
		} else if (request.getParameter(ACTION_UPDATE) != null) {
			/* 
			 * Remember the submission_id so we know which study/submission we are working with
			 */
			ControllerUtil.saveStudy(request, study);
			return new ModelAndView("redirect:/user/submissionMain.html");
		}
		
		return new ModelAndView(getSuccessView());
	}
	/**
	 * 
	 * Creation date: Jun 3, 2006 2:24:42 PM
	 */
	protected Object formBackingObject (HttpServletRequest request) throws ServletException{
		String submission_id = request.getParameter("id");
		Study study = ControllerUtil.findStudy(request, mStudyService);
		/* 
		 * Send "submission_id so we can display RHS menu
		 * Do not send it if want to wait till after "submit" to show the menu
		 * 
		 */
		ControllerUtil.saveStudy(request, study);
		return study;
	}
	
	
}
