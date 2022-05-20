


package org.cipres.treebase.web.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.RowSegment;
import org.cipres.treebase.domain.matrix.RowSegmentService;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.study.SubmissionService;

public class DeleteARowSegmentController extends BaseFormController {
	private static final Logger LOGGER = LogManager.getLogger(DeleteARowSegmentController.class);

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
