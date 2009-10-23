/*
 * Copyright 2009 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.util;

import java.util.Map;

import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixRowHome;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyHome;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeHome;

/**
 * @author mjd 20090223
 *
 */
public interface MergeDuplicateTaxonLabelsInterface extends Standalone {
	public StudyHome getStudyHome();
	public void setStudyHome(StudyHome studyHome);
	public TaxonLabelHome getTaxonLabelHome();
	public void setTaxonLabelHome(TaxonLabelHome taxonLabelHome);
	public PhyloTreeHome getTreeHome();
	public void setTreeHome(PhyloTreeHome treeHome);
	public MatrixRowHome getMatrixRowHome();
	public void setMatrixRowHome(MatrixRowHome matrixRowHome);
	
	public void remapStudyTaxonLabels(Study s);
	public void splitStudyTaxonLabels(Study s);
	
	public void remapPhyloTreeTaxonLabels(PhyloTree t,
			Map<TaxonLabel, TaxonLabel> canonical);
	public void remapTaxonLabelSetTaxonLabels(TaxonLabelSet tls,
			Map<TaxonLabel, TaxonLabel> canonical);
	public void remapMatrixTaxonLabels(Matrix matrix,
			Map<TaxonLabel, TaxonLabel> canonical) ;
}
