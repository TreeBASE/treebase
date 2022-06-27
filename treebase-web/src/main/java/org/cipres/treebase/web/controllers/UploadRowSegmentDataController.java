


package org.cipres.treebase.web.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.web.model.FileBean;

public class UploadRowSegmentDataController extends BaseFormController {

	private static final Logger LOGGER = LogManager.getLogger(UploadRowSegmentDataController.class);

	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException myerrors) throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering onSubmit()..."); //$NON-NLS-1$
		}

		String userName = request.getRemoteUser();
		String uploadDir = getServletContext().getRealPath(TreebaseUtil.FILESEP + "RowSegmentData")
			+ TreebaseUtil.FILESEP + userName;

		/* make sure the directory exists */
		File dirPath = new File(uploadDir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		/* store each file uploaded by the user */
		List<File> files = new ArrayList<File>();

		for (FileBean fileBean : getFiles(request)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER
					.debug("Uploading file to =>" + uploadDir + TreebaseUtil.FILESEP + fileBean.getName()); //$NON-NLS-1$
			}
			File uploadedFile = new File(uploadDir + TreebaseUtil.FILESEP + fileBean.getName());
			FileCopyUtils.copy(fileBean.getData(), uploadedFile);
			files.add(uploadedFile);
			request.getSession().setAttribute("ROWSEGMENTDATAFILE", fileBean.getName());
			break;
		}

		System.out.println("Row segment data file: "
			+ request.getSession().getAttribute("ROWSEGMENTDATAFILE"));

		// Parsing each file separately
		String[][] elements = null;
		List<String> allLines = new ArrayList<String>();
		for (File file : files) {
			BufferedReader bReader = new BufferedReader(new FileReader(file));
			String br = null;
			int m = 0, n = 0;
			while ((br = bReader.readLine()) != null) {
				if (m == 0) {
					n = br.split("\t").length; // Get the number of columns
				}
				allLines.add(br);
				++m;
				if (m >= 10) {
					bReader.close();
					break; // Idea is to display only 10 rows maximum.
				}
			}
			elements = new String[m][n];
			int i = 0;
			for (String line : allLines) {
				int j = 0;
				for (String part : line.split("\t")) {
					elements[i][j] = part;
					System.out.print(part + TreebaseUtil.ANEMPTYSPACE + "--");
					j++;
				}
				System.out.println();
				i++;

			}

			break; // we are handling only one file for now.

		}

		request.getSession().setAttribute("ROWSEGMENTDATAARRAY", elements);

		/* pass List<File> to service layer */
		// get user's Submission Object
		return new ModelAndView(getSuccessView());

	}

	/**
	 * Method to retrieve the list of files entered by the user
	 * 
	 * Creation date: May 19, 2006 10:57:01 AM
	 */
	public List<FileBean> getFiles(HttpServletRequest request) throws Exception {
		if (request instanceof MultipartHttpServletRequest) {

			MultipartHttpServletRequest multiReq = (MultipartHttpServletRequest) request;
			MultipartFile file = null;
			FileBean fileBean = null;
			List<FileBean> files = new ArrayList<FileBean>();
			for (Object o : multiReq.getFileMap().values()) {
				if (o != null) {
					file = (MultipartFile) o;
					if (!file.isEmpty()) {
						fileBean = new FileBean();
						fileBean.setName(file.getOriginalFilename());
						fileBean.setData(file.getBytes());
						files.add(fileBean);
					}
				}
			}
			return files;
		}
		return null;

	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		return new Object();
	}
}
