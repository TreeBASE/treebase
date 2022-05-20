
package org.cipres.treebase.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * ListAnalysisController.java
 * 
 * Created on Jun 6, 2006
 * @author lcchan
 *
 */
public class ListAnalysisController implements Controller {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = LogManager.getLogger(ListAnalysisController.class);

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
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
									  HttpServletResponse response) throws Exception {
		
		Study study = ControllerUtil.findStudy(request, mStudyService);
		List<Analysis> analysisList = study.getAnalyses();
		return new ModelAndView("analysisList", Constants.ANALYSIS_LIST, analysisList);
	}	
}
