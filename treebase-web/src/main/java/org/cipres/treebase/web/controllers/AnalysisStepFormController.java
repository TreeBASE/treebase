
package org.cipres.treebase.web.controllers;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.TreebaseUtil;

import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.study.Algorithm;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisService;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalysisStepHome;
import org.cipres.treebase.domain.study.AnalysisStepService;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.Software;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.web.model.AnalysisStepCommand;
import org.cipres.treebase.web.model.AnalyzedDataCommand;
import org.cipres.treebase.web.model.EditFieldCommand;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * AnalysisStepFormController.java
 * 
 * Created on June 6, 2006
 * 
 * @author lcchan
 * 
 * modified by
 * @author Jin Ruan
 * @author Madhu
 * 
 */
public class AnalysisStepFormController extends BaseFormController {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(AnalysisStepFormController.class);

	private StudyService mStudyService;
	private AnalysisService mAnalysisService;
	private AnalysisStepService mAnalysisStepService;

	private SubmissionService mSubmissionService;
	private AnalysisStepHome mAnalysisStepHome;

	/**
	 * Return the AnalysisStepHome field.
	 * 
	 * @return AnalysisStepHome mAnalysisStepHome
	 */
	private AnalysisStepHome getAnalysisStepHome() {
		return mAnalysisStepHome;
	}

