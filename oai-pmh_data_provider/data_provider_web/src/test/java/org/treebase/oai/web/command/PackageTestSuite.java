package org.treebase.oai.web.command;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PackageTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.cipres.treebase.auxdata");
		
		suite.addTestSuite(IdentifyTest.class);
	
	
		
		return suite;
	}

}
