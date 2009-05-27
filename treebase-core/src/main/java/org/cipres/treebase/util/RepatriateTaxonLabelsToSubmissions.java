package org.cipres.treebase.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.hibernate.Transaction;

class RepatriateTaxonLabelsToSubmissions extends AbstractStandalone {
	
	public static void main(String [] args) {
		RepatriateTaxonLabelsToSubmissions rt = new RepatriateTaxonLabelsToSubmissions();
		rt.setupContext();
		TaxonLabelHome tlHome = ContextManager.getTaxonLabelHome();
		
		// Map taxonlabels to the submissions that contain them
		Map<TaxonLabel,List<Submission>> submissions = new HashMap<TaxonLabel,List<Submission>> ();
		for (Object o : tlHome.findAll(Submission.class)) {
			Submission sub = (Submission) o;
			for (TaxonLabel tl : sub.getSubmittedTaxonLabelsReadOnly()) {
				if (! submissions.containsKey(tl)) {
					submissions.put(tl, new LinkedList<Submission> ());
				} 
				submissions.get(tl).add(sub);
			}
		}

		for (Object o : tlHome.findAll(TaxonLabel.class)) {
			TaxonLabel tl = (TaxonLabel) o;
			Submission correctSub = tl.getSubmission();
			List<Submission> otherSubs = submissions.get(tl);

			warn("#" + tl.getId());
			if (otherSubs.size() == 1
					&& otherSubs.iterator().next().equals(correctSub)) {
				warn("  already correct");
				continue;
			}

			Transaction trans = rt.beginTransaction();
						
			for (Submission otherSub : otherSubs) {
				if (otherSub == correctSub) continue;
		
				warn("  removing from " + otherSub.getId());
				otherSub.removeTaxonLabel(tl);
			}
			tlHome.flush();
			warn("  adding to " + correctSub.getId());
			correctSub.addTaxonLabel(tl);
			
			trans.commit();
			warn("  committed");
		}
	}
}
