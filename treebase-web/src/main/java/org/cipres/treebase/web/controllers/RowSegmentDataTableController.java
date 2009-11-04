


package org.cipres.treebase.web.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.matrix.RowSegmentService;
import org.cipres.treebase.domain.matrix.RowSegmentService.RowSegmentField;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.framework.ExecutionResult;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * 
 * @author madhu
 * 
 * Created on May 6, 2008
 * 
 * Puts the tab delimited data in a two dimensional array and array is put in the session scope.
 * 
 */

public class RowSegmentDataTableController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(RowSegmentDataTableController.class);
	private RowSegmentService mRowSegmentService;
	private MatrixService mMatrixService;
	private StudyService mStudyService;

	@Override
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		boolean checked = true;
		if (request.getParameter("cbox") == null) {
			checked = false;
		}
		System.out.println("Value of the check box: " + checked);

		List<String> headerListWithNoIgnores = new ArrayList<String>();
		List<String> headerList = new ArrayList<String>();
		String[][] rowSegmentDataArray = (String[][]) request.getSession().getAttribute(
			"ROWSEGMENTDATAARRAY");

		if (rowSegmentDataArray == null || rowSegmentDataArray.length < 1) {
			return new ModelAndView(getCancelView());
		}
		
		String matrixId = (String) request.getSession().getAttribute("MATRIX_ID");
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("MATRIX ID = " + matrixId);
		}

		List<RowSegmentService.RowSegmentField> rowSFList = new ArrayList<RowSegmentService.RowSegmentField>();
		for (int i = 0; i < rowSegmentDataArray[0].length; i++) {
			String parameter = request.getParameter("coldata" + i);

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Parameter value = " + parameter);
			}
			headerList.add(parameter);
			rowSFList.add(RowSegmentField.findByDisplayName(parameter));
			if (!parameter.equals("Ignore")) {
				headerListWithNoIgnores.add(parameter);
			}
		}

		if (headerListWithNoIgnores.size() == 0) {
			request.setAttribute(
				"messages",
				"No selections are made for headers. Please fix the problem.");
			return showForm(request, response, bindExp);
		}
		Set<String> aset = new HashSet<String>();
		aset.addAll(headerListWithNoIgnores);
		// This step is necessary to check for duplicate selected column headers.
		if (headerListWithNoIgnores.size() != aset.size()) {
			request.setAttribute(
				"messages",
				"Duplicate selections are made for headers. Please fix the problem.");
			request.setAttribute("SELECTEDHEADERLIST", headerList);
			return showForm(request, response, bindExp);
		}

		String path = getServletContext().getRealPath(TreebaseUtil.FILESEP + "RowSegmentData")
			+ TreebaseUtil.FILESEP + request.getRemoteUser() + TreebaseUtil.FILESEP
			+ request.getSession().getAttribute("ROWSEGMENTDATAFILE");

		File file = new File(path);

		Study study = ControllerUtil.findStudy(request, mStudyService);

		ExecutionResult exResult = getRowSegmentService().createSegments(
			study.getId(),
			Long.parseLong(matrixId),
			file,
			rowSFList,
			checked);

		List<String> messages = new ArrayList<String>();
		messages.add(exResult.getErrorMessage());
		messages.add("Success count: " + exResult.getSuccessfulCount());
		messages.add("Failure count: " + exResult.getFailureCount());

		request.getSession().setAttribute("messages", messages);

		// List<RowSegment> rowSegmentList = getRowSegmentService().findByMatrixID(
		// Long.parseLong(matrixId));
		// for (RowSegment rs : rowSegmentList) {
		// We have to get the column headers
		// }

		return new ModelAndView(getSuccessView());
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		List<String> headerNameList = RowSegmentService.RowSegmentField.getDisplayNameList();

		request.getSession().setAttribute("ROWSEGMENTHEADERS", headerNameList);
		return new Object();
	}

	/**
	 * @return the rowSegmentService
	 */
	public RowSegmentService getRowSegmentService() {
		return mRowSegmentService;
	}

	/**
	 * @param pRowSegmentService the rowSegmentService to set
	 */
	public void setRowSegmentService(RowSegmentService pRowSegmentService) {
		mRowSegmentService = pRowSegmentService;
	}

	/**
	 * @return the matrixService
	 */
	public MatrixService getMatrixService() {
		return mMatrixService;
	}

	/**
	 * @param pMatrixService the matrixService to set
	 */
	public void setMatrixService(MatrixService pMatrixService) {
		mMatrixService = pMatrixService;
	}

	/**
	 * @return the studyService
	 */
	public StudyService getStudyService() {
		return mStudyService;
	}

	/**
	 * @param pStudyService the studyService to set
	 */
	public void setStudyService(StudyService pStudyService) {
		mStudyService = pStudyService;
	}
}
