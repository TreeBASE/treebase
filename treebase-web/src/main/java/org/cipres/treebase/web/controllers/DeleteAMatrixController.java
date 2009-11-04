


package org.cipres.treebase.web.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.study.SubmissionService;

/**
 * 
 * @author madhu
 * 
 */

public class DeleteAMatrixController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(DeleteAMatrixController.class);

	private MatrixService mMatrixService;
	private SubmissionHome mSubmissionHome;
	private SubmissionService mSubmissionService;

	public SubmissionHome getSubmissionHome() {
		return mSubmissionHome;
	}

	public void setSubmissionHome(SubmissionHome pSubmissionHome) {
		mSubmissionHome = pSubmissionHome;
	}

	public MatrixService getMatrixService() {
		return mMatrixService;
	}

	public void setMatrixService(MatrixService pMatrixService) {
		mMatrixService = pMatrixService;
	}

	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		Matrix amatrix = (Matrix) command;

		String username = request.getRemoteUser();

		Long submissionId = getSubmissionHome().findByMatrix(amatrix).getId();

		TBPermission perm2 = getSubmissionService().getPermission(username, submissionId);

		if (perm2 == TBPermission.WRITE) {
			getMatrixService().deleteMatrix(amatrix);
		} else {

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Sorry Authorization Failure, you cannot delete this matrix.");
			}

			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"Sorry Authorization Failure, you cannot delete this matrix.");

		}

		return new ModelAndView(getSuccessView());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) {

		if (request.getParameter("matrixid") != null) {

			Long matrixID = Long.parseLong(request.getParameter("matrixid"));
			Matrix pMatrix = getMatrixService().findByID(matrixID);
			return pMatrix;

		} else {
			return null;
		}
	}

	protected Map<String,String> referenceData(HttpServletRequest request) {

		Map<String,String> referenceMap = new HashMap<String,String>();
		referenceMap.put("deleteelementtype", "Delete this particular matrix");

		Long matrixID = Long.parseLong(request.getParameter("matrixid"));
		Matrix pMatrix = getMatrixService().findByID(matrixID);

		if (pMatrix.getStudy() != null) { // It means this tree is tied to analysis step
			referenceMap.put("generalmessage", "This matrix is tied to an Analysis Step.");
		}

		referenceMap.put("deleteid", "Matrix ID : " + matrixID);
		referenceMap.put("objectname", "Matrix Title : " + pMatrix.getTitle());

		return referenceMap;
	}

	public SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	public void setSubmissionService(SubmissionService pSubmissionService) {
		mSubmissionService = pSubmissionService;
	}

}
