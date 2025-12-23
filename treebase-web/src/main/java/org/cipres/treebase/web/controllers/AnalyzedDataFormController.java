
package org.cipres.treebase.web.controllers;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.TreebaseUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.cipres.treebase.web.compat.AbstractWizardFormController;
import org.springframework.web.servlet.view.RedirectView;

import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.AnalysisService;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalysisStepService;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.AnalyzedDataService;
import org.cipres.treebase.domain.study.AnalyzedMatrix;
import org.cipres.treebase.domain.study.AnalyzedTree;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.model.AnalyzedDataCommand;
import org.cipres.treebase.web.model.EditFieldCommand;
import org.cipres.treebase.web.model.LabelValue;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * DataFormController.java
 * 
 * Created on June 21, 2006
 * 
 * @author lcchan
 * 
 */
public class AnalyzedDataFormController extends AbstractWizardFormController {
	private static final Logger LOGGER = LogManager.getLogger(AnalyzedDataFormController.class);

	private AnalysisService mAnalysisService;
	private StudyService mStudyService;
	private AnalysisStepService mAnalysisStepService;
	private MatrixService mMatrixService;
	private AnalyzedDataService mAnalyzedDataService;
	private PhyloTreeService mPhyloTreeService;

	/**
	 * Return the PhyloTreeService field.
	 * 
	 * @return PhyloTreeService mPhyloTreeService
	 */
	public PhyloTreeService getPhyloTreeService() {
		return mPhyloTreeService;
	}

