package org.cipres.treebase.web.controllers;

import java.util.HashMap;
import java.util.Map;

import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.matrix.MatrixService;
import org.cipres.treebase.domain.study.Study;

public class PhyloWSMatrixController extends PhyloWSController {

	private MatrixService mMatrixService;

	@Override
	String getSearchPage() {
		return "matrixSearch.html";
	}

	@Override
	String getDownloadPage() {
		return "downloadAMatrix.html";
	}

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
		Map<String,String> params = new HashMap<String,String>();
		params.put("id", ""+study.getId());
		params.put("matrixid", ""+objectId);
		return params;
	}

	@Override
	String getDisplayPage() {
		return "matrix.html";
	}

	@Override
	boolean hasWebPage(Class<?> theClass) {
		return Matrix.class.isAssignableFrom(theClass);
	}
	
	public MatrixService getMatrixService() {
		return mMatrixService;
	}
	
	public void setMatrixService(MatrixService matrixService) {
		mMatrixService = matrixService;
	}	

}
