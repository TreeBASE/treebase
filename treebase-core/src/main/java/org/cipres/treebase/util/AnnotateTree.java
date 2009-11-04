
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
