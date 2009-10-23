package org.cipres.treebase.util;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeHome;

/**
 * Delete PhyloTrees.
 * 
 * <P>This deletes the trees themselves, their nodes, and any treeblocks that are empty as a result.
 * It does <i>not</i> delete the taxonlabels.
 * 
 * @author mjd 20090319
 *
 *
 */
public class DeleteTree extends AbstractStandalone implements Standalone {
	
	static PhyloTreeHome treeHome;
	
	public static void main(String[] args) {
		setupContext();
		treeHome = ContextManager.getPhyloTreeHome();

		for (String arg : args) {
			Long tId = Long.decode(arg);
			((DeleteTree) ContextManager.getBean("deleteTree")).doIt(tId);
		}
	}

	public void doIt(Long id) {
		PhyloTree t = treeHome.findPersistedObjectByID(PhyloTree.class, id);
		if (t == null) return;

		treeHome.deleteNodes(t);
		treeHome.delete(t);
	}


}
