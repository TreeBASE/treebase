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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.RowSegmentService;
import org.cipres.treebase.web.util.WebUtil;

/**
 * 
 * @author Madhu
 * 
 * Created on May 15, 2008
 * 
 * For exporting the tab delimited row segment data for a particular matrix.
 * 
 */
public class ExportRowSegmentDataController implements Controller {

	private RowSegmentService mRowSegmentService;

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

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String matrixId = (String) request.getSession().getAttribute("MATRIX_ID");
		String data = null;
		if (!TreebaseUtil.isEmpty(matrixId)) {
			data = getRowSegmentService().generateRowSegmentTextTSV(Long.parseLong(matrixId));
		}

		String downloadDir = request.getSession().getServletContext().getRealPath(
			TreebaseUtil.FILESEP + "RowSegmentDownload")
			+ TreebaseUtil.FILESEP + request.getRemoteUser();

		String fileName = "RowSegmentData_" + matrixId + ".txt";

		File dirPath = new File(downloadDir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		try {

			File file = new File(downloadDir + TreebaseUtil.FILESEP + fileName);
			FileWriter out = new FileWriter(file);

			out.write(data);

			out.close();
			// File did not exist and was created
			// } else {
			// File already exists
			// }

		} catch (IOException e) {
			e.printStackTrace();
		}

		WebUtil.downloadFile(response, downloadDir, fileName);

		// TODO Auto-generated method stub
		return null;
	}

}
