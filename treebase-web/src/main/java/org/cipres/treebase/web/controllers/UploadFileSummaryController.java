/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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

	private static final Logger LOGGER = Logger.getLogger(UploadFileSummaryController.class);

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
