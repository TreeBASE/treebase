package org.cipres.treebase.web.controllers;

/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.cipres.treebase.web.model.AGenericList;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * ListTreeController.java
 * 
 * Retrieve the list of trees user has uploaded from nexus files
 * 
 * Created on Jun 23, 2006
 * 
 * @author lcchan
 * 
 * Modified by
 * @author Madhu on June 27, 2007 Now individual PhyloTrees can be deleted and Tree Labels & Titles
 *         can be altered
 * 
 */

public class ListTreeBlockController extends BaseFormController {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(ListTreeBlockController.class);

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

	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException myerrors) throws Exception {

		AGenericList<Collection<TreeBlock>> aBlockColection = (AGenericList<Collection<TreeBlock>>) command;
		Collection<TreeBlock> ablkcollection = (Collection<TreeBlock>) aBlockColection.getMyList();

		if (request.getParameter(ACTION_UPDATE) != null) {

			List<Object> testList = checkLength(ablkcollection, TBPersistable.COLUMN_LENGTH_STRING);
			Boolean check = (Boolean) testList.get(0);

			if (!check) {
				testList.remove(0);
				request.setAttribute("errors", testList);
				return showForm(request, response, myerrors);
			} else {
				getPhyloTreeService().updateCollection(ablkcollection);
			}
		}

		return new ModelAndView(getSuccessView());
	}

	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */

	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		Study study = ControllerUtil.findStudy(request, mStudyService);
		Collection<TreeBlock> treeBlockList = study
			.getSubmission().getSubmittedTreeBlocksReadOnly();
		checkAnalyzed(treeBlockList,study);
		List<TreeBlock> test = new ArrayList<TreeBlock>();
		test.addAll(treeBlockList);
		return new AGenericList<List<TreeBlock>>(test);
	}
	
	private void checkAnalyzed(Collection<TreeBlock> mylist, Study study) {	
		Iterator<TreeBlock> treeBlockIterator = mylist.iterator();
		while ( treeBlockIterator.hasNext() ) {
			TreeBlock treeBlock = treeBlockIterator.next();
			if ( study != null ) {
				Iterator<PhyloTree> analyzedTreeIterator = study.getTrees().iterator();
				ANALYZED: while ( analyzedTreeIterator.hasNext() ) {
					PhyloTree analyzedTree = analyzedTreeIterator.next();
					if ( analyzedTree.getTreeBlock().getId() == treeBlock.getId() ) {
						treeBlock.setAnalyzed(true);
						break ANALYZED;
					}
				}
			}
			else {
				treeBlock.setAnalyzed(false);
			}			
		}		
	}	

	/**
	 * This method is used to check length of the title.
	 * 
	 * @param mylist
	 * @param permittedlength
	 * @return
	 */

	private List<Object> checkLength(Collection<TreeBlock> mylist, int permittedlength) {

		String oversizerows = "Please fix, following rows have over " + permittedlength
			+ " characters in the Title:  ";

		List<Object> returnedList = new ArrayList<Object>();
		Boolean check = true;
		StringBuilder oversizerowsbldr = new StringBuilder("");
		Iterator<TreeBlock> treeblocks = mylist.iterator();

		int i = 0;
		while (treeblocks.hasNext()) {
			TreeBlock ablock = treeblocks.next();
			if (ablock.getTitle().trim().length() > permittedlength) {
				check = false;
				oversizerowsbldr.append(String.valueOf(i + 1));
				oversizerowsbldr.append(",");
			}
			i++;
		}

		returnedList.add(check);

		if (oversizerowsbldr.toString().trim().length() > 0) {
			returnedList.add(oversizerows + oversizerowsbldr.toString());
		}

		return returnedList;
	}

}
