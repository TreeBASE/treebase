package org.cipres.treebase.util;

import org.cipres.treebase.domain.tree.PhyloTreeService;

public interface AnnotateTreeInterface extends Standalone {
	void annotateTree(Long treeId);
	public PhyloTreeService getTreeService();
	public void setTreeService(PhyloTreeService treeService);
}
