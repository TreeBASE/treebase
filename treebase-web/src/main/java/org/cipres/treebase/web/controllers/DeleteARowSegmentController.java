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
import org.cipres.treebase.domain.matrix.RowSegment;
import org.cipres.treebase.domain.matrix.RowSegmentService;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.study.SubmissionService;

public class DeleteARowSegmentController extends BaseFormController {
	private static final Logger LOGGER = Logger.getLogger(DeleteARowSegmentController.class);

	private RowSegmentService mRowSegmentService;
	private SubmissionHome mSubmissionHome;
	private SubmissionService mSubmissionService;

	public DeleteARowSegmentController() {
		super();
		setSessionForm(true);
	}

	public SubmissionHome getSubmissionHome() {
		return mSubmissionHome;
	}

	public void setSubmissionHome(SubmissionHome pSubmissionHome) {
		mSubmissionHome = pSubmissionHome;
	}

	public RowSegmentService getRowSegmentService() {
		return mRowSegmentService;
	}

	public void setRowSegmentService(RowSegmentService pRowSegmentService) {
		mRowSegmentService = pRowSegmentService;
	}

	@Override
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		RowSegment arowsegment = (RowSegment) command;
		if (arowsegment.getId() == null) {
			return new ModelAndView(getCancelView());
			// Attempt is made to delete an object that does not exist anymore.
		}

		String username = request.getRemoteUser();
		arowsegment = getRowSegmentService().resurrect(arowsegment);

		Matrix amatrix = arowsegment.getMatrixRow().getMatrix();
		Long submissionId = getSubmissionHome().findByMatrix(amatrix).getId();

		TBPermission perm2 = getSubmissionService().getPermission(username, submissionId);

		if (perm2 == TBPermission.WRITE) {
			getRowSegmentService().deleteRowSegment(
				getRowSegmentService().findByID(arowsegment.getId()));
		} else {

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Sorry Authorization Failure, you cannot delete this rowsegment.");
			}

			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"Sorry Authorization Failure, you cannot delete this rowsegment.");

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

		String rsmentid = request.getParameter("rowsegmentid");
		RowSegment pRowSegment = null;
		if (rsmentid != null) {
			pRowSegment = getRowSegmentService().findByID(Long.parseLong(rsmentid));
			if (pRowSegment == null) {
				pRowSegment = new RowSegment();
			}
			// Even if rowsegmentid is provided, pRowSegment could still be null because rowsegment
			// with that particular id might have been deleted and user is pressing the back button
			// on the browser.

		}

		return pRowSegment;
	}

	@Override
	protected Map<String, String> referenceData(HttpServletRequest request) {

		Map<String, String> referenceMap = new HashMap<String, String>();
		referenceMap.put("deleteelementtype", "Delete this particular rowsegment");

		String noidavailable = null;

		String rsId = request.getParameter("rowsegmentid");
		if (rsId != null) {
			Long rsmentID = Long.parseLong(rsId);
			RowSegment pRowSegment = getRowSegmentService().findByID(rsmentID);

			referenceMap.put("deleteid", "RowSegment ID : " + rsmentID);

			if (pRowSegment == null) {
				noidavailable = "Rowsement with id <u>" + rsId + "</u> does not exist.";
			} else {
				referenceMap.put("objectname", "RowSegment Title : " + pRowSegment.getTitle());
			}
		} else {
			noidavailable = "Rowsement with id <u>" + rsId + "<u> does not exist.";
		}
		referenceMap.put("noidavailable", noidavailable);
		return referenceMap;
	}

	public SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	public void setSubmissionService(SubmissionService pSubmissionService) {
		mSubmissionService = pSubmissionService;
	}
}
