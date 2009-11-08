package org.treebase.oai.web;

import junit.framework.Test;
import junit.framework.TestSuite;


public class PackageTestSuite {


	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	
	public static Test suite() {
		TestSuite suite = new TestSuite("Tests in package " + PackageTestSuite.class.getName());

		//suite.addTestSuite(ContextManagerTest.class);
		//suite.addTestSuite(RangeExpressionTest.class);
		//suite.addTestSuite(TreebaseIDStringTest.class);
		//suite.addTestSuite(TreebaseUtilTest.class);

		suite.addTest(org.treebase.oai.web.command.PackageTestSuite.suite());
		suite.addTest(org.treebase.oai.web.controller.PackageTestSuite.suite());
		suite.addTest(org.treebase.oai.web.util.PackageTestSuite.suite());
		
		return suite;
	}
}
