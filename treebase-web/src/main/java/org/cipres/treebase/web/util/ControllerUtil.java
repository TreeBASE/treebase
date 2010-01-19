
package org.cipres.treebase.web.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserService;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.study.Algorithm;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.DistanceAlgorithm;
import org.cipres.treebase.domain.study.LikelihoodAlgorithm;
import org.cipres.treebase.domain.study.OtherAlgorithm;
import org.cipres.treebase.domain.study.ParsimonyAlgorithm;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.exceptions.EmptyStudyException;

/**
 * ControllerUtil.java
 * 
 * Created on Jun 6, 2006
 * 
 * @author lcchan
 * 
 */
public class ControllerUtil {
	private static final Logger LOGGER = Logger.getLogger(ControllerUtil.class);

	public static final String DEL_STUDY_PAGE_KEY = "DEL_STUDY_PAGE";

	private static final String message_no_study = "No study has been selected. You must select one from the list below or create one to continue	";

	/**
	 * Return an authenticated User object
	 * 
	 * @param request
	 * @param userService
	 * @return User
	 */
	public static User getUser(HttpServletRequest request, UserService userService) {
		// TODO: check if username is null (should have been handled by Acegi
		String username = request.getRemoteUser();
		User user = userService.findUserByName(username);
		return user;
	}

	/**
	 * Return study id in the session scope so we know what study/submission we are working with
	 * 
	 * @param request
	 * @return
	 */
	public static Long getStudyId(HttpServletRequest request) throws EmptyStudyException {
		Map<String, Object> map = (Map) request.getSession().getAttribute(Constants.STUDY_MAP);
		if (map == null) {
			throw new EmptyStudyException(message_no_study);
		}
		return (Long) map.get("id");
	}

	/**
	 * Retrieve a study object give a submission id We have the method here in case submission id
	 * gets changed in the future (i.e. by submission number)
	 * 
	 * @param request
	 * @param studyService
	 * @return Study
	 */
	public static Study findStudy(HttpServletRequest request, StudyService studyService)
		throws EmptyStudyException {		
		// TODO:				
		Map attribute = (Map) request.getSession().getAttribute(Constants.STUDY_MAP);
		Map<String, Object> map = attribute;
		Long id;
		Study study = null;
		;

		if (map != null) {
			String action = (String) map.get("action");

			if (action != null && action.equals("search")) {
				id = (Long) map.get("id");
				study = studyService.findByID(id);
			} else {
				id = getStudyId(request);
				study = studyService.findByID(id);
			}
			if (study == null) {
				throw new EmptyStudyException(message_no_study);
			}
		}
		/*
		 * XXX I (RAV) added the block below, I realize it's clunky,
		 * but I ended up in a condition where the map was null, but
		 * the study id was defined, so I did it like this. If there
		 * is a better way (which I'm sure there is) please fix this
		 * method.
		 */
		else {
			id = Long.parseLong(request.getParameter("id").toString());	
			study = studyService.findByID(id);
			if (study == null) {
				throw new EmptyStudyException(message_no_study);
			}
		}
		return study;
	}

	/**
	 * 
	 * Return analysis id in the session scope so we know what study/submission we are working with
	 * 
	 * @param request
	 * @return
	 */
	// TODO: check null value
	public static Long getAnalysisId(HttpServletRequest request) {
		Map<String, Object> map = (Map) request.getSession().getAttribute(Constants.ANALYSIS_MAP);
		Long id = (Long) map.get("id");
		return id;
	}

	/**
	 * return analylsis step id in the session
	 * 
	 * @param request
	 * @return
	 */
	public static Long getAnalysisStepId(HttpServletRequest request) {
		Map<String, Object> map = (Map) request.getSession().getAttribute(
			Constants.ANALYSIS_STEP_MAP);
		Long id = (Long) map.get("id");
		return id;
	}

	/**
	 * study information to be store in the session
	 * 
	 * @param request
	 * @param study
	 */
	public static void saveStudy(HttpServletRequest request, Study study) {
		Map<String, Object> studyMap = new HashMap<String, Object>();
		// studyMap.put("id", study.getSubmission().getId());
		studyMap.put("id", study.getId());
		studyMap.put("name", study.getName());
		request.getSession().setAttribute(Constants.STUDY_MAP, studyMap);
	}

	// TODO:
	// get rid of this method
	// add a "action" field so we know if we are searching or submitting....
	public static void saveStudy(HttpServletRequest request, Study study, String action) {
		Map<String, Object> studyMap = new HashMap<String, Object>();
		studyMap.put("id", study.getId());
		studyMap.put("name", study.getName());
		studyMap.put("action", action);
		request.getSession().setAttribute(Constants.STUDY_MAP, studyMap);
	}

	/**
	 * analysis information to be store in the session
	 * 
	 * @param request
	 * @param analysis
	 */
	public static void saveAnalysis(HttpServletRequest request, Analysis analysis) {
		Map<String, Object> analysisMap = new HashMap<String, Object>();
		analysisMap.put("id", analysis.getId());
		analysisMap.put("name", analysis.getName());
		request.getSession().setAttribute(Constants.ANALYSIS_MAP, analysisMap);
	}