	/**
	 * Set the PhyloTreeService field.
	 */
	public void setPhyloTreeService(PhyloTreeService pNewPhyloTreeService) {
		mPhyloTreeService = pNewPhyloTreeService;
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
	 * Return the MatrixService field.
	 * 
	 * @return MatrixService mMatrixService
	 */
	public MatrixService getMatrixService() {
		return mMatrixService;
	}

	/**
	 * Set the MatrixService field.
	 */
	public void setMatrixService(MatrixService pNewMatrixService) {
		mMatrixService = pNewMatrixService;
	}

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
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView processFinish(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) throws Exception {

		AnalyzedDataCommand data = (AnalyzedDataCommand) command;

		// get the analysis step we are dealing with
		String analysis_step_id = ServletRequestUtils.getStringParameter(
			request,
			"analysis_step_id",
			null);

		request.getSession().setAttribute("ANALYSIS_STEP_ID_FROM_ANALYZED_DATA", analysis_step_id);

		AnalysisStep analysisStep = mAnalysisStepService.findByID(Long.parseLong(analysis_step_id));

		// process each check box user has selected -- add to AnalyzedData table
		String dataType = data.getDataType();
		Boolean input = (data.getInputOutputType().equals(Constants.INPUT_KEY)) ? (true) : (false);

		if (dataType.equals(Constants.MATRIX_KEY)) {
			List<EditFieldCommand> matrixList = data.getMatrixList();
			for (int i = 0; i < matrixList.size(); i++) {
				EditFieldCommand editFieldCommand = matrixList.get(i);
				// check if user update title
				Matrix matrix = mMatrixService.findByID(editFieldCommand.getMatrix().getId());
				matrix.setTitle(editFieldCommand.getMatrix().getTitle());
				mMatrixService.update(matrix);
				if (!editFieldCommand.getChecked()) continue; // skip current iteration if not
				// checked
				AnalyzedMatrix analyzedMatrix = new AnalyzedMatrix();
				analyzedMatrix.setInput(input);
				analyzedMatrix.setMatrix(matrix);
				analysisStep.addAnalyzedData(analyzedMatrix);
				mAnalysisStepService.update(analysisStep);
			}// end for
		} else if (dataType.equals(Constants.TREE_KEY)) {
			List<EditFieldCommand> treeList = data.getTreeList();
			for (int i = 0; i < treeList.size(); i++) {
				EditFieldCommand editFieldCommand = treeList.get(i);
				// check if user update title
				PhyloTree phyloTree = mPhyloTreeService.findByID(editFieldCommand
					.getPhyloTree().getId());
				phyloTree.setLabel(editFieldCommand.getPhyloTree().getLabel());
				mPhyloTreeService.update(phyloTree);
				if (!editFieldCommand.getChecked()) {
					continue; // skip current iteration if not
				}
				// checked
				AnalyzedTree analyzedTree = new AnalyzedTree();
				analyzedTree.setInput(input);
				analyzedTree.setTree(phyloTree);
				analysisStep.addAnalyzedData(analyzedTree);
				mAnalysisStepService.update(analysisStep);
			}
		} else if ( dataType.equals(Constants.TREE_LIST)) {
			List<EditFieldCommand> treeBlockList = data.getTreeBlockList();
			LOGGER.warn("Going to set tree block as analyzed data");
			for ( int i = 0; i < treeBlockList.size(); i++ ) {
				EditFieldCommand editFieldCommand = treeBlockList.get(i);
				if ( ! editFieldCommand.getChecked() ) {
					continue;  // skip current iteration if not checked
				}				
				TreeBlock treeBlock = mPhyloTreeService.findTreeBlockByID(editFieldCommand.getTreeBlock().getId());
				if ( treeBlock == null ) {
					LOGGER.warn("No treeblock!!!");
				}
				else {
					LOGGER.warn(treeBlock.getTitle());
					Iterator<PhyloTree> treeIterator = treeBlock.getTreeListIterator();
					while (treeIterator.hasNext()) {
						PhyloTree phyloTree = treeIterator.next();
						AnalyzedTree analyzedTree = new AnalyzedTree();
						analyzedTree.setInput(input);
						analyzedTree.setTree(phyloTree);
						analysisStep.addAnalyzedData(analyzedTree);
						mAnalysisStepService.update(analysisStep);
					}
					/*
					AnalyzedTreeBlock analyzedTreeBlock = new AnalyzedTreeBlock();
					analyzedTreeBlock.setInput(input);
					analyzedTreeBlock.setTreeBlock(treeBlock);
					analysisStep.addAnalyzedData(analyzedTreeBlock);
					mAnalysisStepService.update(analysisStep);
					*/					
				}
			}
		}
		return getNextView();
	}

	/**
	 * Determine next jsp to forward to - if user selects type = Tree ->
	 * analyzedDataForm-treeSeleciton.jsp - if user selects type = Matrix ->
	 * analyzedDataForm-matrixSeleciton.jsp
	 * 
	 * I separate these into different page in case if we want to show different fields based (right
	 * now, we are only show title and id fields)
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#getTargetPage(javax.servlet.http.HttpServletRequest,
	 *      java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected int getTargetPage(
		HttpServletRequest request,
		Object command,
		Errors errors,
		int currentPage) {

		AnalyzedDataCommand analyzedDataCommand = (AnalyzedDataCommand) command;
		String dataType = analyzedDataCommand.getDataType();
		int nextPage = 0;

		if (currentPage == 0) {
			if (dataType.equals(Constants.TREE_KEY)) {
				nextPage = 2;
			} 
			else if (dataType.equals(Constants.MATRIX_KEY)) {
				nextPage = 1;
			}
			else if ( dataType.equals(Constants.TREE_LIST)) {
				nextPage = 3;
			}
		}
		return nextPage;
	}

	/**
	 * convert Long id from String to Long and vice versa
	 * 
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest,
	 *      org.springframework.web.bind.ServletRequestDataBinder)
	 */
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {

		NumberFormat nf = NumberFormat.getNumberInstance();
		// convert java.lang.Long
		binder.registerCustomEditor(Long.class, null, new CustomNumberEditor(Long.class, nf, true));
	}

	/**
	 * 
	 * 
	 * @param request
	 * @return
	 */
	@Override
	protected Map<String,List<LabelValue>> referenceData(HttpServletRequest request, int page) throws Exception {

		Map<String,List<LabelValue>> dataMap = new HashMap<String,List<LabelValue>>();

		List<LabelValue> steps = new ArrayList<LabelValue>();
		// determine the analysis step name for the drop down list
		String analysis_step_id = ServletRequestUtils.getStringParameter(
			request,
			"analysis_step_id",
			null);

		AnalysisStep analysisStep = mAnalysisStepService.findByID(Long.parseLong(analysis_step_id));
		// add it to the drop down list
		steps.add(new LabelValue(analysisStep.getDisplayName(), analysis_step_id));
		dataMap.put("steps", steps);

		// determine input/output type
		List<LabelValue> inputOutputTypes = new ArrayList<LabelValue>();
		inputOutputTypes.add(new LabelValue("Input", Constants.INPUT_KEY));
		inputOutputTypes.add(new LabelValue("Output", Constants.OUTPUT_KEY));
		dataMap.put("inputOutputTypes", inputOutputTypes);

		// determine tree/matrix type
		List<LabelValue> dataTypes = new ArrayList<LabelValue>();
		dataTypes.add(new LabelValue("Matrix", Constants.MATRIX_KEY));
		dataTypes.add(new LabelValue("Tree", Constants.TREE_KEY));
		dataTypes.add(new LabelValue("Tree Block", Constants.TREE_LIST));
		dataMap.put("dataTypes", dataTypes);

		return dataMap;
	}

	/**
	 * 
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	public Object formBackingObject(HttpServletRequest request) throws ServletException {

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("In FormbackingObject");
		}

		Study study = ControllerUtil.findStudy(request, mStudyService);
		Submission submission = (Submission) study.getSubmission();

		AnalyzedDataCommand command = new AnalyzedDataCommand();
		String analysis_step_id = ServletRequestUtils.getStringParameter(
			request,
			"analysis_step_id",
			null);
		if (TreebaseUtil.isEmpty(analysis_step_id)) {
			return command;
		}
		command.setStep(analysis_step_id);

		// retrieve list of matrix IDs user has previously selected and store in them for lookup
		AnalysisStep analysisStep = mAnalysisStepService.findByID(Long.parseLong(analysis_step_id));
		List<AnalyzedData> selectedData = analysisStep.getDataSetReadOnly();
		Map<Long, Boolean> selectedMatrices = new HashMap<Long, Boolean>();
		Map<Long, Boolean> selectedTrees = new HashMap<Long, Boolean>();
		Map<Long, Boolean> selectedTreeBlocks = new HashMap<Long,Boolean>();
		Map<Long, Boolean> incompleteTreeBlocks = new HashMap<Long,Boolean>();

		for (AnalyzedData data : selectedData) {
			if (data instanceof AnalyzedMatrix) {
				AnalyzedMatrix analyzedMatrix = (AnalyzedMatrix) data;
				selectedMatrices.put(analyzedMatrix.getMatrix().getId(), analyzedMatrix
					.isInputData());
			} else if (data instanceof AnalyzedTree) {
				AnalyzedTree analyzedTree = (AnalyzedTree) data;
				selectedTrees.put(analyzedTree.getTree().getId(), analyzedTree.isInputData());
				TreeBlock treeBlock = analyzedTree.getTree().getTreeBlock();
				Long tbID = treeBlock.getId();
				if ( ! selectedTreeBlocks.containsKey(tbID) && ! incompleteTreeBlocks.containsKey(tbID) ) {
					int seen = 0;
					for ( AnalyzedData innerData : selectedData ) {
						if ( innerData instanceof AnalyzedTree ) {							
							Iterator<PhyloTree> treeIterator = treeBlock.getTreeListIterator();
							FIND: while (treeIterator.hasNext()) {
								PhyloTree treeInBlock = treeIterator.next();
								if ( treeInBlock.getId() == ((AnalyzedTree)innerData).getTree().getId() ) {
									seen++;
									break FIND;
								}
							}
						}
					}
					if ( seen == treeBlock.getTreeCount() ) {
						selectedTreeBlocks.put(tbID, true);
					}
					else {
						incompleteTreeBlocks.put(tbID, true);
					}
				}
			}
		}
		// get list of all the matrices and set check = true if it's already been selected
		List<EditFieldCommand> matrixList = new ArrayList<EditFieldCommand>();
		Collection<Matrix> matrices = submission.getSubmittedMatricesReadOnly();
		for (Matrix matrix : matrices) {
			Long id = matrix.getId();
			String selected = new String();
			if (selectedMatrices.containsKey(id)) {
				selected = selectedMatrices.get(id)
					? (Constants.INPUT_KEY)
					: (Constants.OUTPUT_KEY);
			}
			EditFieldCommand editFieldCommand = new EditFieldCommand(matrix, selected);
			matrixList.add(editFieldCommand);
		}
		command.setMatrixList(matrixList);

		// do the same for trees
		// Iterator<PhyloTree> trees = submission.getTreeIterator();
		List<EditFieldCommand> treeList = new ArrayList<EditFieldCommand>();
		Collection<PhyloTree> trees = submission.getAllSubmittedTrees();
		for (PhyloTree tree : trees) {
			Long id = tree.getId();
			String selected = new String();
			if (selectedTrees.containsKey(id)) {
				selected = selectedTrees.get(id) ? (Constants.INPUT_KEY) : (Constants.OUTPUT_KEY);
			}
			EditFieldCommand editFieldCommand = new EditFieldCommand(tree, selected);
			treeList.add(editFieldCommand);
		}
		command.setTreeList(treeList);
		
		List<EditFieldCommand> treeBlockList = new ArrayList<EditFieldCommand>();
		Collection<TreeBlock> treeBlocks = submission.getSubmittedTreeBlocksReadOnly();
		for (TreeBlock treeBlock : treeBlocks) {
			Long id = treeBlock.getId();
			String selected = new String();
			if ( selectedTreeBlocks.containsKey(id)) {
				selected = selectedTreeBlocks.get(id) ? (Constants.INPUT_KEY) : (Constants.OUTPUT_KEY);
			}
			EditFieldCommand editFieldCommand = new EditFieldCommand(treeBlock,selected);
			treeBlockList.add(editFieldCommand);
		}
		command.setTreeBlockList(treeBlockList);

		return command;
	}

	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView processCancel(
		HttpServletRequest pRequest,
		HttpServletResponse pResponse,
		Object pCommand,
		BindException pErrors) throws Exception {
		return getNextView();
	}

	/**
	 * 
	 * @return
	 */
	private ModelAndView getNextView() {
		return new ModelAndView(new RedirectView("analysisDisplay.html"));
		// return new ModelAndView(new RedirectView("analyzedDataList.html"+"?id=" +
		// analysis_step_id));
	}
}