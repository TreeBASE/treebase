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
			return getNexmlService().serialize(study,getDefaultProperties(request));
		}
		else if ( getFormat(request) == FORMAT_RDF ) {
			Long study_id = ControllerUtil.getStudyId(request);
			Study study = mStudyService.findByID(study_id);		
			return getRdfaService().serialize(study,getDefaultProperties(request));			
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

	@Override
	protected Study getStudy(long objectId, HttpServletRequest request) {
		return ControllerUtil.findStudy(request, mStudyService);
	}

}
