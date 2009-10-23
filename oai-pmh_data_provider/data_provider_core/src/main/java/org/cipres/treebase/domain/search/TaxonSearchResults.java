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
 */package org.cipres.treebase.domain.search;
 


import java.util.Collection;
import java.util.HashSet;

import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.service.AbstractService;

public class TaxonSearchResults extends SearchResults<Taxon> {

	public TaxonSearchResults() { super(); }

	public TaxonSearchResults(Boolean isEmpty) {
		super(isEmpty);
	}

	public TaxonSearchResults(Collection<Taxon> results) {
		super(results);
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.search.SearchResults#convertToMatrices()
	 */
	@Override
	public MatrixSearchResults convertToMatrices() {
		if (this.isAll()) return new MatrixSearchResults();
		Collection<Matrix> matrices = new HashSet<Matrix> ();
		
		for (Taxon m : getResults()) {
			matrices.addAll(getTaxonLabelService().findMatrices(m));
		}
		return new MatrixSearchResults(matrices);
	}

	@Override
	public StudySearchResults convertToStudies() {
		if (this.isAll()) return new StudySearchResults();

		Collection<Study> studies = new HashSet<Study> ();
		for (Taxon t : this.getResults()) {
			studies.addAll(getTaxonLabelService().findStudies(t));
		}
		return new StudySearchResults(studies);
	
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.search.SearchResults#convertToTrees()
	 */
	@Override
	public TreeSearchResults convertToTrees() {
		if (this.isAll()) return new TreeSearchResults();
		Collection<PhyloTree> trees = new HashSet<PhyloTree> ();
		
		for (Taxon t : getResults()) {
			trees.addAll(getTaxonLabelService().findTrees(t));
		}
		return new TreeSearchResults(trees);
	}

	/* (non-Javadoc)
	 * @see org.cipres.treebase.domain.search.SearchResults#resultType()
	 */
	@Override
	public SearchResultsType resultType() {
		return SearchResultsType.TAXON;
	}

	@Override
	public TaxonSearchResults convertToTaxa() {
		return this;
	}

	@Override
	public Class getResultClass() {
		return Taxon.class;
	}

	@Override
	public AbstractService getService() {
		return getTaxonLabelService();
	}

}
