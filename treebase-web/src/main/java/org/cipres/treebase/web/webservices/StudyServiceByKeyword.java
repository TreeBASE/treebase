package org.cipres.treebase.web.webservices;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(targetNamespace = "http://org/treebase/services/studyservicebykeyword")
public interface StudyServiceByKeyword {
	@WebMethod
	public String fetchStudiesContainingKeyword(String keyword);
}
