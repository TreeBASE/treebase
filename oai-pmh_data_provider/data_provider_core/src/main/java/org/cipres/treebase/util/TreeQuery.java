package org.cipres.treebase.util;

import java.util.Set;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;

public class TreeQuery extends AbstractStandalone {
	public static void main(String[] args) {
		setupContext();
		PhyloTreeService treeServ = ContextManager.getPhyloTreeService();
		TaxonLabelService tlServ = ContextManager.getTaxonLabelService();
		
		Set<TaxonVariant>[] tvSet = new Set[3];
		for (int i=0; i<3; i++) {
			tvSet[i] = tlServ.findTaxonVariantByName(args[i]);
			warn(args[i] + " yields " + tvSet[i].size() + " matches");
		}
		
		TaxonVariant[] tv = new TaxonVariant[3];
		for (int i=0; i<3; i++) {
			tv[i] = tvSet[i].iterator().next();
			tvShow(tv[i]);
		}		
		
		Set<PhyloTree> trees = treeServ.findByTopology3(tv[0], tv[1], tv[2]);
		warn("Found " + trees.size() + " trees.");
		
		for (PhyloTree t : trees) {
			System.out.println(t.getId());
		}
	}
	
	public static void tvShow(TaxonVariant tv) {
		warn("Tv" + tv.getId() + ": " + tv.getName());
	}
}
