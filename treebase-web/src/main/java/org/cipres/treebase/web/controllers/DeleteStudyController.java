
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
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * DeleteStudyController.java
 * 
 * Created on July 23, 2007
 * 
 * @author Madhu
 * 
 */
public class DeleteStudyController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(DeleteStudyController.class);
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

	public StudyService getStudyService() {
		return mStudyService;
	}

	public void setStudyService(StudyService pStudyService) {
		mStudyService = pStudyService;
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
		BindException errors) throws Exception {

		Long subId = (Long) command;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("IN DeletedStudyCONTROLLER -SUBMISSION ID =" + subId);
		}

		String nextView = getSuccessView();
		if (request.getParameter(ACTION_DELETE) != null) {
			getSubmissionService().deleteSubmission(subId);
			
			//now check whether the next view is defined:
			//if defined, use it and clear it! 
			// if not defined use the succeessView. 
			String delFromView = getPredefeinedNextView(request);
			if (!TreebaseUtil.isEmpty(delFromView)) {
				nextView = delFromView;
			}
		} else if (request.getParameter(ACTION_CANCEL) != null) {

		}
		return new ModelAndView(nextView);

	}

	/**
	 * Check whether the next view is defined:
	 * if defined, use it and clear it! 
	 * 
	 * @param request
	 * @return
	 */
	private String getPredefeinedNextView(HttpServletRequest request) {
		String delFromView = (String) request.getSession().getAttribute(ControllerUtil.DEL_STUDY_PAGE_KEY);
		if (!TreebaseUtil.isEmpty(delFromView)) {
			delFromView = "redirect:" + delFromView;
			request.getSession().removeAttribute(ControllerUtil.DEL_STUDY_PAGE_KEY);
		}
		return delFromView;
	}

	/**
	 * Override to return to the correct view when cancel. 
	 * 
	 * @see org.springframework.web.servlet.mvc.CancellableFormController#onCancel(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	protected ModelAndView onCancel(
		HttpServletRequest pRequest,
		HttpServletResponse pResponse,
		Object pCommand) throws Exception {

		String nextView = getCancelView();
		String delFromView = getPredefeinedNextView(pRequest);
		if (!TreebaseUtil.isEmpty(delFromView)) {
			nextView = delFromView;
		}
		return new ModelAndView(nextView);
	}

	/**
	 * Retrieve Analysis object we are working with. Create a new object if it's a new form
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		String username = request.getRemoteUser();
		String subid = ServletRequestUtils.getStringParameter(request, "submissionid", null);

		// FIXME: move the method to studyService, handle the case study w/o submission
		// Here I am making sure that attempt is made to delete a study owned by the user
		// and not the one owned by anyone else.
		TBPermission perm = getSubmissionService().getPermission(username, Long.parseLong(subid));

		if (perm == TBPermission.WRITE) {
			LOGGER.info("setAuthorizationChecked(true)");
			setAuthorizationChecked(true);
		} else {
			LOGGER.info("setAuthorizationChecked(false)");
			setAuthorizationChecked(false);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("IN DELETESTUDYCONTROLLER-VALUE OF SUBMISSION ID in FORM BACKING ="
				+ subid);
		}

		// return getSubmissionService().findSubmissionByID(Long.parseLong(subid));
		return Long.parseLong(subid);

	}
}
