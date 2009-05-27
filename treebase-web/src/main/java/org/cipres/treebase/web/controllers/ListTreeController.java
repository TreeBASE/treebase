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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.SubmissionHome;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.cipres.treebase.domain.tree.TreeQuality;
import org.cipres.treebase.domain.tree.PhyloTreeHome;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.web.model.AGenericList;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * ListTreeController.java Generated on July, 2007
 * 
 * @author madhu
 * 
 * This is new incarnation of ListTreeController. Old ListTreeController has been copied to
 * ReadOnlyListTreeController.
 * 
 * Now individual PhyloTrees can be deleted, down loaded, viewed as line graph and Tree Labels &
 * Titles can be altered. In addition, original nexus files can be down loaded and tree-types can be
 * altered.
 */

public class ListTreeController extends BaseFormController {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(ListTreeController.class);

	private PhyloTreeService mPhyloTreeService;
	private StudyService mStudyService;
	private PhyloTreeHome mPhyloTreeHome;
	private SubmissionHome mSubmissionHome;
	private SubmissionService mSubmissionService;

	public SubmissionHome getSubmissionHome() {
		return mSubmissionHome;
	}

	public void setSubmissionHome(SubmissionHome pSubmissionHome) {
		mSubmissionHome = pSubmissionHome;
	}

	private SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	/**
	 * Set the SubmissionService field.
	 */
	public void setSubmissionService(SubmissionService pNewSubmissionService) {
		mSubmissionService = pNewSubmissionService;
	}

	public PhyloTreeHome getPhyloTreeHome() {
		return mPhyloTreeHome;
	}

	/**
	 * Set the PhyloTreeService
	 */
	public void setPhyloTreeHome(PhyloTreeHome pPhyloTreeHome) {
		mPhyloTreeHome = pPhyloTreeHome;
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

	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException myerrors) throws Exception {

		AGenericList<Collection<PhyloTree>> treeList = (AGenericList<Collection<PhyloTree>>) command;

		if (request.getParameter(ACTION_UPDATE) != null) {

			Collection<PhyloTree> phyloTrees = (Collection<PhyloTree>) treeList.getMyList();
			List<Object> testList = checkLength(phyloTrees, TBPersistable.COLUMN_LENGTH_STRING);
			Boolean check = (Boolean) testList.get(0);

			if (!check) {
				testList.remove(0);
				request.setAttribute("errors", testList);
				return showForm(request, response, myerrors);
			} 
			else {
				for (PhyloTree atree : phyloTrees) {
					String treeType = atree.getTypeDescription();
					atree.setTreeType(getPhyloTreeHome().findTypeByDescription(treeType));
					String treeKind = atree.getKindDescription();
					atree.setTreeKind(getPhyloTreeHome().findKindByDescription(treeKind));
					String treeQuality = atree.getQualityDescription();
					TreeQuality quality = getPhyloTreeHome().findQualityByDescription(treeQuality);
					atree.setTreeQuality(quality);
				}
				getPhyloTreeService().updateCollection(phyloTrees);
			}
		}

		return new ModelAndView(getSuccessView());
	}

	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		// Study study = ControllerUtil.findStudy(request, mStudyService);
		String TBID = request.getParameter("treeblockid");

		if (TBID == null) {
			TBID = ControllerUtil.getTreeBlockId(request);
		}

		if (TBID == null) {
			return new AGenericList<Collection<PhyloTree>>(new ArrayList<PhyloTree>());
		}

		Long value = Long.parseLong(TBID);

		TreeBlock treeblock = getPhyloTreeHome().findTreeBlockById(value);
		// It can happen that the TBID is saved but the tree-block has been deleted
		// Or the only tree in the block has been removed.
		if (treeblock != null) {

			Long submission_id = getSubmissionHome().findByTreeBlock(treeblock).getId();
			TBPermission perm2 = getSubmissionService().getPermission(
				request.getRemoteUser(),
				submission_id);
			if (perm2 == TBPermission.WRITE || perm2 == TBPermission.SUBMITTED_WRITE) {
				setAuthorizationChecked(true);
				Collection<PhyloTree> phyloTreeList = treeblock.getTreeList();
				ControllerUtil.setTreeBlockId(TBID, request);
				checkAnalyzed(phyloTreeList,ControllerUtil.findStudy(request, mStudyService));
				// This ID has to be saved for success/cancel view.
				return new AGenericList<Collection<PhyloTree>>(phyloTreeList);
			} else {
				setAuthorizationChecked(false);
				return new AGenericList<Collection<PhyloTree>>(new ArrayList<PhyloTree>());
			}

		} else {
			return new AGenericList<Collection<PhyloTree>>(new ArrayList<PhyloTree>());
		}

	}
	
	private void checkAnalyzed(Collection<PhyloTree> mylist, Study study) {	
		Iterator<PhyloTree> treeIterator = mylist.iterator();
		while ( treeIterator.hasNext() ) {
			PhyloTree tree = treeIterator.next();
			if ( tree.isPublished() ) {
				tree.setAnalyzed(true);
			}
			else {
				if ( study != null ) {
					Iterator<PhyloTree> analyzedTreeIterator = study.getTrees().iterator();
					ANALYZED: while ( analyzedTreeIterator.hasNext() ) {
						PhyloTree analyzedTree = analyzedTreeIterator.next();
						if ( analyzedTree.getId() == tree.getId() ) {
							tree.setAnalyzed(true);
							break ANALYZED;
						}
					}
				}
				else {
					tree.setAnalyzed(false);
				}
			}			
		}		
	}	

	private List<Object> checkLength(Collection<PhyloTree> mylist, int permittedlength) {

		String oversizeTitlerows = "Please fix, following rows have over " + permittedlength
			+ " characters in the Title:  ";

		String oversizeLabelrows = "Please fix, following rows have over " + permittedlength
			+ " characters in the Label:  ";

		List<Object> returnedList = new ArrayList<Object>();
		Boolean check = true;
		StringBuilder oversizeTitlerowsbldr = new StringBuilder("");
		StringBuilder oversizeLabelrowsbldr = new StringBuilder("");
		Iterator<PhyloTree> treeIter = mylist.iterator();

		int i = 0;
		while (treeIter.hasNext()) {
			PhyloTree aTree = treeIter.next();
			if ((aTree.getTitle() != null) && (aTree.getTitle().trim().length() > permittedlength)) {
				check = false;
				oversizeTitlerowsbldr.append(String.valueOf(i + 1)).append(",");
			}
			if ((aTree.getLabel() != null) && (aTree.getLabel().trim().length() > permittedlength)) {
				check = false;
				oversizeLabelrowsbldr.append(String.valueOf(i + 1)).append(",");
			}
			i++;
		}

		returnedList.add(check);

		if (oversizeTitlerowsbldr.toString().trim().length() > 0) {
			returnedList.add(oversizeTitlerows + oversizeTitlerowsbldr.toString());
		}
		if (oversizeLabelrowsbldr.toString().trim().length() > 0) {
			returnedList.add(oversizeLabelrows + oversizeLabelrowsbldr.toString());
		}

		return returnedList;
	}

	protected ModelAndView disallowDuplicateFormSubmission(
		HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		BindException errors = new BindException(formBackingObject(request), getCommandName());
		errors.reject("duplicateFormSubmission", null, "Duplicate form submission");
		return showForm(request, response, errors);
	}

	protected ModelAndView handleInvalidSubmit(
		HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Inside Handle Invalid Submit");
		}
		return disallowDuplicateFormSubmission(request, response);
	}

}
