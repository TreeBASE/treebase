package org.cipres.treebase.web.controllers;

import java.util.HashMap;
import java.util.Map;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.cipres.treebase.domain.tree.PhyloTreeService;

public class PhyloWSTreeController extends PhyloWSController {

	private PhyloTreeService mPhyloTreeService;

	@Override
	String getSearchPage() {
		return "treeSearch.html";
	}

	@Override
	String getDownloadPage() {
		return "downloadATree.html";
	}

	@Override
	Map<String,String> getObjectQueryParameters(Long objectId) throws ObjectNotFoundException {
		PhyloTree phyloTree = getPhyloTreeService().findByID(objectId);
		if ( phyloTree == null ) {
			throw new ObjectNotFoundException("Can't find tree " + objectId);
		}
		Study study = phyloTree.getStudy();
		if ( study == null ) {
			throw new ObjectNotFoundException("Can't find study for tree "+objectId);
		}
		Map<String,String> params = new HashMap<String,String>();
		params.put("id",""+study.getId());
		params.put("treeid", ""+objectId);
		return params;		
	}

	@Override
	String getDisplayPage() {
		return "tree.html";
	}

	@Override
	boolean hasWebPage(String prefix) {
		return "Tr".equals(prefix);
	}
	
	public PhyloTreeService getPhyloTreeService() {
		return mPhyloTreeService;
	}
	
	public void setPhyloTreeService(PhyloTreeService phyloTreeService) {
		mPhyloTreeService = phyloTreeService;
	}	

}
