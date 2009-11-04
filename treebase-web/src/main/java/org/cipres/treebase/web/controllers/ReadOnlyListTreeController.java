
package org.cipres.treebase.web.controllers;

import java.util.Collection;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.web.util.ControllerUtil;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.web.Constants;

/**
 * ReadOnlyListTreeController.java
 * 
 * Created on June 23, 2006; This is essentially old List Tree Controller
 * 
 * @author lcchan
 */

public class ReadOnlyListTreeController implements Controller {

	private static final Logger LOGGER = Logger.getLogger(ReadOnlyListTreeController.class);

	private PhyloTreeService mPhyloTreeService;
	private StudyService mStudyService;

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

	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		Study study = ControllerUtil.findStudy(request, mStudyService); // TODO:
		Submission submission = study.getSubmission();
		// old studies do not have submission
		if (submission == null) {
			return new ModelAndView(
				"readOnlyAllTreeList",
				Constants.READONLY_TREE_LIST,
				new ArrayList());
		}

		if (request.getParameter("treeid") != null) {
			getPhyloTreeService().deletePhyloTree(
				getPhyloTreeService().findByID(Long.parseLong(request.getParameter("treeid"))));
		}

		Collection<PhyloTree> phyloTrees = submission.getAllSubmittedTrees();

		return new ModelAndView("readOnlyAllTreeList", Constants.READONLY_TREE_LIST, phyloTrees);
	}

}
