package org.cipres.treebase.web.controllers;

import java.util.HashMap;
import java.util.Map;

import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.Study;

public class PhyloWSMatrixController extends PhyloWSController {

	private MatrixService mMatrixService;

	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.web.controllers.PhyloWSController#getSearchPage()
	 */
	@Override
	String getSearchPage() {
		return "matrixSearch.html";
	}

	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.web.controllers.PhyloWSController#getDownloadPage()
	 */
	@Override
	String getDownloadPage() {
		return "downloadAMatrix.html";
	}

	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.web.controllers.PhyloWSController#getObjectQueryParameters(java.lang.Long)
	 */
	@Override
	Map<String,String> getObjectQueryParameters(Long objectId) throws ObjectNotFoundException {
		Matrix matrix = getMatrixService().findByID(objectId);
		if ( matrix == null ) {
			throw new ObjectNotFoundException("Can't find matrix " + objectId);
		}
		Study study = matrix.getStudy();
		
		if ( study == null ) {
			throw new ObjectNotFoundException("Can't find study for matrix "+objectId);
		}
		
		checkAccess(matrix.getStudy().isPublished());
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("id", ""+study.getId());
		params.put("matrixid", ""+objectId);
		return params;
	}

	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.web.controllers.PhyloWSController#getDisplayPage()
	 */
	@Override
	String getDisplayPage() {
		return "matrix.html";
	}

	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.web.controllers.PhyloWSController#hasWebPage(java.lang.String)
	 */
	@Override
	boolean hasWebPage(String prefix) {
		return "M".equals(prefix);
	}
	
	public MatrixService getMatrixService() {
		return mMatrixService;
	}
	
	public void setMatrixService(MatrixService matrixService) {
		mMatrixService = matrixService;
	}	

}
