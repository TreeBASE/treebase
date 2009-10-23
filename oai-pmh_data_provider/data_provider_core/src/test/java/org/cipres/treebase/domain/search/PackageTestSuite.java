package org.cipres.treebase.domain.search;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PackageTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.cipres.treebase.domain.search");
		//$JUnit-BEGIN$
		suite.addTestSuite(SearchResultsTest.class);
		//$JUnit-END$
		return suite;
	}

}
