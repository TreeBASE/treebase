
package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.nexus.mesquite.MesquiteConverter;

/**
 * Summary page after upload nexus file(s). 
 * Shows the number of matrices and trees uploaded, and parser log. 
 * 
 * @author Jin Ruan
 */

public class UploadFileSummaryController extends BaseFormController {

	private static final Logger LOGGER = LogManager.getLogger(UploadFileSummaryController.class);

	/**
	 * Constructor.
	 */
	public UploadFileSummaryController() {
		super();
	}

	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		// get the mesquite log file:
		String nexusFileName = (String) request.getSession().getAttribute("uploadFileName");
		if (!TreebaseUtil.isEmpty(nexusFileName)) {
			String parserLog = MesquiteConverter.getParsingLog(nexusFileName);

			return setAttributeAndShowForm(request, response, bindExp, "uploadParserLog", parserLog);
		}

		return new ModelAndView(getSuccessView());
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	// */
	// @Override
	// protected Object formBackingObject(HttpServletRequest request) {
	//
	// if (request.getParameter("matrixid") != null) {
	//
	// Long matrixID = Long.parseLong(request.getParameter("matrixid"));
	// Matrix pMatrix = getMatrixService().findByID(matrixID);
	// return pMatrix;
	//
	// } else {
	// return null;
	// }
	// }
	//
}
