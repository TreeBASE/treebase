package org.treebase.oai;

import junit.framework.Test;
import junit.framework.TestSuite;


public class PackageTestSuite {


	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	
	public static Test suite() {
		TestSuite suite = new TestSuite("Tests in package " + PackageTestSuite.class.getName());

		suite.addTest(org.treebase.oai.web.PackageTestSuite.suite());

		return suite;
	}
}

