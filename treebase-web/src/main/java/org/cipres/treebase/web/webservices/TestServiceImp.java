package org.cipres.treebase.web.webservices;

import javax.jws.WebService;

@WebService(serviceName = "MyTestService", endpointInterface = "org.cipres.treebase.web.webservices.TestService")
public class TestServiceImp implements TestService {
	public String sayHello() {
		return "Hello World Service";
	}
}
