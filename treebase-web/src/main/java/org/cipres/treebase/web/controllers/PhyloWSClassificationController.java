package org.cipres.treebase.web.controllers;

import java.util.HashMap;
import java.util.Map;

public class PhyloWSClassificationController extends PhyloWSController {

	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.web.controllers.PhyloWSController#getSearchPage()
	 */
	@Override
	String getSearchPage() {
		return "classificationSearch.html";
	}

	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.web.controllers.PhyloWSController#getDownloadPage()
	 */
	@Override
	String getDownloadPage() {
		return "downloadAClassification.html";
	}

	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.web.controllers.PhyloWSController#getObjectQueryParameters(java.lang.Long)
	 */
	@Override
	Map<String,String> getObjectQueryParameters(Long objectId) throws ObjectNotFoundException {
		// TODO Auto-generated method stub
		return new HashMap<String,String>();
	}

	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.web.controllers.PhyloWSController#getDisplayPage()
	 */
	@Override
	String getDisplayPage() {
		// TODO Auto-generated method stub
		return "classification.html";
	}

	/*
	 * (non-Javadoc)
	 * @see org.cipres.treebase.web.controllers.PhyloWSController#hasWebPage(java.lang.String)
	 */
	@Override
	boolean hasWebPage(String prefix) {
		return false;
	}

}
