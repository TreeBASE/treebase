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
