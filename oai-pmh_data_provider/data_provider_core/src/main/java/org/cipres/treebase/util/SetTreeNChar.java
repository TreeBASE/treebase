package org.cipres.treebase.util;

import java.util.Collection;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeNode;
import org.cipres.treebase.domain.tree.PhyloTreeService;

public class SetTreeNChar extends AbstractStandalone implements SetTreeNCharInterface {

	static PhyloTreeService treeService;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		setupContext();
		SetTreeNCharInterface me = (SetTreeNCharInterface) ContextManager.getBean("setTreeNChar");
		treeService  = (PhyloTreeService) ContextManager.getBean("phyloTreeService");


		Collection<PhyloTree> trees = ContextManager.getPhyloTreeHome().findAll(PhyloTree.class);
		int nTrees = trees.size();
		int count = 0;
		
		for (PhyloTree t : trees) {
			int nTax = me.setTreeNChar(t);
			count++;
			int pct = (100 * count) / nTrees;
			warn(pct + "% Tree Tr" + t.getId() + " now has nTAX = " + nTax);
		}
			
	}

	public int setTreeNChar(PhyloTree tDetached) {
		PhyloTree t = treeService.findByID(tDetached.getId());
		int nTax = 0;
		for (PhyloTreeNode n : t.getTreeNodesReadOnly())
			if (n.isLeaf()) nTax++;
		t.setnTax(nTax);
		return nTax;
	}
}
