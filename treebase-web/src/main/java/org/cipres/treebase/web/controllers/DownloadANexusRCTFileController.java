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

/**
 * @author madhu
 * 
 * Created on March 17, 2008
 * 
 * Modified on April 28, 2008
 * 
 */
public class DownloadANexusRCTFileController extends AbstractDownloadController implements Controller {

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
		generateAFileDynamically(pRequest, pResponse, submission.getId());
		return null;
	}

	@Override
	protected String getFileName(long objectId,HttpServletRequest req) {
		String fileName = req.getParameter("nexusfile");
		if (fileName == null) {
			return null;
		}
		return (fileName + "rct").replaceAll(TreebaseUtil.ANEMPTYSPACE, "_");		
	}

	@Override
	protected String getFileNamePrefix() {
		// Not necessary because we override getFileName
		return null;
	}

	@Override
	protected String getFileContent(long submisionId, HttpServletRequest request) {
		if ( getFormat(request) == FORMAT_NEXML ) {
			Long study_id = ControllerUtil.getStudyId(request);
			Study study = mStudyService.findByID(study_id);
			return getNexmlService().serialize(study);
		}
		else if ( getFormat(request) == FORMAT_RDF ) {
			Long study_id = ControllerUtil.getStudyId(request);
			Study study = mStudyService.findByID(study_id);		
			return getRdfaService().serialize(study);			
		}		
		else {
			String fileName = request.getParameter("nexusfile");
			if (fileName == null) {
				return null;
			}		
			StringBuilder bldr = new StringBuilder("#NEXUS");
			bldr
				.append(TreebaseUtil.getLineSeparators(2))
				.append(getStudyService()
						.generateReconstructedNexusFile(submisionId, fileName));
			return bldr.toString();
		}
	}

}
