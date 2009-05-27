package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixRow;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.model.MatrixRowCommand;
import org.cipres.treebase.web.util.ControllerUtil;

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

/**
 * ListMatrixRowController.java
 * 
 * Created on June 23, 2006
 * 
 * @author lcchan
 * 
 */
public class ListMatrixRowController implements Controller {

	private static final Logger LOGGER = Logger.getLogger(ListMatrixRowController.class);

	private MatrixService mMatrixService;

	/**
	 * Return the MatrixService field.
	 * 
	 * @return MatrixService mMatrixService
	 */
	public MatrixService getMatrixService() {
		return mMatrixService;
	}

	/**
	 * Set the MatrixService field.
	 */
	public void setMatrixService(MatrixService pNewMatrixService) {
		mMatrixService = pNewMatrixService;
	}

	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String matrix_id = ServletRequestUtils.getStringParameter(request, "id", null);
		Matrix matrix = mMatrixService.findByID(Long.parseLong(matrix_id));

		request.getSession().setAttribute("MATRIX_ID", matrix_id); // This session variable is used
		// for the cancel button in the
		// matrixRowSegmentList.jsp page

		// all the matrices are standard matrices for now
		CharacterMatrix standardMatrix = (CharacterMatrix) matrix;
		List<MatrixRow> matrixRows = standardMatrix.getRowsReadOnly();

		// save Matrix object in session
		ControllerUtil.saveMatrix(request, matrix);

		// add some data for the row to show
		List<MatrixRowCommand> matrixRowSet = new ArrayList<MatrixRowCommand>();
		//List<String> rowElementStr = getMatrixService().findRowAsString(standardMatrix, 0, Constants.MAX_MATRIX_COLUMNS_SHOW);
		
		for (int i = 0; i < matrixRows.size(); i++) {
			MatrixRow row = matrixRows.get(i);
			
//			String rowElementString = row.getElementAsString(
//				0,
//				Constants.MAX_MATRIX_COLUMNS_SHOW);
			
			//matrixRowSet.add(new MatrixRowCommand(row, rowElementStr.get(i)));
			matrixRowSet.add(new MatrixRowCommand(row));
			}
		return new ModelAndView("matrixRowList", Constants.MATRIX_ROW_LIST, matrixRowSet);
	}

}
