
package org.cipres.treebase.web.controllers;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.cipres.treebase.TreebaseUtil;
import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalysisStepService;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.AnalyzedDataService;

/**
 * UpdateAnalyzedDataListController.java
 * 
 * Created on June 24, 2006
 * 
 * @author lcchan
 * 
 */
public class UpdateAnalyzedDataListController implements Controller {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(UpdateAnalyzedDataListController.class);

	private AnalyzedDataService mAnalyzedDataService;
	private AnalysisStepService mAnalysisStepService;

	/**
	 * Return the AnalysisStepService field.
	 * 
	 * @return AnalysisStepService mAnalysisStepService
	 */
	public AnalysisStepService getAnalysisStepService() {
		return mAnalysisStepService;
	}

	/**
	 * Set the AnalysisStepService field.
	 */
	public void setAnalysisStepService(AnalysisStepService pNewAnalysisStepService) {
		mAnalysisStepService = pNewAnalysisStepService;
	}

	/**
	 * Return the AnalyzedDataService field.
	 * 
	 * @return AnalyzedDataService mAnalyzedDataService
	 */
	public AnalyzedDataService getAnalyzedDataService() {
		return mAnalyzedDataService;
	}

	/**
	 * Set the AnalyzedDataService field.
	 */
	public void setAnalyzedDataService(AnalyzedDataService pNewAnalyzedDataService) {
		mAnalyzedDataService = pNewAnalyzedDataService;
	}

	/**
	 * Return the AnalyzedDataService field.
	 * 
	 * @return AnalyzedDataService mAnalyzedDataService
	 */

	/**
	 * There maybe a better way to delete an AnalyzedData object if the service layer provides a
	 * different API.
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		// figure out which analysis step we are dealing with
		String analysis_step_id = ServletRequestUtils.getStringParameter(
			request,
			"analysis_step_id",
			null);
		String analyzed_id = ServletRequestUtils.getStringParameter(request, "id", null);
		String successView = "analysisDisplay.html";

		if (TreebaseUtil.isEmpty(analyzed_id)) {
			return new ModelAndView(new RedirectView(successView));
		}
		// TODO:
		// is there a better way to delete this with either matrix_id or analyzed_matrix_id?
		AnalysisStep analysisStep = mAnalysisStepService.findByID(Long.parseLong(analysis_step_id));
		Collection<AnalyzedData> analyzedSet = analysisStep.getDataSetReadOnly();

		for (AnalyzedData data : analyzedSet) {
			if (data.getId().equals(Long.parseLong(analyzed_id))) {
				mAnalyzedDataService.deleteAnalyzedData(data);
				break;
			}
		}
		return new ModelAndView(new RedirectView(successView));
	}

}