	/**
	 * analysis step information to be store in the session
	 * 
	 * @param request
	 * @param analysisStep
	 */
	public static void saveAnalysisStep(HttpServletRequest request, AnalysisStep analysisStep) {
		Map<String, Object> analysisStepMap = new HashMap<String, Object>();
		analysisStepMap.put("id", analysisStep.getId());
		analysisStepMap.put("name", analysisStep.getName());
		request.getSession().setAttribute(Constants.ANALYSIS_STEP_MAP, analysisStepMap);
	}

	/**
	 * 
	 * 
	 * @param request
	 * @param matrix
	 */
	public static void saveMatrix(HttpServletRequest request, Matrix matrix) {
		Map<String, Object> matrixMap = new HashMap<String, Object>();
		matrixMap.put("id", matrix.getId());
		matrixMap.put("title", matrix.getTitle());
		request.getSession().setAttribute(Constants.MATRIX_MAP, matrixMap);
	}

	public static Long getMatrixId(HttpServletRequest request) {
		Map<String, Object> map = (Map) request.getSession().getAttribute(Constants.MATRIX_MAP);
		Long id = (Long) map.get("id");
		return id;
	}

	/**
	 * 
	 * 
	 * @param request
	 * @param matrixRow
	 */
	public static void saveMatrixRow(HttpServletRequest request, MatrixRow matrixRow) {
		Map<String, Object> matrixRowMap = new HashMap<String, Object>();
		matrixRowMap.put("id", matrixRow.getId());
		matrixRowMap.put("taxonLabel", matrixRow.getTaxonLabel());
		request.getSession().setAttribute(Constants.MATRIX_ROW_MAP, matrixRowMap);
	}

	public static Long getMatrixRowId(HttpServletRequest request) {
		Map<String, Object> map = (Map) request.getSession().getAttribute(Constants.MATRIX_ROW_MAP);
		Long id = (Long) map.get("id");
		return id;
	}

	/**
	 * helper class to determine the type of algorithm
	 * 
	 * @param algorithm
	 * @return
	 */
	public static String getAlgorithmType(Algorithm algorithm) {
		String type = null;

		if (algorithm instanceof LikelihoodAlgorithm) {
			type = Constants.ALGORITHM_LIKELIHOOD;
		} else if (algorithm instanceof ParsimonyAlgorithm) {
			type = Constants.ALGORITHM_PARSIMONY;
		} else if (algorithm instanceof DistanceAlgorithm) {
			type = Constants.ALGORITHM_DISTANCE;
		} else if (algorithm instanceof OtherAlgorithm) {
			type = Constants.ALGORITHM_OTHER;
		}
		return type;
	}

	public static String getTreeBlockId(HttpServletRequest pRequest) {
		return (String) pRequest.getSession().getAttribute("TREEBLOCK_ID");
	}

	public static void setTreeBlockId(String pTreeBlockId, HttpServletRequest pRequest) {
		pRequest.getSession().setAttribute("TREEBLOCK_ID", pTreeBlockId);
	}

	public static boolean checkForLongNumber(String parameter) {
		boolean test = true;
		try {
			Long.parseLong(parameter);
		} catch (NumberFormatException nfe) {
			LOGGER.debug("EXCEPTION THROWN IN chekForLongNumber () IN ControllerUtil class");
			test = false;
			// nfe.printStackTrace();
		} finally {

		}
		return test;
	}
	
	/**
	 * This method looks up (and manipulates) two session attributes:
	 * <li>Constants.REVIEWER_AGREEMENT_ACCEPTED, whose value is a boolean.
	 * This attribute flags whether the reviewer has accepted the agreement
	 * regarding embargoed data.</li>
	 * <li>Constants.X_ACCESS_CODE, whose value is a string. This attribute
	 * stores the hashed ID string for the focal study.</li>
	 * @param req
	 * @return
	 */
	public static boolean isReviewerAccessGranted(HttpServletRequest req) {
		boolean passedHashedIDCheck = false;
		HttpSession session = req.getSession();
		if ( "cancel".equals(req.getParameter("agreement")) ) {
			session.setAttribute(Constants.REVIEWER_AGREEMENT_ACCEPTED, false);
		}	
		if ( "ok".equals(req.getParameter("agreement")) ) {
			session.setAttribute(Constants.REVIEWER_AGREEMENT_ACCEPTED, true);
		}
		Object xAccesCodeObject = session.getAttribute(Constants.X_ACCESS_CODE);
		if ( xAccesCodeObject != null ) {
			String suppliedHashedID = xAccesCodeObject.toString();
			TreebaseIDString tbidstr = new TreebaseIDString(Study.class,Long.parseLong(req.getParameter("id")));
			if ( suppliedHashedID.equals(tbidstr.getNamespacedGUID().getHashedIDString()) ) {
				passedHashedIDCheck = true;
				Object agreementAccepted = session.getAttribute(Constants.REVIEWER_AGREEMENT_ACCEPTED);
				if ( agreementAccepted == null || ((Boolean)agreementAccepted).booleanValue() == false ) {
					LOGGER.info("Going to display agreement - agreement acceptance: "+agreementAccepted);
					session.setAttribute("displayAgreement",true);
				}
				else {
					LOGGER.info("Not displaying agreement");					
					session.setAttribute("displayAgreement",false);
				}				
			}
		}		
		return passedHashedIDCheck;
	}	

}
