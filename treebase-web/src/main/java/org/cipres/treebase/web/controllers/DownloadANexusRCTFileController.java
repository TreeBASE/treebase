/*
 * CIPRES Copyright (c) 2005- 2008, The Regents of the University of California All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. *
 * Neither the name of the University of California or the San Diego Supercomputer Center nor the
 * names of its contributors may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.cipres.treebase.web.controllers;

import java.io.File;
import java.io.FileWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.web.util.ControllerUtil;
import org.cipres.treebase.web.util.WebUtil;

/**
 * @author madhu
 * 
 * Created on March 17, 2008
 * 
 * Modified on April 28, 2008
 * 
 */
public class DownloadANexusRCTFileController implements Controller {

	Logger LOGGER = Logger.getLogger(DownloadANexusRCTFileController.class);

	StudyService mStudyService;

	/**
	 * @return the studyService
	 */
	public StudyService getStudyService() {
		return mStudyService;
	}

	/**
	 * @param pStudyService the studyService to set
	 */
	public void setStudyService(StudyService pStudyService) {
		mStudyService = pStudyService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest pRequest, HttpServletResponse pResponse)
		throws Exception {

		Long study_id = ControllerUtil.getStudyId(pRequest);
		Study study = mStudyService.findByID(study_id);
		Submission submission = study.getSubmission();

		String fileName = pRequest.getParameter("nexusfile");
		if (fileName == null) {
			return null;
		}

		String nexFileName = (fileName + "rct").replaceAll(TreebaseUtil.ANEMPTYSPACE, "_");

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Submission id: " + submission.getId() + ", FileName: " + nexFileName);
		}

		String downloadDir = pRequest.getSession().getServletContext().getRealPath(
			TreebaseUtil.FILESEP + "NexusFileDownload")
			+ TreebaseUtil.FILESEP + pRequest.getRemoteUser();

		long start = System.currentTimeMillis();

		File dirPath = new File(downloadDir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}

		String pathName = downloadDir + TreebaseUtil.FILESEP + nexFileName;
		File file = new File(pathName);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("path=" + pathName);
		}

		FileWriter fwriter = new FileWriter(file);
		StringBuilder bldr = new StringBuilder("#NEXUS");
		bldr.append(TreebaseUtil.getLineSeparators(2));
		bldr
			.append(getStudyService().generateReconstructedNexusFile(submission.getId(), fileName));

		fwriter.write(bldr.toString());
		fwriter.close();
		WebUtil.downloadFile(pResponse, downloadDir, nexFileName);

		if (LOGGER.isDebugEnabled()) {
			long end = System.currentTimeMillis();
			LOGGER.debug(" file download time=" + (end-start));
		}

		return null;
	}

}
