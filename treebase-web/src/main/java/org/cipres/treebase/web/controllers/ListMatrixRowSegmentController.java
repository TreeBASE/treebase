
package org.cipres.treebase.web.controllers;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.TreebaseUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.MatrixRowService;
import org.cipres.treebase.domain.matrix.RowSegment;
import org.cipres.treebase.domain.matrix.RowSegmentService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * ListMatrixRowSegment.java
 * 
 * Created on June 23, 2006
 * 
 * @author lcchan
 * 
 */
public class ListMatrixRowSegmentController implements Controller {

	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = LogManager.getLogger(ListMatrixRowSegmentController.class);

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

	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String matrix_row_id = ServletRequestUtils.getStringParameter(request, "id", null);

		// TODO:

		if (TreebaseUtil.isEmpty(matrix_row_id)) {
			return new ModelAndView(new RedirectView("matrixRowList.html" + "?id="
				+ request.getSession().getAttribute("MATRIX_ID"))); // Go back to the matrix row
			// list using Matrix ID.
		}
		MatrixRow matrixRow = mMatrixRowService.findByID(Long.parseLong(matrix_row_id));
		// Save MatrixRow to session
		ControllerUtil.saveMatrixRow(request, matrixRow);
		// Get row segment list for the matrix row selected
		Set<RowSegment> rowSegments = matrixRow.getSegmentsReadOnly();
		return new ModelAndView(
			"matrixRowSegmentList",
			Constants.MATRIX_ROW_SEGMENT_LIST,
			rowSegments);
	}
}
