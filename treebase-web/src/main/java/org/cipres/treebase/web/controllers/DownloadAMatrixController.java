/*
 * Copyright 2007 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.web.util.ControllerUtil;
import org.cipres.treebase.web.util.WebUtil;

/**
 * DownloadAMatrixController.java
 * 
 * Generate a file file dynamically for download
 * 
 * Created on July 30, 2007
 * 
 * @author Madhu
 * 
 * 
 */

public class DownloadAMatrixController implements Controller {
	private static final Logger LOGGER = Logger.getLogger(DownloadAMatrixController.class);

	private MatrixService mMatrixService;
	private StudyService mStudyService;

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

	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		//
		// System.out.println("CONTEXT PATH IS: " + request.getContextPath());
		// System.out.println("REAL PATH IS: "
		// + request.getSession().getServletContext().getRealPath("/NexusFileDownload"));
		// System.out.println("PATH INFO: " + request.getPathInfo());
		//
		// TODO:

		if (request.getParameter("matrixid") == null) {
			return null;
		}
		
		//Long studyId = ControllerUtil.getStudyId(request);
		//Study s = getStudyService().findByID(studyId);
		
		long matrixId = Long.parseLong(request.getParameter("matrixid"));
		String fileName = getFileName(matrixId);

		String downloadDir = request.getSession().getServletContext().getRealPath(
			TreebaseUtil.FILESEP + "NexusFileDownload")
			+ TreebaseUtil.FILESEP + request.getRemoteUser();

		long start = System.currentTimeMillis();

		generateAFileDynamically(request, matrixId, downloadDir);
		WebUtil.downloadFile(response, downloadDir, fileName);

		long end = System.currentTimeMillis();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("TIME DIFFERENCE FOR A SINGAL FILE IN DOWNLOAD A MATRIX CONTROLLER: "
				+ (end - start));
		}

		return null; // new ModelAndView("treeList", Constants.TREE_LIST, phyloTrees);
	}

	/**
	 * This method adds "M" as Prefix to the matrix ID and ".nex" as Suffix.
	 * 
	 * @param id (Matrix id)
	 * @return returns the file name to be used for down loading
	 */

	private String getFileName(long id) {

		return "M" + id + ".nex";
	}

	/**
	 * This method is responsible for generating a Matrix file in a particular down load directory.
	 * But first It should get All the Taxa associated with a Matrix id and get the max length of
	 * the Taxon Labels and put Taxa and remaining matrix information in a separate ArrayLists
	 * 
	 * @param request
	 * @param pMatrixId It is Matrix id.
	 * @param downloadDirName down load directory where files will be created
	 */

	private void generateAFileDynamically(HttpServletRequest request, long pMatrixId, String downloadDirName) {

		Study pStudy = ControllerUtil.findStudy(request, mStudyService);
		
		Matrix matrix = mMatrixService.findByID(pMatrixId);

		File dirPath = new File(downloadDirName);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		StringBuilder matrixContent = new StringBuilder();
		matrixContent.append("#NEXUS\n");
		
		//header:
		TreebaseUtil.attachStudyHeader(pStudy, matrixContent);
		
		// taxa:
		TaxonLabelSet taxa = matrix.getTaxa();
		if (taxa != null) {
			//one taxon per line, no line number:
			taxa.buildNexusBlockTaxa(matrixContent, true, false);
		}
		
		matrix.generateNexusBlock(matrixContent);
		// matrixContent.append("END;\n"); // Not Needed

		String tmp = getFileName(pMatrixId);

		try {

			File file = new File(downloadDirName + TreebaseUtil.FILESEP + tmp);
			FileWriter out = new FileWriter(file);

			out.write(matrixContent.toString());

			out.close();
			// File did not exist and was created
			// } else {
			// File already exists
			// }

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
