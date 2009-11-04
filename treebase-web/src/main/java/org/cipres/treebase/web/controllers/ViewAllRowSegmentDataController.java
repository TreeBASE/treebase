


package org.cipres.treebase.web.controllers;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.RowSegment;
import org.cipres.treebase.domain.matrix.RowSegmentService;
import org.cipres.treebase.web.model.AGenericList;

/**
 * 
 * @author Madhu
 * 
 * created on May 14, 2008
 * 
 * Idea here is to show the row segment data for all the rows of the matrix. It is possible some of
 * the data may have been entered for individual rows and rest of the data might be uploaded for
 * many rows in bulk through row segment data file upload feature.
 * 
 */
public class ViewAllRowSegmentDataController extends BaseFormController {

	private RowSegmentService mRowSegmentService;

	private final String aSuccessView = "redirect:/user/viewAllRowSegmentData.html";

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

	@Override
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) throws Exception {

		AGenericList<List<RowSegment>> rsList = (AGenericList<List<RowSegment>>) command;

		if (request.getParameter(ACTION_DELETE) != null) {
			for (RowSegment rsegment : rsList.getMyList()) {
				System.out.println("Checked value: " + rsegment.getChecked());

				if (rsegment.getChecked()) {
					getRowSegmentService().deleteRowSegment(rsegment);
				}
			}
		}

		if (request.getParameter(ACTION_UPDATE) != null) {
			getRowSegmentService().updateCollection(rsList.getMyList());
		}

		return new ModelAndView(aSuccessView);
	}

	@Override
	public Object formBackingObject(HttpServletRequest request) throws ServletException {
		String matrixId = (String) request.getSession().getAttribute("MATRIX_ID");
		List<RowSegment> rowSegmentList = null;
		if (!TreebaseUtil.isEmpty(matrixId)) {
			rowSegmentList = getRowSegmentService().findByMatrixID(Long.parseLong(matrixId));
		}
		return new AGenericList<List<RowSegment>>(rowSegmentList);
	}

}
