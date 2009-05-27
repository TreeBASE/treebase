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

package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.admin.UserService;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.nexus.NexusService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeNode;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * TestParserResultController.java
 * 
 * Created on May 18, 2007
 * 
 * @author jruan
 * 
 */
public class TreeParserResultController extends BaseFormController {
	private static final Logger LOGGER = Logger.getLogger(TreeParserResultController.class);

	private UserService mUserService;

	private SubmissionService mSubmissionService;

	private StudyService mStudyService;

	private PhyloTreeService mPhyloTreeService;

	private NexusService mNexusService;

	// private StringBuilder xmlPlaceHolder = new StringBuilder();

	public PhyloTreeService getPhyloTreeService() {
		return mPhyloTreeService;
	}

	/**
	 * Set the NexusService field.
	 */
	public void setPhyloTreeService(PhyloTreeService pNewPhyloTreeService) {
		mPhyloTreeService = pNewPhyloTreeService;
	}

	/**
	 * Return the NexusService field.
	 * 
	 * @return NexusService mNexusService
	 */
	public NexusService getNexusService() {
		return mNexusService;
	}

	/**
	 * Set the NexusService field.
	 */
	public void setNexusService(NexusService pNewNexusService) {
		mNexusService = pNewNexusService;
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
	 * Return the SubmissionService field.
	 * 
	 * @return SubmissionService mSubmissionService
	 */
	public SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	/**
	 * Set the SubmissionService field.
	 */
	public void setSubmissionService(SubmissionService pNewSubmissionService) {
		mSubmissionService = pNewSubmissionService;
	}

	/**
	 * Return the UserService field.
	 * 
	 * @return UserService mUserService
	 */
	public UserService getUserService() {
		return mUserService;
	}

	/**
	 * Set the UserService field.
	 */
	public void setUserService(UserService pNewUserService) {
		mUserService = pNewUserService;
	}

	/**
	 * 
	 * Creation date: June 3, 2006 2:24:42 PM
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		Study s = ControllerUtil.findStudy(request, getStudyService());
		ControllerUtil.saveStudy(request, s); // user has made selection

		return s;
	}

	public StringBuilder treesParser(Collection<PhyloTree> treesList) {
		StringBuilder xmlInfo = new StringBuilder();
		printStartTag("TREES", xmlInfo);

		for (PhyloTree tmptree : treesList) {

			printTreeTag(tmptree, xmlInfo);

			treeAnalyzer(tmptree, xmlInfo);

			printEndTag("ROOT-Node", xmlInfo);
			printEndTag("TREE", xmlInfo);
		}
		printEndTag("TREES", xmlInfo);
		return xmlInfo;
	}

	/**
	 * <p>
	 * This method analyses the PhyoTree and checks for the RootNode and its children.
	 * </p>
	 * 
	 * @param PhyloTree
	 */

	public void treeAnalyzer(PhyloTree mytree, StringBuilder xmlPlaceHolder) {
		PhyloTreeNode rootNode = mytree.getRootNode();
		printNodeProperties(rootNode, xmlPlaceHolder);
		List<PhyloTreeNode> firstlevelnodes = rootNode.getChildNodes();

		nodesListParser(firstlevelnodes, xmlPlaceHolder);

	}

	/**
	 * <p>
	 * This method traverses through the List containing PhyloTreeNodes.
	 * </p>
	 * 
	 * @param List <PhyloTreeNode>
	 */
	public void nodesListParser(List<PhyloTreeNode> nodesList, StringBuilder xmlPlaceHolder) {

		for (int i = 0; i < nodesList.size(); i++) {

			PhyloTreeNode tmpnode = nodesList.get(i);

			nodeAnalyzer(tmpnode, xmlPlaceHolder);

			if (!tmpnode.isLeaf()) {
				printEndTag("Node", xmlPlaceHolder);
			}

		}

	}

	/**
	 * <p>
	 * This method analyzes the PhyoTreeNode depending upon whether it is a leaf or has children and
	 * passes the node to nodeListParser method.
	 * </p>
	 * 
	 * @param PhyloTreeNode
	 */

	public void nodeAnalyzer(PhyloTreeNode ptn, StringBuilder xmlPlaceHolder) {
		printNodeProperties(ptn, xmlPlaceHolder);

		if (!ptn.isLeaf()) nodesListParser(ptn.getChildNodes(), xmlPlaceHolder);

	}

	/**
	 * <p>
	 * This method prints PhyloTreeNode's properties.
	 * </p>
	 * 
	 * @param PhyloTreeNode
	 */
	public void printNodeProperties(PhyloTreeNode ptnode, StringBuilder xmlPlaceHolder) {
		if (ptnode.isRootNode()) {
			xmlPlaceHolder.append("\t<ROOT-");

		}

		else if (ptnode.isLeaf()) {
			xmlPlaceHolder.append("\t\t<LEAF-");

		}

		else {
			xmlPlaceHolder.append("\t\t<");

		}
		// Only Leaf Node has name
		if (ptnode.isLeaf()) {

			xmlPlaceHolder.append("Node Id = " + "\"" + ptnode.getId() + "\"" + "\t\tName = "
				+ "\"" + ptnode.getTaxonLabelOrName() + "\"" + ">");

		} else {
			if (ptnode.getTaxonLabelOrName() == null) {
				xmlPlaceHolder.append("Node Id = " + "\"" + ptnode.getId() + "\"" + ">");

			} else {
				xmlPlaceHolder.append("Node Id = " + "\"" + ptnode.getId() + "\"" + "\t\tName = "
					+ "\"" + ptnode.getTaxonLabelOrName() + "\"" + ">");
			}

		}

		if (ptnode.hasBranchLength()) {
			xmlPlaceHolder.append("<Branch Length = " + "\"" + ptnode.getBranchLength().doubleValue() + "\""
				+ "/>");

		}

		if (ptnode.isLeaf()) {
			printEndTag("LEAF-Node", xmlPlaceHolder);
		}

	}

	public void printTreeTag(PhyloTree ptree, StringBuilder xmlPlaceHolder) {
		xmlPlaceHolder.append("<TREE Label = " + "\"" + ptree.getLabel() + "\"" + ">");
	}

	public void printStartTag(String value, StringBuilder xmlPlaceHolder) {
		xmlPlaceHolder.append("<" + value + ">\n");
	}

	public void printEndTag(String value, StringBuilder xmlPlaceHolder) {
		xmlPlaceHolder.append("\t\t</" + value + ">\n");

	}

	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Map<String,Object> referenceData(HttpServletRequest pRequest) throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Study study = ControllerUtil.findStudy(pRequest, getStudyService());
		Collection<Matrix> matrices = study.getSubmission().getSubmittedMatricesReadOnly();
		Collection<PhyloTree> trees;

		if (pRequest.getParameter("id") == null) {
			trees = study.getSubmission().getAllSubmittedTrees();
		} else {
			trees = new ArrayList<PhyloTree>();
			trees.add(getPhyloTreeService().findByID(Long.parseLong(pRequest.getParameter("id"))));
		}

		String treesSize = String.valueOf(trees.size());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("++++++++++++++++" + trees.size());
		}
		// FIXME: referenceData

		StringBuilder xmlData = treesParser(trees);
		System.out.println(xmlData.toString());

		pRequest.getSession().setAttribute("xmlTrees", xmlData.toString());
		xmlData.delete(0, xmlData.length() - 1);

		resultMap.put("treesSize", treesSize);
		resultMap.put("matrices", matrices);
		resultMap.put("trees", trees);

		return resultMap;
	}

	/**
	 * 
	 * Creation date: May 18, 2006 6:33:54 PM
	 */
	@Override
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering onSubmit()..."); //$NON-NLS-1$
		}

		if (request.getParameter(ACTION_DELETE) != null) {

			Study study = ControllerUtil.findStudy(request, getStudyService());
			getSubmissionService().deleteSubmittedData(study.getSubmission());
		}
		// TODO Submission submission = study.getSubmission();
		// MyProgressionListener listener = new MyProgressionListener();
		// mSubmissionService.addNexusFiles(submission, files, listener);

		return new ModelAndView(getSuccessView());
	}
}
