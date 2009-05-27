package org.cipres.treebase.web.webservices;

import javax.jws.WebService;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.study.StudyService;

/**
 * 
 * @author madhu
 * 
 * Created on January 24th, 2008
 */

@WebService(serviceName = "StudiesContainingKeyword", endpointInterface = "org.cipres.treebase.web.webservices.StudyServiceByKeyword")
public class StudyServiceByKeywordImp implements StudyServiceByKeyword {

	private StudyService mStudyService;

	public String fetchStudiesContainingKeyword(String pKeyword) {

		if (TreebaseUtil.isEmpty(pKeyword)) {
			return "Keyword cannot be empty.";
		}

		StudyService service = getStudyService();
		String trimmedKeywords = pKeyword.trim();
		String xmlStringByKeyword = service.generateXMLStringByKeyword(trimmedKeywords);
		return xmlStringByKeyword;
		//return getStudyService().generateXMLStringByKeyword(pKeyword.trim());
	}

	public StudyService getStudyService() {
		return mStudyService;
	}

	public void setStudyService(StudyService pStudyService) {
		mStudyService = pStudyService;
	}
}
