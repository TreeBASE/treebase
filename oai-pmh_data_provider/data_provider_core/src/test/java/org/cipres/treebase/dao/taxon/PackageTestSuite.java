package org.cipres.treebase.dao.taxon;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PackageTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.cipres.treebase.dao.taxon");
		//$JUnit-BEGIN$
		suite.addTestSuite(TaxonLabelDAOTest.class);
		//$JUnit-END$
		return suite;
	}

}
