
package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private static final Logger LOGGER = LogManager.getLogger(SubmissionToJsonController.class);

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
