package org.cipres.treebase.web.controllers;

import java.util.HashMap;
import java.util.Map;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;

public class PhyloWSStudyController extends PhyloWSController {

	private StudyService mStudyService;

	@Override
	String getSearchPage() {
		return "studySearch.html";
	}

	@Override
	String getDownloadPage() {
		return "downloadAStudy.html";
	}

	@Override
	Map<String,String> getObjectQueryParameters(Long objectId) throws ObjectNotFoundException {
		Study study = getStudyService().findByID(objectId);
		if ( study == null ) {
			throw new ObjectNotFoundException("Can't find study " + objectId);
		}		
		Map<String,String> params = new HashMap<String,String>();
		params.put("id", ""+objectId);
		return params;
	}

	@Override
	String getDisplayPage() {
		return "summary.html";
	}

	@Override
	boolean hasWebPage(String prefix) {
		return "S".equals(prefix);
	}

	public StudyService getStudyService() {
		return mStudyService;
	}
	
	public void setStudyService(StudyService studyService) {
		mStudyService = studyService;
	}	
	
}
