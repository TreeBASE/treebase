package org.cipres.treebase.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.cipres.treebase.ContextManager;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Analysis;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyHome;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.TreeBlock;
import org.hibernate.Hibernate;

public class RegenerateTaxonLabelSets extends AbstractStandalone implements RegenerateTaxonLabelSetsInterface {

	TaxonLabelService tlService;
	TaxonLabelHome tlHome;
	StudyService studyService;
	StudyHome studyHome;
	
	public static void main(String [] args) {
		setupContext();
		RegenerateTaxonLabelSetsInterface rt = (RegenerateTaxonLabelSetsInterface) ContextManager.getBean("regenerateTaxonLabelSets");
	
		GetOpts<UnixOptions> go = new GetOpts<UnixOptions>(new UnixOptions ("tm"));
		UnixOptions opts = go.getOpts(args);
		
		// Kind of silly to convert the IDs to strings just to convert them back to numbers again...
		if (args.length == 0) {
			Collection<Study> allStudies = rt.getStudyHome().findAll(Study.class);
			List<String> newArgs = new LinkedList<String> ();
			for (Study s : allStudies) 
				newArgs.add(s.getId().toString());
			args = newArgs.toArray(args);
		}
		
		for (String arg : args) {
			Long id = Long.parseLong(arg);
			System.err.println("Processing study " + id);

			Set<TaxonLabelSet> newSets = rt.createTaxonLabelSets(id, opts.getBoolOpt("t"), opts.getBoolOpt("m"));
			rt.getTlHome().flush();
			/*	
			 * Do this another time, just destroy all sets that should be garbage-collected
			for (TaxonLabelSet tlSet : oldSets) {
				tlHome.deletePersist(tlSet);	// XXX Should be taxonLabelSetHome
			}
//			tlHome.flush();    // XXX Should be taxonLabelSetHome

			 */

			String sets = newSets.size() == 1 ? "set" : "sets";
			System.err.println("  Rebuilt " + newSets.size() + " taxon label " + sets + " for study " + id);
		}
	}

	private static void addToSetIfNecessary(TaxonLabelSet tlSet, Collection<TaxonLabel> labels) {
		Set<TaxonLabel> tlList = new HashSet<TaxonLabel>(tlSet.getTaxonLabelsReadOnly());
		for (TaxonLabel label : labels) {
			if (! tlList.contains(label)) {
				tlList.add(label);
				tlSet.addPhyloTaxonLabel(label);
			}
		}
	}

	public Set<TaxonLabelSet> createTaxonLabelSets(Long studyId, boolean justTrees, boolean justMatrices) {
		bindSession();
		if (studyId == null) {
			Set<TaxonLabelSet> result = new HashSet<TaxonLabelSet> ();
			return result;
		} else {
			Study s = getStudyHome().findPersistedObjectByID(Study.class, studyId);
			if (s == null) {
				System.err.println("No study with id=" + studyId);
				unbindSession();
				return null;
			}

			unbindSession();
			return createTaxonLabelSets(s, false, false);
		}
	}
	
	public Set<TaxonLabelSet> createTaxonLabelSets(Study s, boolean justTrees, boolean justMatrices) {
		bindSession();		

		Set<TaxonLabelSet> newSets = new HashSet<TaxonLabelSet> ();
		Set<TaxonLabelSet> oldSets = s.getTaxonLabelSets();
		Hibernate.initialize(oldSets);

		for (Analysis an : s.getAnalyses()) {
			TaxonLabelSet theSet = new TaxonLabelSet();
			theSet.setStudy(s);
			theSet.setTitle(an.getName());
			Collection<TreeBlock> treeBlocks = new LinkedList<TreeBlock> ();

			for (AnalysisStep as : an.getAnalysisStepsReadOnly()) {
				for (AnalyzedData ad : as.getDataSetReadOnly()) {
					Matrix m = ad.getMatrixData();
					PhyloTree t = ad.getTreeData();

					if (m != null && ! justTrees) {
						addToSetIfNecessary(theSet, m.getAllTaxonLabels());
						m.setTaxa(theSet);
//						tlService.updateStudyForAllLabels(m, s);
					}
					if (t != null && ! justMatrices) {
						addToSetIfNecessary(theSet, t.getAllTaxonLabels());
//						tlService.updateStudyForAllLabels(t, s);
						treeBlocks.add(t.getTreeBlock());
					}		
				}
			}

			ContextManager.getTaxonLabelService().save(theSet);
			for (TreeBlock tb : treeBlocks) {
				tb.setTaxonLabelSet(theSet);
				ContextManager.getTaxonLabelService().save(tb);  // XXX Should be treeBlockService
			}
			newSets.add(theSet);
		}

		s.setTaxonLabelSets(newSets);
		
		unbindSession();
		return newSets;
	}

	public TaxonLabelService getTlService() {
		return tlService;
	}

	public void setTlService(TaxonLabelService tlService) {
		this.tlService = tlService;
	}

	public TaxonLabelHome getTlHome() {
		return tlHome;
	}

	public void setTlHome(TaxonLabelHome tlHome) {
		this.tlHome = tlHome;
	}

	public StudyService getStudyService() {
		return studyService;
	}

	public void setStudyService(StudyService studyService) {
		this.studyService = studyService;
	}

	public StudyHome getStudyHome() {
		return studyHome;
	}

	public void setStudyHome(StudyHome studyHome) {
		this.studyHome = studyHome;
	}

	public Set<TaxonLabelSet> createTaxonLabelSets(Study s) {
		return createTaxonLabelSets(s, false, false);
	}

	public Set<TaxonLabelSet> createTaxonLabelSets(Long sid) {
		return createTaxonLabelSets(sid, false, false);
	}
}
