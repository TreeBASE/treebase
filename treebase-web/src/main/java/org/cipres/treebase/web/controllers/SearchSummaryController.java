
package org.cipres.treebase.web.controllers;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.MatrixRowService;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.Algorithm;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisService;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.AnalyzedMatrix;
import org.cipres.treebase.domain.study.AnalyzedTree;
import org.cipres.treebase.domain.study.DistanceAlgorithm;
import org.cipres.treebase.domain.study.LikelihoodAlgorithm;
import org.cipres.treebase.domain.study.OtherAlgorithm;
import org.cipres.treebase.domain.study.ParsimonyAlgorithm;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.model.AnalysisCommand;
import org.cipres.treebase.web.model.AnalysisStepCommand;
import org.cipres.treebase.web.model.AnalyzedDataCommand;
import org.cipres.treebase.web.model.StudyCommand;
import org.cipres.treebase.web.util.AnalyzedDataComparator;
import org.cipres.treebase.web.util.ControllerUtil;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for all pages that display a single study, 
 * or aspects of a single study, in the search area
 * 
 * @author mjd 20080718
 *
 */
class SearchSummaryController extends BaseFormController {
	StudyService studyService;
	MatrixService matrixService;
	PhyloTreeService phyloTreeService;
	AnalysisService analysisService;
	MatrixRowService matrixRowService;
	TaxonLabelService taxonLabelService;
	
	String defaultPage = null;
	private static final Logger LOGGER = Logger.getLogger(SearchSummaryController.class);
	
	class NoStudySpecifiedError extends Error { }
	class UnknownStudyError extends Error { }
	class RestrictedStudyError extends Error { }
	
	Study theStudy = null;
	CharacterMatrix theMatrix = null; // XXX What if it isn't a CharacterMatrix?
	PhyloTree theTree = null;
	Analysis theAnalysis = null;
	MatrixRow theRow = null;
	Taxon theTaxon = null;
	
	@Override
	protected Study formBackingObject(HttpServletRequest request)
			throws Exception {
		Map<String,String []> param = request.getParameterMap();
		LOGGER.info("in formBackingObject");
		theStudy = null;
		{
			Long studyID = getIDParam(param, "id");
			if (studyID == null) { throw new NoStudySpecifiedError(); }
			theStudy = getStudyService().findByID(studyID);
			if (theStudy == null) { throw new UnknownStudyError(); }
			LOGGER.debug("formBackingObject found study " + theStudy);
			if ( ! theStudy.isPublished() ) {
				// we assume access is denied, unless specifically granted!
				setAuthorizationChecked(false);
			}
			else {
				setAuthorizationChecked(true);
			}
		}
		
		theTree = null;
		if (param.containsKey("treeid"))
		{
			Long treeID = getIDParam(param, "treeid");
			theTree = getPhyloTreeService().findByID(treeID);
		}
		
		theMatrix = null;
		if (param.containsKey("matrixid"))
		{
			Long matrixID = getIDParam(param, "matrixid");
			Matrix m = getMatrixService().findByID(matrixID);
			if (m instanceof CharacterMatrix) {
				theMatrix = (CharacterMatrix) m;
			} 
		}
		
		theAnalysis = null;
		if (param.containsKey("analysisid"))
		{
			Long analysisID = getIDParam(param, "analysisid");
			theAnalysis = getAnalysisService().findByID(analysisID);
		}
		
		theRow = null;
		if (param.containsKey("matrixrowid")) {
			Long matrixRowID = getIDParam(param, "matrixrowid");
			theRow = getMatrixRowService().findByID(matrixRowID);
		}
		
		theTaxon = null;
		if ( param.containsKey("taxonid")) {
			Long taxonId = getIDParam(param,"taxonid");
			theTaxon = getTaxonLabelService().findTaxonByIDString(taxonId.toString());
		}
		
		return theStudy;
	}
	
