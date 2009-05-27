/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisService;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.model.AnalysisStepCommand;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * ListAnalysisStepController.java
 * 
 * Created on June 6, 2006
 * 
 * @author lcchan
 * 
 */
public class ListAnalysisStepController implements Controller {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(ListAnalysisStepController.class);

	private StudyService mStudyService;
	private AnalysisService mAnalysisService;

	/**
	 * Return the AnalysisService field.
	 * 
	 * @return AnalysisService mAnalysisService
	 */
	public AnalysisService getAnalysisService() {
		return mAnalysisService;
	}

	/**
	 * Set the AnalysisService field.
	 */
	public void setAnalysisService(AnalysisService pNewAnalysisService) {
		mAnalysisService = pNewAnalysisService;
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
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		/*
		 * retrieve list of anlysisSteps for an analysis
		 */
		// TODO: check if value is null
		String analysis_id = ServletRequestUtils.getStringParameter(request, "id", null);
		Analysis analysis = mAnalysisService.findByID(Long.parseLong(analysis_id));
		List<AnalysisStep> steps = analysis.getAnalysisStepsReadOnly();
		// send selected analysis info to session
		ControllerUtil.saveAnalysis(request, analysis);
		/*
		 * map AnalysisStep to AnalysisStepCommand so we can print the algorithm type and the order
		 */
		List<AnalysisStepCommand> list = new ArrayList<AnalysisStepCommand>();
		int order = 0;
		for (AnalysisStep step : steps) {
			AnalysisStepCommand command = new AnalysisStepCommand();
			BeanUtils.copyProperties(step, command);
			command.setAlgorithmType(ControllerUtil.getAlgorithmType(step.getAlgorithmInfo()));
			command.setOrder(order);
			list.add(order, command);
			order++;
		}
		return new ModelAndView("analysisStepList", Constants.ANALYSIS_STEP_LIST, list);
	}
}
