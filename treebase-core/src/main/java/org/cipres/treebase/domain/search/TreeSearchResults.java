
package org.cipres.treebase.domain.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.service.AbstractService;

public class TreeSearchResults extends SearchResults<PhyloTree> {	 

	public TreeSearchResults() { super(); }

	public TreeSearchResults(Boolean isEmpty) {
		super(isEmpty);
	}

	public TreeSearchResults(Collection<PhyloTree> results) {
		super(results);
	}


	public SearchResultsType resultType() { return SearchResultsType.TREE; }


	public StudySearchResults convertToStudies() {
		if (this.isAll()) return new StudySearchResults();
		
		Collection<Study> studies = getStudyService().findByTrees(this.getResults());
		StudySearchResults m = new StudySearchResults (studies);

		return m;		
	} 
	
	// Bill says this can return the matrices from which these trees were derived.
	// That is, it returns all matrix m such that exists analysis step a such that m was the input to a
	// and some tree in this searchresults was the output of a.  20081205 mjd
	// I believe the implementation below meets that requirement. 20082602 rav
	public MatrixSearchResults convertToMatrices() { 		
		Collection<Matrix> pResults = new ArrayList<Matrix>();
		
		Set<PhyloTree> phyloTreeSet = getResults();
		if ( phyloTreeSet != null ) {

			// for each tree in the result set we need to traverse its associated study
			for ( PhyloTree phyloTree : phyloTreeSet ) {
				Study study = phyloTree.getStudy();
				
				// need to resurrect the study, otherwise we get a LazyInitializationException
				Study resurrectedStudy = getStudyService().resurrect(study);
				
				if ( resurrectedStudy != null ) {
					Collection<AnalysisStep> analysisStepCollection = resurrectedStudy.getAnalysisSteps();
					
					// for the study we need to iterate over all analysis steps
					for ( AnalysisStep analysisStep : analysisStepCollection ) {
						Collection<AnalyzedData> analyzedDataCollection = analysisStep.getDataSetReadOnly();
						boolean treeIsOutput = false;
							
						// now we have to check any trees in the analyzedData...
						for ( AnalyzedData analyzedData : analyzedDataCollection ) {
							PhyloTree analyzedTree = analyzedData.getTreeData();
							String inputOutput = analyzedData.getInputOutput();						
							
							// if the datum is a tree, and it's an output, and it's our focal tree...
							if ( inputOutput.equals("output") && analyzedTree != null && analyzedTree.getId() == phyloTree.getId() ) {
								treeIsOutput = true;
							}					
						}
						// ...then...
						if ( treeIsOutput ) {
							for ( AnalyzedData analyzedData : analyzedDataCollection ) {
								Matrix analyzedMatrix = analyzedData.getMatrixData();
								String inputOutput = analyzedData.getInputOutput();
								
								// collect all input matrices
								if ( inputOutput.equals("input") && analyzedMatrix != null ) {
									pResults.add(analyzedMatrix);
								}									
							}
						}
					}
				}
			}	
		}
		if ( pResults.size() > 0 ) {
			return new MatrixSearchResults(pResults);
		}
		else {
			return new MatrixSearchResults();
		}
	}
	
	public TreeSearchResults convertToTrees() { return this; }

	@Override
	public TaxonSearchResults convertToTaxa() {
		if (this.isAll()) return new TaxonSearchResults();

		Set<Taxon> taxa = new HashSet<Taxon> ();
		for (PhyloTree t : getResults()) {
			for (TaxonLabel tl : t.getAllTaxonLabels())
				if (tl.getTaxonVariant() != null && tl.getTaxonVariant().getTaxon() != null)
					taxa.add(tl.getTaxonVariant().getTaxon());
		}
		return new TaxonSearchResults(taxa);
	}

	@Override
	public Class getResultClass() {
		return PhyloTree.class;
	}

	@Override
	public AbstractService getService() {
		return getPhyloTreeService();
	} 
}