	@Override
	protected Map<String,Object> referenceData(HttpServletRequest pRequest) throws Exception {
		LOGGER.info("in referenceData");
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
	
	/**
	 * @param params the parameter map for the current request, as returned by HttpServletRequest.getParameterMap
	 * @param paramName the name of the ID parameter to retrieve
	 * @return the ID number parameter with the specified name
	 * 
	 * Convenience function to fetch ID numbers from CGI query parameters.  
	 * If there are multiple parameters with the specified name, returns one at random;
	 * if none, returns null.
	 */
	private Long getIDParam(Map<String, String []> params, String paramName) {
		String [] IDParam = params.get(paramName);
		return IDParam.length == 0 ? null : Long.parseLong(IDParam[0]);
	}
	
	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException bindException)
			throws Exception {
		LOGGER.info("in processFormSubmission");
		LOGGER.debug("processFormSubmission found study " + (Study) command);
		return getConditionalModelAndView(request, showForm(request, response, bindException));
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors) throws Exception {
		String pageName = request.getServletPath();
		LOGGER.info("in showForm");
//		LOGGER.debug("GetServletPath returns " + pageName);
		if (pageName == null) {
			pageName = getDefaultPage();
		}
		if (pageName.endsWith(".html")) {
			pageName = pageName.substring(0, pageName.length() - 5);
		} else if (pageName.endsWith(".jsp")){
			pageName = pageName.substring(0, pageName.length() - 4);
		}
		
		LOGGER.debug("showForm has pagename = " + pageName);
		Map<String,Object> newModel = new HashMap<String, Object>(errors.getModel());
		// OK if these next three are null
		newModel.put("matrix", theMatrix);
		newModel.put("tree", theTree);
		newModel.put("analysis", theAnalysis);
		newModel.put("matrixrow", theRow);
		newModel.put("taxon", theTaxon);
		
		newModel.put("matrices", getMatrixService().findByStudy(theStudy));
		newModel.put("trees", getPhyloTreeService().findByStudy(theStudy));	
		
		Submission submission = theStudy.getSubmission();
		java.util.Date creationDate = submission.getCreateDate();
		
		if (creationDate != null) {
			String initiatedDate = DateFormat.getDateInstance(DateFormat.LONG).format(creationDate);
			newModel.put("initiatedDate", initiatedDate);
		} else {
			newModel.put("initiatedDate", null);
		}		
		
		newModel.put("submissionNumber", submission.getSubmissionNumber());
		newModel.put("studyStatus", theStudy.getStudyStatus().getDescription());	
		StudyCommand studyCommand = new StudyCommand();
		studyCommand.setStudy(theStudy);
		if (submission != null) {
			studyCommand.setSubmission_id(submission.getId());
		}
		List<Analysis> analysisList = theStudy.getAnalyses();
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
		newModel.put(Constants.STUDY_COMMAND_KEY, studyCommand);
		
		
		{
			List<TaxonLabel> taxa;
			
			if (theTree != null) {
				taxa = new ArrayList<TaxonLabel>(theTree.getAllTaxonLabels());
			} else if (theMatrix != null) {
				taxa = theMatrix.getAllTaxonLabels();
			} else {
				taxa = new ArrayList<TaxonLabel>(theStudy.getTaxonLabels());
			}
			Comparator<TaxonLabel> c = new TaxonLabelByLabelComparator();
			Collections.sort(taxa, c);
			newModel.put("taxa", taxa);
		}
		
		// XXX this nasty hack should be fixed
		// The trees.html controller should subclass this one
		if (pageName.endsWith("/tree") && theTree != null) {
			setupForPhyloWidget(request);
		}
		
//		setupMenus(request.getSession());
		
		// formbackingObject should have already set up the study
		// but what the heck, let's put it in just to be sure
		newModel.put("study", theStudy);

		return getConditionalModelAndView(request, new ModelAndView(pageName, newModel));
	}
	

