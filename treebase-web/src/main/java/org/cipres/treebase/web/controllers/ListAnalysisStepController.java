
package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private static final Logger LOGGER = LogManager.getLogger(ListAnalysisStepController.class);

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
