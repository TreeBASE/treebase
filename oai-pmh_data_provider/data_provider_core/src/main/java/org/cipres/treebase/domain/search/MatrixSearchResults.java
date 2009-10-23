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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.AnalysisStep;
import org.cipres.treebase.domain.study.AnalyzedData;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.service.AbstractService;

public class MatrixSearchResults extends SearchResults<Matrix> {

	public MatrixSearchResults() { super(); }

	public MatrixSearchResults(Boolean b) { super(b); }

	public MatrixSearchResults(Collection<Matrix> results) {
		super(results);
	}

	public SearchResultsType resultType() { return SearchResultsType.MATRIX; }

	public StudySearchResults convertToStudies() {
		if (this.isAll()) return new StudySearchResults();

		Collection<Study> studies = new HashSet<Study> ();
		for (Matrix m : this.getResults()) {
			studies.add(m.getStudy());
		}
		return new StudySearchResults(studies);
	} 

	public MatrixSearchResults convertToMatrices() { return this; } 

	// Bill says this can return the trees that were derived from these matrices.
	// That is, it returns all phylotree t such that exists analysis step a such that t was the output of a
	// and some matrix in this searchresults was the input of a.  20081205 mjd
	// The implementation below should meet that requirement. 20092602 rav
	public TreeSearchResults convertToTrees() {		
		Collection<PhyloTree> pResults = new ArrayList<PhyloTree>();
		
		Set<Matrix> matrixSet = getResults();
		if ( matrixSet != null ) {

			// for each matrix in the result set we need to traverse the associated study
			for ( Matrix matrix : matrixSet ) {
				Study study = matrix.getStudy();
				
				// need to resurrect the study, otherwise we get a LazyInitializationException
				Study resurrectedStudy = getStudyService().resurrect(study);
				Collection<AnalysisStep> analysisStepCollection = resurrectedStudy.getAnalysisSteps();
				
				// for that study we need to iterate over all analysis steps
				for ( AnalysisStep analysisStep : analysisStepCollection ) {
					Collection<AnalyzedData> analyzedDataCollection = analysisStep.getDataSetReadOnly();
					boolean matrixIsInput = false;
						
					// then we need to see if the focal matrix is an input matrix
					for ( AnalyzedData analyzedData : analyzedDataCollection ) {
						Matrix analyzedMatrix = analyzedData.getMatrixData();
						String inputOutput = analyzedData.getInputOutput();
							
						// if the focal datum is an input, and it's a matrix, and it's THIS matrix...
						if ( inputOutput.equals("input") && analyzedMatrix != null && analyzedMatrix.getId() == matrix.getId() ) {						
							matrixIsInput = true;
						}						
					}
					// ...then collect all output trees
					if ( matrixIsInput ) {
						for ( AnalyzedData analyzedData : analyzedDataCollection ) {
							PhyloTree analyzedTree = analyzedData.getTreeData();
							String inputOutput = analyzedData.getInputOutput();							
							
							// here we store all output data that are trees
							if ( inputOutput.equals("output") && analyzedTree != null ) {
								pResults.add(analyzedTree);
							}
						}	
					}
				}
			}
		}
		if ( pResults.size() > 0 ) {
			return new TreeSearchResults(pResults);
		}
		else {
			return new TreeSearchResults();
		}
	}

	@Override
	public TaxonSearchResults convertToTaxa() {
		if (this.isAll()) return new TaxonSearchResults();

		Collection<Taxon> taxa = new HashSet<Taxon> ();
		for (Matrix m : getResults()) {
			if (! m.isCharacterMatrix()) continue;
			CharacterMatrix cm = (CharacterMatrix) m;
			for (TaxonLabel tl : cm.getAllTaxonLabels())
				if (tl.getTaxonVariant() != null && tl.getTaxonVariant().getTaxon() != null)
					taxa.add(tl.getTaxonVariant().getTaxon());
		}
		return new TaxonSearchResults(taxa);
	}

	@Override
	public Class getResultClass() {
		return Matrix.class;
	}

	@Override
	public AbstractService getService() {
		return getMatrixService();
	}
}
