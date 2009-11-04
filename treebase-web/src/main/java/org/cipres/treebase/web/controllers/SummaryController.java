package org.cipres.treebase.web.controllers;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cipres.treebase.TreebaseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.study.Algorithm;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.AnalyzedMatrix;
import org.cipres.treebase.domain.study.AnalyzedTree;
import org.cipres.treebase.domain.study.ArticleCitation;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.DistanceAlgorithm;
import org.cipres.treebase.domain.study.LikelihoodAlgorithm;
import org.cipres.treebase.domain.study.OtherAlgorithm;
import org.cipres.treebase.domain.study.ParsimonyAlgorithm;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.model.AnalysisCommand;
import org.cipres.treebase.web.model.AnalysisStepCommand;
import org.cipres.treebase.web.model.AnalyzedDataCommand;
import org.cipres.treebase.web.model.StudyCommand;
import org.cipres.treebase.web.util.AnalyzedDataComparator;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * SummaryController.java
 * 
 * Created on August 15, 2007
 * 
 * @author Madhu
 * 
 */

public class SummaryController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(SummaryController.class);

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

	private SubmissionService mSubmissionService;

	private SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	/**
	 * Set the SubmissionService field.
	 */
	public void setSubmissionService(SubmissionService pNewSubmissionService) {
		mSubmissionService = pNewSubmissionService;
	}

	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) throws Exception {

		return new ModelAndView(getSuccessView());
	}

	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		if ( request.getRequestURI().equals("/treebase-web/user/analyses.html") ) {
			setFormView("analyses");
		}
		else {
			setFormView("submissionSummaryView");
		}
		String requestURI = request.getRequestURI();
		String username = request.getRemoteUser();
		String submission_id = ServletRequestUtils.getStringParameter(request, "id", null);
		Study study;
		if (TreebaseUtil.isEmpty(submission_id)) {
			study = ControllerUtil.findStudy(request, mStudyService);
			setAuthorizationChecked(true);// This is needed in case one is clicking at Summary
			// link at the bottom of the menu list on the right hand
			// side.
		} else {
			study = mStudyService.findBySubmissionID(Long.parseLong(submission_id));
		}

		if (!TreebaseUtil.isEmpty(submission_id)) {

			TBPermission perm2 = getSubmissionService().getPermission(
				username,
				Long.parseLong(submission_id));
			if (perm2 == TBPermission.WRITE || perm2 == TBPermission.READ_ONLY
				|| perm2 == TBPermission.SUBMITTED_WRITE) {
				setAuthorizationChecked(true);
			} else {
				setAuthorizationChecked(false);
				return new ArticleCitation();
			}

			ControllerUtil.saveStudy(request, study); // user has made selection
			// Added by Madhu to set the session variables
			if (study.isReady()) {
				request.getSession().setAttribute("publicationState", "Ready");
			} else if (study.isPublished()) {
				request.getSession().setAttribute("publicationState", "Published");
			} else {
				request.getSession().setAttribute("publicationState", "NotReady");
			}

		} else {
			request.getSession().setAttribute("publicationState", "NotReady");
		}

		Citation citation = study.getCitation();
		if (citation == null) {
			return new ArticleCitation();
		} else {
			return citation;
		}
	}

	@Override
	protected Map<String,Object> referenceData(HttpServletRequest pRequest) throws Exception {

		Map<String,Object> resultMap = new HashMap<String,Object>();
		Study study = ControllerUtil.findStudy(pRequest, getStudyService());

		Submission submission = study.getSubmission();
		java.util.Date creationDate = submission.getCreateDate();

		if (creationDate != null) {
			String initiatedDate = DateFormat.getDateInstance(DateFormat.LONG).format(creationDate);
			resultMap.put("initiatedDate", initiatedDate);
		} else {
			resultMap.put("initiatedDate", null);
		}

		resultMap.put("submissionNumber", submission.getSubmissionNumber());
		resultMap.put("studyStatus", study.getStudyStatus().getDescription());

		// Collection<Analysis> anacoll = study.getAnalysesReadOnly();
		// resultMap.put("analysisCollection", anacoll);

		// This line and below, I have taken the code from Lucie's
		// DisplayAnalysisController's handleRequest method. The Analysis shown here
		// as part of the summary is essentially identical to the Analysis section details as read
		// only mode.
		StudyCommand studyCommand = new StudyCommand();
		// copy study information
		studyCommand.setStudy(study);
		// study from TBI do not contain submission_id
		if (submission != null) {
			studyCommand.setSubmission_id(submission.getId());
		}

		List<Analysis> analysisList = study.getAnalyses();
		List<AnalysisCommand> analysisCommandList = new ArrayList<AnalysisCommand>();

		for (Analysis analysis : analysisList) {

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
				} else if (algorithm instanceof DistanceAlgorithm) {
					algorithmType = Constants.ALGORITHM_DISTANCE;
				} else if (algorithm instanceof OtherAlgorithm) {
					algorithmType = Constants.ALGORITHM_OTHER;
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
		studyCommand.setAnalysisCommandList(analysisCommandList);
		//
		resultMap.put(Constants.STUDY_COMMAND_KEY, studyCommand);
		return resultMap;
	}
}