	/**
	 * Set the AnalysisStepHome field.
	 */
	public void setAnalysisStepHome(AnalysisStepHome pNewAnalysisStepHome) {
		mAnalysisStepHome = pNewAnalysisStepHome;
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
	 * Return the AnalysisService field.
	 * 
	 * @return AnalysisService mAnalysisService
	 */
	public AnalysisService getAnalysisService() {
		return mAnalysisService;
	}

	private SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	/**
	 * Set the SubmissionService field.
	 */
	public void setSubmissionService(SubmissionService pNewSubmissionService) {
		mSubmissionService = pNewSubmissionService;
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

	protected ModelAndView processFormSubmission(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) throws Exception {

		System.out.println("Calling processformSubmission()");

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Calling .... processformSubmission()");
		}
		if ((request.getParameter(ACTION_SUBMIT) == null)
			&& (request.getParameter(ACTION_UPDATE) == null)
			&& (request.getParameter(ACTION_DELETE) == null)
			&& (request.getParameter(ACTION_CANCEL) == null)) {

			return showForm(request, response, errors);
		} else return onSubmit(request, response, command, errors);
	}

	/**
	 * 
	 * 
	 * @param request
	 * @return boolean
	 */
	protected boolean isFormChangeRequest(HttpServletRequest request, Object command) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("++++++++++++++++ isFormChangeRequest() is called");
		}
		return ((request.getParameter(ACTION_SUBMIT) == null)
			&& (request.getParameter(ACTION_UPDATE) == null) && (request
			.getParameter(ACTION_DELETE) == null));

	}
	
	private ModelAndView updateAnalysisStep(
			AnalysisStepCommand analysisStepCommand,
			AnalysisStep analysisStep,
			HttpServletRequest request,
			HttpServletResponse response,
			BindException myerrors) throws Exception {
		
		BeanUtils.copyProperties(analysisStepCommand, analysisStep);
		Software s = analysisStepCommand.getSoftwareInfo();
		if (TreebaseUtil.isEmpty(s.getName())) {
			s = null;
		} else {
			getAnalysisService().save(s);
			s = getAnalysisService().update(s);
		}	
		analysisStep.setSoftwareInfo(s);
		analysisStepCommand.setSoftwareInfo(s);
		String algorithmType = analysisStepCommand.getAlgorithmType();
		Algorithm algorithm = analysisStepCommand.getAlgorithmMap(algorithmType);
		if ( algorithm != null ) {
			if (algorithmType.startsWith("other") && algorithm.getDescription().trim().length() == 0) {				
				request.setAttribute("errors", "New Algorithm cannot be empty");
				return showForm(request, response, myerrors);
			}		
			if (algorithm != null && !algorithmType.startsWith("other") ) {
				algorithm.setDescription(algorithmType);
			}	
			if (request.getParameter(ACTION_SUBMIT) != null || algorithm.getId() == null ) {
				algorithm = getAnalysisService().update(algorithm);
			}
			analysisStep.setAlgorithmInfo(algorithm);
		}
		if ( algorithmType.startsWith("other") && algorithm == null ) {				
			request.setAttribute("errors", "New Algorithm cannot be empty");
			return showForm(request, response, myerrors);
		}
		return null;
	}

	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException myerrors) throws Exception {

		AnalysisStepCommand analysisStepCommand = (AnalysisStepCommand) command;
		String redirectView = request.getParameter("redirect");
		if (request.getParameter(ACTION_SUBMIT) != null) {

			// TODO: persist the analysisStep first since we need the analysis step id later.
			// It is better to move the analysis step creation code to analysisStepService.create()
			// method and return the analysis step id.
			AnalysisStep analysisStep = new AnalysisStep();
			ModelAndView resultView = updateAnalysisStep(analysisStepCommand,analysisStep,request,response,myerrors);
			if ( resultView != null ) {
				return resultView;
			}

			// determine which analysis we are dealing with
			String analysis_id = ServletRequestUtils.getStringParameter(request,"analysis_id",null);
			Analysis analysis = mAnalysisService.findByID(Long.parseLong(analysis_id));
			analysis.addAnalysisStep(analysisStep);

			mAnalysisService.update(analysis);
			if ( redirectView == null ) {
				return new ModelAndView("redirect:/user/analyzedDataForm.html" + "?analysis_step_id="
					+ analysisStep.getId().toString());
			}
			else {
				return new ModelAndView(redirectView);
			}
		} else if (request.getParameter(ACTION_UPDATE) != null) {
			AnalysisStep analysisStep = mAnalysisStepService.findByID(analysisStepCommand.getId());
			ModelAndView resultView = updateAnalysisStep(analysisStepCommand,analysisStep,request,response,myerrors);
			if ( resultView != null ) {
				return resultView;
			}
			
			mAnalysisStepService.update(analysisStep);

		} else if (request.getParameter(ACTION_DELETE) != null) {

			AnalysisStep analysisStep = mAnalysisStepService.findByID(analysisStepCommand.getId());
			mAnalysisStepService.deleteAnalysisStep(analysisStep);
			if ( redirectView == null ) {
				return new ModelAndView(getSuccessView());
			}
			else {
				return new ModelAndView(redirectView);
			}
		}

		if ( redirectView == null ) {
			return new ModelAndView(getSuccessView());
		}
		else {
			return new ModelAndView(redirectView);
		}
		// construct html for the next view
		// String nextView = "analysisStepList.html" + "?id=" + analysis_id.toString();
		// return new ModelAndView(new RedirectView(nextView));

	}

	/**
	 * Retrieve Analysis Step object we are working with. Create a new object if it's a new form
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Calling formBackingObject()");
		}

		String username = request.getRemoteUser();

		long study_id = ControllerUtil.getStudyId(request);
		Long submissionId = null;

		Study study = getStudyService().findByID(study_id);
		if (study != null) {
			Submission sub = study.getSubmission();
			if (sub != null) {
				submissionId = sub.getId();
			}
		}

		// FIXME: move the method to studyService, handle the case study w/o submission
		TBPermission perm = getSubmissionService().getPermission(username, submissionId);

		/* retrieve analysis step id */
		String analysisStep_id = ServletRequestUtils.getStringParameter(request, "id", null);
		
		request.getSession().setAttribute("analysisStep_id", analysisStep_id);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ANALYSIS Step ID = " + analysisStep_id + " study id = " + study_id);
		}

		List<String> uniqueAlgorithmDescriptions = getAnalysisStepService()
			.findUniqueAlgorithmDescriptions();

		AnalysisStepCommand analysisStepCommand = new AnalysisStepCommand();
		analysisStepCommand.setUniqueAlgorithmDescriptions(uniqueAlgorithmDescriptions);

		if (TreebaseUtil.isEmpty(analysisStep_id)) {
			if (perm == TBPermission.WRITE) {
				LOGGER.info("setAuthorizationChecked(true)");
				setAuthorizationChecked(true);
				return analysisStepCommand;
			} else {
				LOGGER.info("setAuthorizationChecked(false)");
				setAuthorizationChecked(false);
				return null;
			}
		} else {
			AnalysisStep analysisStep = mAnalysisStepService.findByID(Long
				.parseLong(analysisStep_id));
			Long analysis_id = analysisStep.getAnalysis().getId();

			TBPermission perm2 = getAnalysisService().getPermission(username, analysis_id);
			if (perm2 == TBPermission.WRITE || perm2 == TBPermission.READ_ONLY
				|| perm2 == TBPermission.SUBMITTED_WRITE) {

				BeanUtils.copyProperties(analysisStep, analysisStepCommand);
				Software software = analysisStep.getSoftwareInfo();
				analysisStepCommand.setSoftwareInfo(software);
				Algorithm algorithm = analysisStep.getAlgorithmInfo();
				if ( algorithm != null ) {
					analysisStepCommand.setAlgorithmType(algorithm.getAlgorithmType());
					analysisStepCommand.setAlgorithmMap(algorithm.getAlgorithmType(), algorithm);
				}
				List<AnalyzedDataCommand> analyzedDataCommandList = createAnalyzedDataCommandList(analysisStep);
				
				analysisStepCommand.setAnalyzedDataCommandList(analyzedDataCommandList);
				
				// save analysis step object
				ControllerUtil.saveAnalysisStep(request, analysisStep);

				if (perm2 == TBPermission.READ_ONLY) {
					// FIXME set a session variable to hide the edit buttons.

				}
				LOGGER.info("setAuthorizationChecked(true)");
				setAuthorizationChecked(true);
				return analysisStepCommand;
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("NULL CONDITION SATISFIED");
				}
				LOGGER.info("setAuthorizationChecked(false)");
				setAuthorizationChecked(false);
				return analysisStepCommand;
			}
		}
	}
	private List<AnalyzedDataCommand> createAnalyzedDataCommandList(AnalysisStep analysisStep) {
		Iterator<AnalyzedData> dataListIterator = analysisStep.getDataSetReadOnly().iterator();
		List<AnalyzedDataCommand> analyzedDataCommandList = new ArrayList<AnalyzedDataCommand>();
		while ( dataListIterator.hasNext() ) {
			AnalyzedData data = dataListIterator.next();
			AnalyzedDataCommand command = new AnalyzedDataCommand();
			command.setDataType(data.getDataType());
			command.setDisplayName(data.getDisplayName());
			command.setInput(data.isInputData());
			command.setInputOutputType(data.getInputOutput());
			command.setNotes(data.getNotes());
			command.setId(data.getId());
			List<EditFieldCommand> efcList = new ArrayList<EditFieldCommand>();
			EditFieldCommand efc = null;
			if ( data.getDataType().equals("tree") ) {
				command.setDataId(data.getTreeData().getId());
				efc = new EditFieldCommand(data.getTreeData(),data.getInputOutput());
			}
			else if ( data.getDataType().equals("matrix") ) {
				command.setDataId(data.getMatrixData().getId());
				efc = new EditFieldCommand(data.getMatrixData(),data.getInputOutput());
			}
			efcList.add(efc);
			analyzedDataCommandList.add(command);
		}
		return analyzedDataCommandList;
	}
}
