
package org.cipres.treebase.service.search;

import org.cipres.treebase.domain.DomainHome;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.search.SearchResults;
import org.cipres.treebase.domain.search.SearchResultsType;
import org.cipres.treebase.domain.search.SearchService;
import org.cipres.treebase.domain.search.UnknownSearchResultsTypeError;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.tree.PhyloTreeService;
import org.cipres.treebase.service.AbstractService;
import org.cipres.treebase.service.AbstractServiceImpl;

/**
 * @author mjd 20080717
 *
 */
public class SearchServiceImpl extends AbstractServiceImpl implements SearchService {
	StudyService studyService;
	MatrixService matrixService;
	PhyloTreeService phyloTreeService;
	TaxonLabelService taxonLabelService;

	@SuppressWarnings("serial")
	public class UnimplementedLayerException extends RuntimeException {
		public UnimplementedLayerException(String s) {
			super(s);
		}
		public UnimplementedLayerException() {
			super();
		}
	}

	protected DomainHome getDomainHome() {
		throw new UnimplementedLayerException("SearchHome layer unimplemented");
	}

	public AbstractService getAppropriateService(SearchResultsType srType) {
		switch(srType) {
		case STUDY: return getStudyService();
		case MATRIX: return getMatrixService();
		case TAXON: return getTaxonLabelService();
		case TREE: return getPhyloTreeService();
		default: throw new UnknownSearchResultsTypeError(srType);
		}
	}
	
	public AbstractService getAppropriateService(SearchResults<?> searchResults) {
		return getAppropriateService(searchResults.getResultType());
	}
	
	public StudyService getStudyService() {
		return studyService;
	}

	public void setStudyService(StudyService studyService) {
		this.studyService = studyService;
	}

	public MatrixService getMatrixService() {
		return matrixService;
	}

	public void setMatrixService(MatrixService matrixService) {
		this.matrixService = matrixService;
	}

	public PhyloTreeService getPhyloTreeService() {
		return phyloTreeService;
	}

	public void setPhyloTreeService(PhyloTreeService phyloTreeService) {
		this.phyloTreeService = phyloTreeService;
	}

	public TaxonLabelService getTaxonLabelService() {
		return taxonLabelService;
	}

	public void setTaxonLabelService(TaxonLabelService taxonLabelService) {
		this.taxonLabelService = taxonLabelService;
	}

	@Override
	public Class defaultResultClass() {
		return null;
	}


}

