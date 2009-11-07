package org.treebase.oai.web.controller;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PackageTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.cipres.treebase.auxdata");
		//$JUnit-BEGIN$
		suite.addTestSuite(OAIPMHControllerTest.class);
		
		//$JUnit-END$
		return suite;
	}

}
