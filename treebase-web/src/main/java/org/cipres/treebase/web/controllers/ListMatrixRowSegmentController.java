/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.TreebaseUtil;
import org.apache.log4j.Logger;
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
	private static final Logger LOGGER = Logger.getLogger(ListMatrixRowSegmentController.class);

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
