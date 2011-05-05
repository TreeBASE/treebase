package org.cipres.treebase.domain.search;
 


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
		
		// XXX need to filter out orphaned matrices or matrices whose studies are unpublished
		Collection<Matrix> orphanedMatrices = new HashSet<Matrix>();
		for ( Matrix mat : matrices ) {
			if ( mat.getStudy() == null || (mat.getStudy().isPublished() == false)  ) {
				orphanedMatrices.add(mat);
			}		
		}
		
		matrices.removeAll(orphanedMatrices);
		
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
		
		// XXX need to filter out orphaned matrices or matrices whose studies are unpublished
		Collection<PhyloTree> orphanedTrees = new HashSet<PhyloTree>();
		for ( PhyloTree tree : trees ) {
			if (tree.getStudy() == null || (tree.getStudy().isPublished() == false)){
				orphanedTrees.add(tree);
			}		
		}
		
		trees.removeAll(orphanedTrees);
		
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
