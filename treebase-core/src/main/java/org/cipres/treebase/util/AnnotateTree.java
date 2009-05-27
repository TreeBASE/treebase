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

package org.cipres.treebase.util;

import java.util.logging.Logger;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeNode;
import org.cipres.treebase.domain.tree.PhyloTreeService;

/**
 * annotateTree.java
 * 
 * Created on Jun 6, 2006
 * 
 * @author mjd
 * 
 */
public class AnnotateTree extends AbstractStandalone implements AnnotateTreeInterface {
		
	private PhyloTreeService treeService;
	
	@SuppressWarnings(value = "unused")
	private static final Logger LOGGER = Logger.getLogger("AnnotateTree");

	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.err.println("Usage: thisProgram treeID");
			System.exit(1);
		}

		setupContext();
		
		AnnotateTreeInterface me = (AnnotateTreeInterface) ContextManager.getBean("annotateTree");
		me.doIt(args);
	}
	
	public void doIt(String[] args) {
		Long treeID = Long.decode(args[0]);
		annotateTree(treeID);
	}

	public PhyloTreeService getTreeService() {
		return treeService;
	}

	public void setTreeService(PhyloTreeService treeService) {
		this.treeService = treeService;
	}

	public void annotateTree(Long treeId) {
		PhyloTree t = getTreeService().findByID(treeId);
		if (t == null) {
			getErrorStream().println("Tree " + treeId + ": No such tree");
			System.exit(1);
		}
		t = getTreeService().resurrect(t);
		
		getErrorStream().println("Got tree " + t.getId() + " '" + t.getTitle() + "'");

	    PhyloTreeNode root = t.getRootNode();
	    long left = root.getLeftNode(), right = 0;
	    if (left != 0) { right = root.getRightNode(); }
	    
	    if (left == 0 || right == 0) {
	    	t.updateSubtreeBounds();
		    getErrorStream().println("Finished with tree " + treeId + ".");
	    } else {
	    	getErrorStream().println("Tree " + treeId + " already annotated");
	    }
	}
}
