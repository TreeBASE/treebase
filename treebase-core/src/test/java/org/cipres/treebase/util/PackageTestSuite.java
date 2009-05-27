package org.cipres.treebase.util;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The class <code>PackageTestSuite</code> builds a suite that can be used to run all of the tests
 * within its package as well as within any subpackages of its package.
 * 
 * @author Mark Dominus 20090520
 * @version $Revision: 1.0 $
 */
public class PackageTestSuite {

	/**
	 * Launch the test.
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	/**
	 * Create a test suite that can run all of the test cases in this package and all subpackages.
	 * 
	 * @return the test suite that was created
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("Tests in package " + PackageTestSuite.class.getName());

		suite.addTestSuite(HashFieldReaderTest.class);
		suite.addTestSuite(ObjectGroupMergerTest.class);
		suite.addTestSuite(UnixOptionsTest.class);

//		suite.addTest(org.cipres.treebase.core.PackageTestSuite.suite());
		
		return suite;
	}
}
