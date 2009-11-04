package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.MatrixRowService;
import org.cipres.treebase.domain.matrix.RowSegment;
import org.cipres.treebase.domain.matrix.RowSegmentService;
import org.cipres.treebase.framework.ExecutionResult;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * @author Madhu
 * 
 * This is a new version of the MatixRowSegmentFormController
 * 
 * Created on May 19, 2008
 * 
 */
public class SingleRowSegmentController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(SingleRowSegmentController.class);

	private RowSegmentService mRowSegmentService;
	private MatrixRowService mMatrixRowService;

	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		RowSegment aRowSegment = (RowSegment) command;
		Long matrixRowId = ControllerUtil.getMatrixRowId(request);
		MatrixRow matrixRow = mMatrixRowService.findByID(matrixRowId);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("++MARTIX ROW ID++ : " + matrixRowId.toString());
		}

		if (request.getParameter(ACTION_DELETE) != null) {
			mRowSegmentService.deleteRowSegment(aRowSegment);
		} else {
			System.out.println("MARTIX ROW ID In Submit Button: " + matrixRowId.toString());
			ExecutionResult executionResult = new ExecutionResult();
			boolean test = aRowSegment.validate(executionResult);
			if (test) {
				if (request.getParameter(ACTION_SUBMIT) != null) {
					matrixRow.addSegment(aRowSegment);
					mMatrixRowService.update(matrixRow);
				} else if (request.getParameter(ACTION_UPDATE) != null) {
					mRowSegmentService.update(aRowSegment);
				}
			} else {
				request.setAttribute("errors", executionResult.getErrorMessage());
				return showForm(request, response, bindExp);
			}

		}

		return new ModelAndView(getSuccessView() + "?id=" + matrixRowId.toString());

	}

	/**
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		Long matrixRowId = ControllerUtil.getMatrixRowId(request);
		MatrixRow matrixRow = getMatrixRowService().findByID(matrixRowId);

		RowSegment rowSegment = null;

		String rowSegmentId = ServletRequestUtils.getStringParameter(request, "id", null);

		if (rowSegmentId == null) {
			rowSegment = new RowSegment();
			rowSegment.setMatrixRow(matrixRow);
			rowSegment.setTaxonLabel(matrixRow.getTaxonLabel());
			
			int endIndex = matrixRow.getSymbolString().length();
			if (endIndex >= 1) {
				rowSegment.setStartIndex(1);
				rowSegment.setEndIndex(endIndex);
			}
			
			return rowSegment;
		}

		List<String> sessionlist = new ArrayList<String>();
		sessionlist.add(request.getRequestURL().toString());
		sessionlist.add(rowSegmentId);

		request.getSession().setAttribute("REQUEST_VIEW_URL", sessionlist);

		rowSegment = getRowSegmentService().findByID(Long.parseLong(rowSegmentId));
		return rowSegment;
	}

	@Override
	protected ModelAndView onCancel(
		HttpServletRequest pRequest,
		HttpServletResponse pResponse,
		Object pCommand) throws Exception {

		Long matrixRowId = ControllerUtil.getMatrixRowId(pRequest);

		return new ModelAndView(getCancelView() + "?id=" + matrixRowId.toString());
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
	 * @return the rowService
	 */
	public MatrixRowService getMatrixRowService() {
		return mMatrixRowService;
	}

	/**
	 * @param pRowService the rowService to set
	 */
	public void setMatrixRowService(MatrixRowService pMatrixRowService) {
		mMatrixRowService = pMatrixRowService;
	}

}
