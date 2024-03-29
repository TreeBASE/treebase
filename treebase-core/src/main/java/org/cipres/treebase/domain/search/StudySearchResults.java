
package org.cipres.treebase.domain.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.service.AbstractService;

public class StudySearchResults extends SearchResults<Study> {
	private static final Logger LOGGER = LogManager.getLogger(StudySearchResults.class);
		
	public StudySearchResults() { super(); }

	public StudySearchResults(Boolean b) { super(b); }
	
	public StudySearchResults(Collection<Study> results) {
		super(results);
		discardUnpublishedStudies();
	}
	
	// public String resultType() { return "StudySearchResults"; }
	public SearchResultsType resultType() { return SearchResultsType.STUDY; }
	public StudySearchResults convertToStudies() { return this; }

	public MatrixSearchResults convertToMatrices() { 
		Set<Study> studies = this.getResults();
		if (studies == null) { return new MatrixSearchResults(); }

		Collection<Matrix> matrices = getMatrixService().findByStudies(studies);
		MatrixSearchResults m = new MatrixSearchResults (matrices);

		return m;		
	}
	
	public TreeSearchResults convertToTrees() { 
		Set<Study> studies = this.getResults();
		if (studies == null) { return new TreeSearchResults(); }
		Collection<PhyloTree> trees = getPhyloTreeService().findByStudies(studies);
		TreeSearchResults m = new TreeSearchResults (trees);

		return m;		
	}

	public StudySearchResults discardUnpublishedStudies() {
		if (getResults() == null) return this;
		
		Study [] pub;
		pub = getResults().toArray(new Study [0]);
		makeEmpty();
		
		for (Study s : pub) {
			if (s != null && s.isPublished()) {
				if (! s.getAnalysesReadOnly().isEmpty()) { this.mResults.add(s); }
			}
		}
		LOGGER.debug("discardUnpublished: before: " + pub.length + "; after: " + this.mResults.size());
		return this;
	}

	@Override
	public void setResults(Collection<Study> results) {
		super.setResults(results);
		discardUnpublishedStudies();
	}

	@Override
	public TaxonSearchResults convertToTaxa() {
		if (this.isAll()) return new TaxonSearchResults();
		
		Collection<Taxon> taxa = new HashSet<Taxon> ();

		for (Study s : getResults()) 
			taxa.addAll(s.getTaxa());
		
		return new TaxonSearchResults(taxa);
	}

	@Override
	public Class getResultClass() {
		return Study.class;
	}

	@Override
	public AbstractService getService() {
		return getStudyService();
	}

	
}

