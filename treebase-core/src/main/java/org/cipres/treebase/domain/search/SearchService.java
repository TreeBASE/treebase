
package org.cipres.treebase.domain.search;

import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.service.AbstractService;

/**
 * @author mjd 20080717
 *
 */
public interface SearchService extends AbstractService {
	public StudyService getStudyService();
	public void setStudyService(StudyService studyService);
	public PhyloTreeService getPhyloTreeService();
	public void setPhyloTreeService(PhyloTreeService phyloTreeService);
	public MatrixService getMatrixService();
	public void setMatrixService(MatrixService matrixService);	
	public TaxonLabelService getTaxonLabelService();
	public void setTaxonLabelService(TaxonLabelService TaxonLabelService);
	public AbstractService getAppropriateService(SearchResultsType srType);
	public AbstractService getAppropriateService(SearchResults<?> searchResults);
}
