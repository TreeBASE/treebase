/*
 * CIPRES Copyright (c) 2005- 2008, The Regents of the University of California All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. *
 * Neither the name of the University of California or the San Diego Supercomputer Center nor the
 * names of its contributors may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
		Submission sub;

		if (TreebaseUtil.isEmpty(studyAccession)) {
			
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"Please provide a study accession number.");
		}

//		if (!ControllerUtil.checkForLongNumber(submissionID)) {
//			return setAttributeAndShowForm(
//				request,
//				response,
//				bindExp,
//				"errors",
//				"Submission ID has to be a number.");
//		}

		sub = getSubmissionHome().findByStudyAccessionNumber(studyAccession);
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
