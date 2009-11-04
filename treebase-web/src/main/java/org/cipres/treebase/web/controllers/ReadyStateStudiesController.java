


package org.cipres.treebase.web.controllers;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;

/**
 * 
 * @author madhu
 * 
 * Generated on November 16, 2007
 * 
 * It has become REDUNDAND
 * 
 */

public class ReadyStateStudiesController implements Controller {

	SubmissionService pSubmissionService;

	public ModelAndView handleRequest(HttpServletRequest pArg0, HttpServletResponse pArg1)
		throws Exception {

		Collection<Submission> completeReadyStateSubmissionList = getSubmissionService()
			.findReadySubmissions();

		return new ModelAndView(
			"readyStateSubmissionList",
			"readyStateSubmissionList",
			completeReadyStateSubmissionList);
		// TODO Auto-generated method stub
		// return null;
	}

	/**
	 * @return the pSubmissionService
	 */
	public SubmissionService getSubmissionService() {
		return pSubmissionService;
	}

	/**
	 * @param pSubmissionService the pSubmissionService to set
	 */
	public void setSubmissionService(SubmissionService pSubmissionService) {
		this.pSubmissionService = pSubmissionService;
	}

}
