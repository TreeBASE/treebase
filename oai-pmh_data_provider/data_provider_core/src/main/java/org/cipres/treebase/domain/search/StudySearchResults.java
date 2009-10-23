/*
 * Copyright 2007 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

package org.cipres.treebase.domain.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.service.AbstractService;

public class StudySearchResults extends SearchResults<Study> {
	private static final Logger LOGGER = Logger.getLogger(StudySearchResults.class);
		
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

