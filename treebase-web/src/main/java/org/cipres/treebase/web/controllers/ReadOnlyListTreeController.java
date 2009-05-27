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
