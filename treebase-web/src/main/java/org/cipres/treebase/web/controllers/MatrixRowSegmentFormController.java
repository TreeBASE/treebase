
package org.cipres.treebase.web.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;

import org.cipres.treebase.TreebaseUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.MatrixRowService;
import org.cipres.treebase.domain.matrix.RowSegment;
import org.cipres.treebase.domain.matrix.RowSegmentService;
import org.cipres.treebase.web.model.RowSegmentCommand;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * MatrixRowSegmentForm.java
 * 
 * Created on June 23, 2006
 * 
 * @author lcchan
 * 
 */
public class MatrixRowSegmentFormController extends BaseFormController {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = LogManager.getLogger(MatrixRowSegmentFormController.class);

	private RowSegmentService mRowSegmentService;
	private MatrixRowService mMatrixRowService;

	/**
	 * Return the MatrixRowService field.
	 * 
	 * @return MatrixRowService mMatrixRowService
	 */
	public MatrixRowService getMatrixRowService() {
		return mMatrixRowService;
	}

	/**
	 * Set the MatrixRowService field.
	 */
	public void setMatrixRowService(MatrixRowService pNewMatrixRowService) {
		mMatrixRowService = pNewMatrixRowService;
	}

	/**
	 * Return the RowSegmentService field.
	 * 
	 * @return RowSegmentService mRowSegmentService
	 */
	public RowSegmentService getRowSegmentService() {
		return mRowSegmentService;
	}

	/**
	 * Set the RowSegmentService field.
	 */
	public void setRowSegmentService(RowSegmentService pNewRowSegmentService) {
		mRowSegmentService = pNewRowSegmentService;
	}

	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) {

		RowSegmentCommand rowSegmentCommand = (RowSegmentCommand) command;
		Long matrix_row_id = ControllerUtil.getMatrixRowId(request);
		MatrixRow matrixRow = mMatrixRowService.findByID(matrix_row_id);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("++MARTIX ROW ID++ : " + matrix_row_id.toString());
		}

		// will never get here. see onCancel().
		// if (request.getParameter(ACTION_CANCEL) != null) {
		// System.out.println("MARTIX ROW ID In Cancel Button: " + matrix_row_id.toString());
		// return null;
		// // return new ModelAndView(getSuccessView() + "?id=" + matrix_row_id.toString());
		// }

		// get matrix so we can have the taxon label to show to user

		if (request.getParameter(ACTION_SUBMIT) != null) {
			System.out.println("MARTIX ROW ID In Submit Button: " + matrix_row_id.toString());
			matrixRow.addSegment(rowSegmentCommand.getRowSegment());
			mMatrixRowService.update(matrixRow);
		} else if (request.getParameter(ACTION_UPDATE) != null) {
			mRowSegmentService.update(rowSegmentCommand.getRowSegment());
		} else if (request.getParameter(ACTION_DELETE) != null) {
			mRowSegmentService.deleteRowSegment(rowSegmentCommand.getRowSegment());
		}
		return new ModelAndView(getSuccessView() + "?id=" + matrix_row_id.toString());
	}

	@Override
	protected ModelAndView onCancel(
		HttpServletRequest pRequest,
		HttpServletResponse pResponse,
		Object pCommand) throws Exception {

		RowSegmentCommand rowSegmentCommand = (RowSegmentCommand) pCommand;
		Long matrix_row_id = ControllerUtil.getMatrixRowId(pRequest);
		// setCan
		return new ModelAndView(getCancelView() + "?id=" + matrix_row_id.toString());
	}

	/**
	 * 
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		// get matrix so we can have the taxon label to show to user

		Long matrix_row_id = ControllerUtil.getMatrixRowId(request);
		MatrixRow matrixRow = mMatrixRowService.findByID(matrix_row_id);

		RowSegmentCommand rowSegmentCommand = new RowSegmentCommand();
		// add taxon label
		rowSegmentCommand.setTaxonLabel(matrixRow.getTaxonLabel().getTaxonLabel());
		rowSegmentCommand.setTaxonLabelId(matrixRow.getTaxonLabel().getId());
		// add matrix row data
		rowSegmentCommand.setMatrixRowData(matrixRow.buildElementAsString());

		String matrix_row_segment_id = ServletRequestUtils.getStringParameter(request, "id", null);

		// Setting the session variable "REQUEST_VIEW_URL
		List<String> sessionlist = new ArrayList<String>();
		sessionlist.add(request.getRequestURL().toString());
		sessionlist.add(matrix_row_segment_id);

		request.getSession().setAttribute("REQUEST_VIEW_URL", sessionlist);

		if (TreebaseUtil.isEmpty(matrix_row_segment_id)) {
			return rowSegmentCommand;
		}

		// setCancelParamKey("?id=" + matrix_row_id.toString());

		RowSegment rowSegment = mRowSegmentService.findByID(Long.parseLong(matrix_row_segment_id));
		rowSegmentCommand.setRowSegment(rowSegment);

		rowSegmentCommand.setMatrixRowDataSelected(rowSegment.getSegmentData());
		
		return rowSegmentCommand;
	}

}
