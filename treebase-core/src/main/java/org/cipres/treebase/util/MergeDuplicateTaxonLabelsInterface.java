
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
