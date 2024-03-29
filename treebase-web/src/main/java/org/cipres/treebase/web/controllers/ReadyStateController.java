
package org.cipres.treebase.web.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.study.ArticleCitation;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * ReadyStateController.java
 * 
 * Created on July 23, 2007
 * 
 * @author Madhu
 * 
 */
public class ReadyStateController extends BaseFormController {

	private static final Logger LOGGER = LogManager.getLogger(ReadyStateController.class);
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
		
		String username = request.getRemoteUser();
		Study study;
		if (TreebaseUtil.isEmpty(subid)) {
			study = ControllerUtil.findStudy(request, mStudyService);
			LOGGER.info("setAuthorizationChecked(true)");
			setAuthorizationChecked(true);// This is needed in case one is clicking at Summary
			// link at the bottom of the menu list on the right hand
			// side.
		} else {
			study = mStudyService.findBySubmissionID(Long.parseLong(subid));
		}
		if (!TreebaseUtil.isEmpty(subid)) {

			TBPermission perm2 = getSubmissionService().getPermission(
				username,
				Long.parseLong(subid));
			if (perm2 == TBPermission.WRITE || perm2 == TBPermission.READ_ONLY
				|| perm2 == TBPermission.SUBMITTED_WRITE) {
				LOGGER.info("setAuthorizationChecked(true)");
				setAuthorizationChecked(true);
			} else {
				LOGGER.info("setAuthorizationChecked(false)");
				setAuthorizationChecked(false);
				return new ArticleCitation();
			}

			ControllerUtil.saveStudy(request, study); // user has made selection
			// Added by Madhu to set the session variables
			if (study.isReady()) {
				request.getSession().setAttribute("publicationState", "Ready");
			} else if (study.isPublished()) {
				request.getSession().setAttribute("publicationState", "Published");
			} else {
				request.getSession().setAttribute("publicationState", "NotReady");
			}

		} else {
			request.getSession().setAttribute("publicationState", "NotReady");
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("VALUE OF SUBMISSION ID in FORM BACKING =" + subid);
		}

		// return getSubmissionService().findSubmissionByID(Long.parseLong(subid));
		return Long.parseLong(subid);

	}

	/*protected Map<String, Boolean> referenceData(HttpServletRequest pRequest) {
		String subid = ServletRequestUtils.getStringParameter(pRequest, "submissionid", null);
		Study study = mStudyService.findBySubmissionID(Long.parseLong(subid));

		HashMap<String, Boolean> validatedProperties = new HashMap<String, Boolean>();
		validatedProperties.put("AnalysesStatus", study.getAnalysesStatus());

		return validatedProperties;
	}*/
	
	protected Map<String, Object> referenceData(HttpServletRequest pRequest) {
		String subid = ServletRequestUtils.getStringParameter(pRequest, "submissionid", null);
		Study study = mStudyService.findBySubmissionID(Long.parseLong(subid));

		HashMap<String, Object> validatedProperties = new HashMap<String, Object>();
		validatedProperties.put("AnalysesStatus", study.getAnalysesStatus());
		validatedProperties.put("submissionNumber", Long.parseLong(subid));

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
