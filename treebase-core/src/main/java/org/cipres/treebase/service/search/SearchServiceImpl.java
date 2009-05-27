/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

