package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.cipres.treebase.web.util.ControllerUtil;
import org.springframework.web.servlet.ModelAndView;

class SearchMapToPhyloWidgetController extends DirectMapToPhyloWidgetController {
	private SubmissionHome mSubmissionHome;
	private StudyService mStudyService;
	
	public SubmissionHome getSubmissionHome() {
		return mSubmissionHome;
	}

	public void setSubmissionHome(SubmissionHome pSubmissionHome) {
		mSubmissionHome = pSubmissionHome;
	}	
	
	public ModelAndView handleRequest(HttpServletRequest pRequest, HttpServletResponse pResponse)
	throws Exception {
		Study study = null;
		if (pRequest.getParameter("treeid") != null) {
			String treeId = pRequest.getParameter("treeid");
			PhyloTree tree = getPhyloTreeService().findByID(Long.parseLong(treeId));
			study = tree.getStudy();
		}
		if (pRequest.getParameter("treeblockid") != null) {
			String treeBlockId = pRequest.getParameter("treeblockid");
			Long value = Long.parseLong(treeBlockId);
			TreeBlock treeBlock = getPhyloTreeHome().findTreeBlockById(value);
			study = getSubmissionHome().findByTreeBlock(treeBlock).getStudy();
		}
		if ( ControllerUtil.isReviewerAccessGranted(pRequest) || ( study != null && study.isPublished() ) ) {
			return super.handleRequest(pRequest, pResponse);
		}
		else {
			return new ModelAndView("redirect:/accessviolation.html");
		}		
	}

	/**
	 * @param mStudyService the mStudyService to set
	 */
	public void setStudyService(StudyService mStudyService) {
		this.mStudyService = mStudyService;
	}

	/**
	 * @return the mStudyService
	 */
	public StudyService getStudyService() {
		return mStudyService;
	}
}