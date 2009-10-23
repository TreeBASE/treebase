package org.cipres.treebase.util;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.tree.PhyloTree;

public class RepatriateTreeTaxonLabels extends AbstractStandalone {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RepatriateTreeTaxonLabels rt = new RepatriateTreeTaxonLabels();
		rt.setupContext();
		TaxonLabelService tlService = ContextManager.getTaxonLabelService();

		for (String arg : args) {
			Long id = Long.parseLong(arg);
			PhyloTree t = ContextManager.getPhyloTreeService().findByID(id);
			if (t == null) {
				System.err.println(id + ": no tree found");
			} else {
				Study s = t.getStudy();
				if (s == null) {
					System.err.println(id + ": no study");
				} else {
					tlService.updateStudyForAllLabels(t, s);
					System.err.println(id + ": done");
				}
			}
		}
	}
}
