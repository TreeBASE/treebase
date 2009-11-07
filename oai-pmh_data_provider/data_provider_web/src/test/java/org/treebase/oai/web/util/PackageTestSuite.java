package org.treebase.oai.web.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PackageTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.cipres.treebase.auxdata");
		//$JUnit-BEGIN$
		suite.addTestSuite(IdentifyUtilTest.class);		
		
		//$JUnit-END$
		return suite;
	}

}
