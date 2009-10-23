package org.cipres.treebase.util;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.TBPersistable;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.hibernate.Transaction;

class RepatriateTaxonLabels extends AbstractStandalone {
	
	public static void main(String [] args) {
		RepatriateTaxonLabels rt = new RepatriateTaxonLabels();
		rt.setupContext();
		TaxonLabelService tlService = ContextManager.getTaxonLabelService();

		GetOpts<UnixOptions> go = new GetOpts<UnixOptions>(new UnixOptions ("tm"));
		UnixOptions opts = go.getOpts(args);


		if (! opts.getBoolOpt("m")) {
			for (TBPersistable tbM : ContextManager.getMatrixHome().findAll(Matrix.class)) {
				Transaction tr = rt.beginTransaction();
				Matrix m = (Matrix) tbM;
				Study s = m.getStudy();
				tlService.updateStudyForAllLabels(m, s);
				tr.commit();
				System.err.println("Repatriated labels of matrix " + m.getId());
			}
		}

		if (! opts.getBoolOpt("t")) {
			for (TBPersistable tbT : ContextManager.getPhyloTreeHome().findAll(PhyloTree.class)) {
				Transaction tr = rt.beginTransaction();
				PhyloTree t = (PhyloTree) tbT;
				Study s = t.getStudy();
				System.err.print("Repatriating labels of tree " + t.getId() + "...");
				tlService.updateStudyForAllLabels(t, s);
				tr.commit();
				System.err.println(" Done.");
			}
		}
	}
}
