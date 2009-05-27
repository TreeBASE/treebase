package org.cipres.treebase.domain.taxon;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PackageTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.cipres.treebase.domain.taxon");
		//$JUnit-BEGIN$
		suite.addTestSuite(TaxonLabelTest.class);
		//$JUnit-END$
		return suite;
	}

}
