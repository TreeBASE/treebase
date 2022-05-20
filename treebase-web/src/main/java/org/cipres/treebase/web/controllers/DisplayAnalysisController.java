
package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.domain.study.Algorithm;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.AnalyzedMatrix;
import org.cipres.treebase.domain.study.AnalyzedTree;
import org.cipres.treebase.domain.study.BayesianAlgorithm;
import org.cipres.treebase.domain.study.EvolutionAlgorithm;
import org.cipres.treebase.domain.study.JoiningAlgorithm;
import org.cipres.treebase.domain.study.LikelihoodAlgorithm;
import org.cipres.treebase.domain.study.OtherAlgorithm;
import org.cipres.treebase.domain.study.ParsimonyAlgorithm;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.UPGMAAlgorithm;
import org.cipres.treebase.framework.ExecutionResult;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.model.AnalysisCommand;
import org.cipres.treebase.web.model.AnalysisStepCommand;
import org.cipres.treebase.web.model.AnalyzedDataCommand;
import org.cipres.treebase.web.model.StudyCommand;
import org.cipres.treebase.web.util.AnalyzedDataComparator;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * DisplayAnalysisController.java
 * 
 * Created on July 28, 2006
 * 
 * @author lcchan
 * 
 */
public class DisplayAnalysisController implements Controller {
	private static final Logger LOGGER = LogManager.getLogger(DisplayAnalysisController.class);

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

		Study study = ControllerUtil.findStudy(request, mStudyService);
		Submission submission = (Submission) study.getSubmission();

		StudyCommand studyCommand = new StudyCommand();
		// copy study information
		studyCommand.setStudy(study);
		// study from TBI do not contain submission_id
		if (submission != null) {
			studyCommand.setSubmission_id(submission.getId());
		}

		List<Analysis> analysisList = study.getAnalyses();
		List<AnalysisCommand> analysisCommandList = new ArrayList<AnalysisCommand>();
		List<Analysis> changedAnalyses = new ArrayList<Analysis>();

		StringBuilder errBuilder = new StringBuilder();
		for (Analysis analysis : analysisList) {
			// FIXME: next if block needs to me moved to the onSumbit method when
			// we this controller will extend BaseFormController.
			if (!analysis.getValidated()) {
				ExecutionResult result = analysis.validate();
				if (!result.isSuccessful()) {
					errBuilder.append(result.getErrorMessage());
				}
				
				if (analysis.getValidated()) {
					// save to db if the validated flag is updated:
					changedAnalyses.add(analysis);
				}
			}
			
			//FIXME: display err message in GUI
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(errBuilder.toString()); //$NON-NLS-1$
			}
			
			//
			// Analysis
			AnalysisCommand analysisCommand = new AnalysisCommand();
			BeanUtils.copyProperties(analysisCommand, analysis);

			// Analysis Steps for Analysis and add algorithm type
			List<AnalysisStep> analysisStepList = analysis.getAnalysisStepsReadOnly();
			List<AnalysisStepCommand> analysisStepCommandList = new ArrayList<AnalysisStepCommand>();

			for (AnalysisStep analysisStep : analysisStepList) {
				AnalysisStepCommand analysisStepCommand = new AnalysisStepCommand();
				BeanUtils.copyProperties(analysisStepCommand, analysisStep);
				// analysisStepCommand.setId(analysisStep.getId());
				// analysisStepCommand.setSoftwareInfo(analysisStep.getSoftwareInfo());
				Algorithm algorithm = analysisStep.getAlgorithmInfo();
				String algorithmType = new String();
				if (algorithm instanceof LikelihoodAlgorithm) {
					algorithmType = Constants.ALGORITHM_LIKELIHOOD;
				} else if (algorithm instanceof ParsimonyAlgorithm) {
					algorithmType = Constants.ALGORITHM_PARSIMONY;
				}  else if (algorithm instanceof OtherAlgorithm) {
					algorithmType = Constants.ALGORITHM_OTHER;
				}else if (algorithm instanceof BayesianAlgorithm) {
					algorithmType = Constants.ALGORITHM_Bayesian;
				} else if (algorithm instanceof EvolutionAlgorithm) {
					algorithmType = Constants.ALGORITHM_Evolution;
				} else if (algorithm instanceof JoiningAlgorithm) {
					algorithmType = Constants.ALGORITHM_Joining;
			} else if (algorithm instanceof UPGMAAlgorithm) {
				algorithmType = Constants.ALGORITHM_UPGMA;	
			}
				// add algorithm type for analysisStepCommand
				analysisStepCommand.setAlgorithmType(algorithmType);

				// analyzed data for each analysis step
				List<AnalyzedData> analyzedDataSet = analysisStep.getDataSetReadOnly();
				List<AnalyzedDataCommand> analyzedDataCommandList = new ArrayList<AnalyzedDataCommand>();

				// Matrix or Tree?
				for (AnalyzedData analyzedData : analyzedDataSet) {

					AnalyzedDataCommand analyzedDataCommand = new AnalyzedDataCommand();
					BeanUtils.copyProperties(analyzedDataCommand, analyzedData);
					String inputOutput = (analyzedData.isInputData()) ? ("Input") : ("Output");
					analyzedDataCommand.setInputOutputType(inputOutput);

					if (analyzedData instanceof AnalyzedMatrix) {
						AnalyzedMatrix analyzedMatrix = (AnalyzedMatrix) analyzedData;
						analyzedDataCommand.setDataType(Constants.MATRIX_KEY);
						analyzedDataCommand.setDisplayName(analyzedMatrix.getMatrix().getTitle());
						analyzedDataCommand.setId(analyzedMatrix.getId());
						analyzedDataCommand.setDataId(analyzedMatrix.getMatrix().getId());
					} else if (analyzedData instanceof AnalyzedTree) {
						AnalyzedTree analyzedTree = (AnalyzedTree) analyzedData;
						analyzedDataCommand.setDataType(Constants.TREE_KEY);
						analyzedDataCommand.setDisplayName(analyzedTree.getTree().getLabel());
						analyzedDataCommand.setId(analyzedTree.getId());
						analyzedDataCommand.setDataId(analyzedTree.getTree().getId());
					}

					analyzedDataCommandList.add(analyzedDataCommand);
				} // end for
				// add analyzedData for analysisStepCommand

				Collections.sort(analyzedDataCommandList, new AnalyzedDataComparator());

				analysisStepCommand.setAnalyzedDataCommandList(analyzedDataCommandList);
				analysisStepCommandList.add(analysisStepCommand);
			}
			analysisCommand.setAnalysisStepCommandList(analysisStepCommandList);
			analysisCommandList.add(analysisCommand);
		}

		getStudyService().updateCollection(changedAnalyses);

		studyCommand.setAnalysisCommandList(analysisCommandList);

		return new ModelAndView("analysisSection", Constants.STUDY_COMMAND_KEY, studyCommand);
	}

}
