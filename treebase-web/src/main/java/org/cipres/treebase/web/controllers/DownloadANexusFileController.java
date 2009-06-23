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

//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
import java.sql.Clob;
//import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.web.util.ControllerUtil;
//import org.cipres.treebase.web.util.WebUtil;

/**
 * 
 * Created on 14th August, 2007
 * 
 * Modified on April 29, 2008 to accommodate blank spaces in the file name. Blank space are replaced
 * by an "_"
 * 
 * @author madhu
 * 
 */
public class DownloadANexusFileController extends AbstractDownloadController implements Controller {

//	private static final Logger LOGGER = Logger.getLogger(DownloadANexusFileController.class);

	private PhyloTreeService mPhyloTreeService;
	private StudyService mStudyService;
	private MatrixService mMatrixService;

	private static final String org = "org";

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

	public PhyloTreeService getPhyloTreeService() {
		return mPhyloTreeService;
	}

	/**
	 * Set the PhyloTreeService
	 */
	public void setPhyloTreeService(PhyloTreeService pNewPhyloTreeService) {
		mPhyloTreeService = pNewPhyloTreeService;
	}

	/**
	 * Return the StudyService field.
	 * 
	 * @return StudyService mStudyService
	 */
	public StudyService getStudyService() {
		return mStudyService;
	}

	/**
	 * Set the StudyService field.
	 */
	public void setStudyService(StudyService pNewStudyService) {
		mStudyService = pNewStudyService;
	}

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
//		String sep = System.getProperty("file.separator");
//		String downloadDir = request.getSession().getServletContext().getRealPath(
//			"/NexusFileDownload")
//			+ sep + request.getRemoteUser();
//		String downloadDir = getDownloadDir(request);
//
//		String treeId = request.getParameter("treeid");
//		String matrixId = request.getParameter("matrixid");
//		String nexusFile = request.getParameter("nexusfile");
//
//		if (treeId == null && matrixId == null && nexusFile == null) {
//			return null;
//		}
//		long clickedParameterId = 0L;
//		String nexusFileName = null;
//
//		if (treeId != null) {
//			clickedParameterId = Long.parseLong(treeId);
//			nexusFileName = getPhyloTreeService().findByID(clickedParameterId).getNexusFileName();
//		} else if (matrixId != null) {
//			clickedParameterId = Long.parseLong(matrixId);
//			nexusFileName = getMatrixService().findByID(clickedParameterId).getNexusFileName();
//		} else if (nexusFile != null) {
//			nexusFileName = (nexusFile).replaceAll("%20", "_");
//		}
//
//		if (nexusFileName == null) {
//			return null;
//		}

		generateAFileDynamically(request,response,0L);
//		generateAFileDynamically(request, downloadDir, nexusFileName);
//		WebUtil.downloadFile(response, downloadDir, nexusFileName + org);

		return null;
	}

	/**
	 * @param req HttpServletRequest
	 * @param downloadDirName String Name of the directory where file(s) will be downloaded
	 * @param fileName String name of the download file
	 */
	/*
	private void generateAFileDynamically(
		HttpServletRequest req,
		String downloadDirName,
		String fileName) {

		Study study = ControllerUtil.findStudy(req, mStudyService);
		Map<String, Clob> nexusMap = study.getNexusFiles();

		File dirPath = new File(downloadDirName);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		// long start = System.currentTimeMillis();
		// long end = System.currentTimeMillis();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("IN DOWNLOAD A NEXUS FILE CONTROLLER for file:  " + fileName);
		}

		try {

			File file = new File(downloadDirName + TreebaseUtil.FILESEP
				+ (fileName + org).replaceAll(TreebaseUtil.ANEMPTYSPACE, "_"));
			FileWriter out = new FileWriter(file);

			// Contents of the file will be overwritten if file already exists
			String clobStr = "File Not Found. File Name is: " + fileName;
			Clob clob = nexusMap.get(fileName);

			if (clob != null) {
				int clobLength = (int) clob.length();
				char[] clobchars = new char[clobLength];
				clob.getCharacterStream().read(clobchars);
				clobStr = new String(clobchars);
			}
			out.write(clobStr);

			out.close();
			// File did not exist and was created
			// } else {
			// File already exists
			// }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

	}
	*/

	@Override
	protected String getFileName(long objectId,HttpServletRequest req) {
		String treeId = req.getParameter("treeid");
		String matrixId = req.getParameter("matrixid");
		String nexusFile = req.getParameter("nexusfile");

		if (treeId == null && matrixId == null && nexusFile == null) {
			return null;
		}
		long clickedParameterId = 0L;
		String nexusFileName = null;
		if (treeId != null) {
			clickedParameterId = Long.parseLong(treeId);
			nexusFileName = getPhyloTreeService().findByID(clickedParameterId).getNexusFileName();
		} else if (matrixId != null) {
			clickedParameterId = Long.parseLong(matrixId);
			nexusFileName = getMatrixService().findByID(clickedParameterId).getNexusFileName();
		} else if (nexusFile != null) {
			nexusFileName = (nexusFile).replaceAll("%20", "_");
		}
		return ( nexusFileName + org ).replaceAll(TreebaseUtil.ANEMPTYSPACE, "_");
	}

	@Override
	protected String getFileNamePrefix() {
		// Not necessary because we override getFileName
		return null;
	}

	@Override
	protected String getFileContent(long objectId, HttpServletRequest req) {
		String nexusFileName = null;
		String treeId = req.getParameter("treeid");
		String matrixId = req.getParameter("matrixid");
		String nexusFile = req.getParameter("nexusfile");		
		long clickedParameterId = 0L;
		if (treeId != null) {
			clickedParameterId = Long.parseLong(treeId);
			nexusFileName = getPhyloTreeService().findByID(clickedParameterId).getNexusFileName();
		} else if (matrixId != null) {
			clickedParameterId = Long.parseLong(matrixId);
			nexusFileName = getMatrixService().findByID(clickedParameterId).getNexusFileName();
		} else if (nexusFile != null) {
			nexusFileName = (nexusFile).replaceAll("%20", "_");
		}
		if (nexusFileName == null) {
			return null;
		}
		Study study = ControllerUtil.findStudy(req, mStudyService);
		Map<String, Clob> nexusMap = study.getNexusFiles();		
		Clob clob = nexusMap.get(nexusFileName);
		String clobStr = "File Not Found. File Name is: " + nexusFileName;
		if (clob != null) {
			try {
				int clobLength = (int) clob.length();
				char[] clobchars = new char[clobLength];
				clob.getCharacterStream().read(clobchars);
				clobStr = new String(clobchars);
			} catch ( Exception e ) {
				e.printStackTrace();
			}
		}
		return clobStr;
	}

}
