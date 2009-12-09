package org.cipres.treebase.web.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionHome;

/**
 * @author madhu
 * 
 * Created on January 4th, 2008
 */

public class SearchBySubmissionIDController extends BaseFormController {
	private static final Logger LOGGER = Logger.getLogger(SearchBySubmissionIDController.class);

	private SubmissionHome mSubmissionHome;

	/**
	 * Return the Submission field.
	 * 
	 * @return SubmissionHome 
	 */
	public SubmissionHome getSubmissionHome() {
		return mSubmissionHome;
	}

	/**
	 * Set the SubmissionHome field.
	 */
	public void setSubmissionHome(SubmissionHome pNewSubmissionHome) {
		mSubmissionHome = pNewSubmissionHome;
	}

	/*
	 * (non-Javadoc)
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

		String studyAccession = request.getParameter("submissionaccession").trim();
		String identifierType = request.getParameter("identifierType").trim();
		Submission sub;

		if (TreebaseUtil.isEmpty(studyAccession)) {			
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"Please provide a study accession number.");
		}
		if ( ! studyAccession.startsWith("S") ) {
			studyAccession = "S" + studyAccession;
		}
		if ( identifierType.equals("TB1") ) {
			sub = getSubmissionHome().findByStudyAccessionNumber(studyAccession);
		}
		else {
			sub = getSubmissionHome().findBySubmissionNumber(studyAccession);
		}
		
		if (sub == null) {
			return setAttributeAndShowForm(request, response, bindExp, "errors", "Submission accession: '"
				+ studyAccession + "' does not exist.");
		}

		request.getSession().setAttribute("SUBMISSION_FROM_SEARCHBYSUBMISSIONID", sub.getId());
		request.getSession().setAttribute("ADMIN_COMING_FROM", "SearchBySubmissionID");

		return new ModelAndView(getSuccessView());

	}

	/**
	 * 
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		return new Object();
	}

}
