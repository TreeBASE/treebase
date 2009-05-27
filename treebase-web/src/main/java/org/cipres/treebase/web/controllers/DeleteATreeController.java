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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;

public class DeleteATreeController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(DeleteATreeController.class);

	private PhyloTreeService mPhyloTreeService;
	private SubmissionHome mSubmissionHome;
	private SubmissionService mSubmissionService;

	private SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	/**
	 * Set the SubmissionService field.
	 */
	public void setSubmissionService(SubmissionService pNewSubmissionService) {
		mSubmissionService = pNewSubmissionService;
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

	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		String username = request.getRemoteUser();
		PhyloTree atree = (PhyloTree) command;

		if (request.getParameter("treeid") != null) {

			Long submission_id = getSubmissionHome().findByTree(atree).getId();

			TBPermission perm2 = getSubmissionService().getPermission(username, submission_id);

			if (perm2 == TBPermission.WRITE) {
				getPhyloTreeService().deletePhyloTree(atree);
			} else {
				return setAttributeAndShowForm(
					request,
					response,
					bindExp,
					"errors",
					"Sorry Authorization Failure, you cannot delete this tree.");

			}
		}

		return new ModelAndView(getSuccessView());
		// IMPORTANT: in handle request RedirectView one cannot use /user/treeList.html
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) {

		if (request.getParameter("treeid") != null) {

			Long treeID = Long.parseLong(request.getParameter("treeid"));

			PhyloTree pTree = getPhyloTreeService().findByID(treeID);
			return pTree;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Map<String, String> referenceData(HttpServletRequest request) {

		Map<String, String> referenceMap = new HashMap<String, String>();
		referenceMap.put("deleteelementtype", "Delete this particular Tree");

		Long treeID = Long.parseLong(request.getParameter("treeid"));
		PhyloTree pTree = getPhyloTreeService().findByID(treeID);

		if (pTree.getStudy() != null) { // It means this tree is tied to analysis step
			referenceMap.put("generalmessage", "This PhyloTree is tied to an Analysis Step.");
		}

		referenceMap.put("deleteid", "Tree ID : " + treeID);
		referenceMap.put("objectname", "Tree Label : " + pTree.getLabel());

		return referenceMap;
	}

	public SubmissionHome getSubmissionHome() {
		return mSubmissionHome;
	}

	public void setSubmissionHome(SubmissionHome pSubmissionHome) {
		mSubmissionHome = pSubmissionHome;
	}
}
