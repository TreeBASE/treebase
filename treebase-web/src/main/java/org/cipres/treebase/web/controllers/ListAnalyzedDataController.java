
package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalysisStepService;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.AnalyzedMatrix;
import org.cipres.treebase.domain.study.AnalyzedTree;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.model.AnalyzedDataCommand;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * ListDataController.java
 * 
 * Created on June 21, 2006
 * 
 * @author lcchan
 * 
 */
public class ListAnalyzedDataController implements Controller {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(ListAnalyzedDataController.class);

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
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String analysis_step_id = ServletRequestUtils.getStringParameter(request, "id", null);
		AnalysisStep analysisStep = mAnalysisStepService.findByID(Long.parseLong(analysis_step_id));
		List<AnalyzedData> dataset = analysisStep.getDataSetReadOnly();
		// map AnalyzedData -> AnalyzedDataCommand
		AnalyzedDataCommand analyzedDataCommand;

		List<AnalyzedDataCommand> list = new ArrayList<AnalyzedDataCommand>();
		for (AnalyzedData data : dataset) {
			analyzedDataCommand = new AnalyzedDataCommand();
			String inputOutput = (data.isInputData()) ? ("Input") : ("Output");
			analyzedDataCommand.setInputOutputType(inputOutput);

			if (data instanceof AnalyzedMatrix) {
				AnalyzedMatrix analyzedMatrix = (AnalyzedMatrix) data;
				analyzedDataCommand.setDataType(Constants.MATRIX_KEY);
				analyzedDataCommand.setDisplayName(analyzedMatrix.getMatrix().getTitle());
				analyzedDataCommand.setId(analyzedMatrix.getId());
				analyzedDataCommand.setDataId(analyzedMatrix.getMatrix().getId());
			} else if (data instanceof AnalyzedTree) {
				AnalyzedTree analyzedTree = (AnalyzedTree) data;
				analyzedDataCommand.setDataType(Constants.TREE_KEY);
				analyzedDataCommand.setDisplayName(analyzedTree.getTree().getLabel());
				analyzedDataCommand.setId(analyzedTree.getId());
				analyzedDataCommand.setDataId(analyzedTree.getTree().getId());
			}
			list.add(analyzedDataCommand);
		}

		// save the analysis step object user has selected
		ControllerUtil.saveAnalysisStep(request, analysisStep);
		return new ModelAndView("analyzedDataList", "analyzedDataList", list);
	}
}
