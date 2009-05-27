/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */



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
