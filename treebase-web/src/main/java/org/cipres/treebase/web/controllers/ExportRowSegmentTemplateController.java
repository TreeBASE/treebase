


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
 * Created on June 9, 2008
 * 
 * For exporting the tab delimited template for a particular matrix.
 * 
 */
public class ExportRowSegmentTemplateController implements Controller {

	private static final String ROWSEGMENTTEMPLATE = "RowSegmentTemplate";

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
			data = getRowSegmentService().generateRowSegmentTemplateTSV(Long.parseLong(matrixId));
		}

		String downloadDir = request.getSession().getServletContext().getRealPath(
			TreebaseUtil.FILESEP + ROWSEGMENTTEMPLATE)
			+ TreebaseUtil.FILESEP + request.getRemoteUser();

		String fileName = ROWSEGMENTTEMPLATE + "_" + matrixId + ".txt";

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
