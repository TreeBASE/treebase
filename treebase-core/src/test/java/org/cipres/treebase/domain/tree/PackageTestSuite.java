package org.cipres.treebase.domain.tree;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The class <code>PackageTestSuite</code> builds a suite that can be used to run all of the tests
 * within its package as well as within any subpackages of its package.
 * 
 * @generatedBy CodePro at 9/27/05 10:24 AM
 * @author Jin Ruan
 * @version $Revision: 1.0 $
 */
public class PackageTestSuite {

	/**
	 * Launch the test.
	 * 
	 * @param args the command line arguments
	 * 
	 * @generatedBy CodePro at 9/27/05 10:24 AM
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	/**
	 * Create a test suite that can run all of the test cases in this package and all subpackages.
	 * 
	 * @return the test suite that was created
	 * 
	 * @generatedBy CodePro at 9/27/05 10:24 AM
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("Tests in package " + PackageTestSuite.class.getName());

		suite.addTestSuite(PhyloTreeTest.class);
		//suite.addTestSuite(StudyTest.class);
		return suite;
	}
}
