
package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.cipres.treebase.TreebaseUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * DisplaySubmissionMenuController.java
 * 
 * Created on Aug 30, 2006
 * 
 * @author lcchan
 * 
 */
public class DisplaySubmissionMenuController implements Controller {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = LogManager.getLogger(DisplaySubmissionMenuController.class);

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

		String submission_id = ServletRequestUtils.getStringParameter(request, "id", null);
		if (!TreebaseUtil.isEmpty(submission_id)) {
			Study study = mStudyService.findBySubmissionID(Long.parseLong(submission_id));
			ControllerUtil.saveStudy(request, study); // user has made selection
			// Added by Madhu to set the session variable
			if (study.isReady()) {
				request.getSession().setAttribute("publicationState", "Ready");
			} else if (study.isPublished()) {
				request.getSession().setAttribute("publicationState", "Published");
			} else {
				request.getSession().setAttribute("publicationState", "NotReady");
			}
			// ///////

		} else {
			request.getSession().setAttribute("publicationState", "NotReady");
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("IN DisplaySubmissionMenuController");
			LOGGER.debug("USER IS: " + request.getRemoteUser());
			LOGGER.debug("Submission ID: " + submission_id);
			LOGGER.debug("Publication state: "
				+ request.getSession().getAttribute("publicationState"));
		}

		return new ModelAndView(new RedirectView("submissionMain.html"));
	}
}
