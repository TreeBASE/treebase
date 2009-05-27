package org.cipres.treebase.web.webservices;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(targetNamespace = "http://org/treebase/services/testservice")
public interface TestService {
	@WebMethod
	public String sayHello();
}
