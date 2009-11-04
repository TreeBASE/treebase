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