	// XXX copied from DirectMapToPhyloWidget
	// The code should be shared
	private void setupForPhyloWidget(HttpServletRequest request) {
		// set up NEWICKSTRINGNAME and NEWICKSTRINGSMAP
		LOGGER.info("setupForPhyloWidget");
		String separator = "@";
		Map<String, String> treeMap = new HashMap<String, String>();
		String newickStringName = null;

		treeMap.put(getMapKey(theTree), theTree.getId() + separator + theTree.getNewickString());
		newickStringName = "A Tree";
		
		request.getSession().setAttribute("treeBlockID", theTree.getTreeBlock().getId());
		request.getSession().setAttribute("studyID", theTree.getStudy().getId());
		request.getSession().setAttribute("NEWICKSTRINGSMAP", treeMap);
		request.getSession().setAttribute("NEWICKSTRINGNAME", newickStringName);
	}
	
	/**
	 * @param pTree
	 * @return String with a complete tree's information
	 * XXX copied from DirectMapToPhyloWidget
	 * The code should be shared
	 */
	private String getMapKey(PhyloTree pTree) {
		LOGGER.info("in getMapKey");
		StringBuilder key = new StringBuilder("TreeId: " + pTree.getId());
		if (!TreebaseUtil.isEmpty(pTree.getLabel())) {
			key.append("|").append(pTree.getLabel());
		}
		if (!TreebaseUtil.isEmpty(pTree.getTitle())) {
			key.append("|").append(pTree.getTitle());
		}
		return key.toString();
	}

	@Override
	protected void onBind(HttpServletRequest request, Object command)
			throws Exception {
		LOGGER.debug("onBind says that the command object is " + (Study) command);
		super.onBind(request, command);
	}

	public String getDefaultPage() {
		LOGGER.info("in getDefaultPage");
		return defaultPage;
	}

	public void setDefaultPage(String defaultView) {
		LOGGER.info("in setDefaultPage");
		this.defaultPage = defaultView;
	}

	public StudyService getStudyService() {
		LOGGER.info("in getStudyService");
		return studyService;
	}

	public void setStudyService(StudyService studyService) {
		LOGGER.info("in setStudyService");
		this.studyService = studyService;
	}

	public MatrixService getMatrixService() {
		LOGGER.info("in getMatrixService");
		return matrixService;
	}

	public void setMatrixService(MatrixService matrixService) {
		LOGGER.info("in setMatrixService");
		this.matrixService = matrixService;
	}

	public MatrixRowService getMatrixRowService() {
		LOGGER.info("in getMatrixRowService");
		return matrixRowService;
	}

	public void setMatrixRowService(MatrixRowService matrixRowService) {
		LOGGER.info("in setMatrixRowService");
		this.matrixRowService = matrixRowService;
	}

	public PhyloTreeService getPhyloTreeService() {
		LOGGER.info("in getPhyloTreeService");
		return phyloTreeService;
	}

	public void setPhyloTreeService(PhyloTreeService phyloTreeService) {
		LOGGER.info("in setPhyloTreeService");
		this.phyloTreeService = phyloTreeService;
	}

	public AnalysisService getAnalysisService() {
		LOGGER.info("in getAnalysisService");
		return analysisService;
	}

	public void setAnalysisService(AnalysisService analysisService) {
		LOGGER.info("in setAnalysisService");
		this.analysisService = analysisService;
	}

	public TaxonLabelService getTaxonLabelService() {
		LOGGER.info("in getTaxonLabelService");
		return taxonLabelService;
	}

	public void setTaxonLabelService(TaxonLabelService taxonLabelService) {
		LOGGER.info("in setTaxonLabelService");
		this.taxonLabelService = taxonLabelService;
	}

	private class TaxonLabelByLabelComparator implements Comparator<TaxonLabel> {
		public int compare(TaxonLabel a, TaxonLabel b) {
			return a.getTaxonLabel().compareTo(b.getTaxonLabel());
		}	
	}
}