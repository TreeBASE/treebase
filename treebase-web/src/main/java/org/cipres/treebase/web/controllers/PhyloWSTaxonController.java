package org.cipres.treebase.web.controllers;

import java.util.HashMap;
import java.util.Map;

import org.cipres.treebase.domain.taxon.TaxonLabel;

public class PhyloWSTaxonController extends PhyloWSController {

	@Override
	String getSearchPage() {
		return "taxonSearch.html";
	}

	@Override
	String getDownloadPage() {
		return "downloadATaxon.html";
	}

	@Override
	Map<String,String> getObjectQueryParameters(Long objectId) throws ObjectNotFoundException {
		// TODO Auto-generated method stub
		return new HashMap<String,String>();
	}

	@Override
	String getDisplayPage() {
		// TODO Auto-generated method stub
		return "taxon.html";
	}

	@Override
	boolean hasWebPage(Class<?> theClass) {
		return TaxonLabel.class.isAssignableFrom(theClass);
	}

}
