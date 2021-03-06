package org.cipres.treebase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The class <code>PackageTestSuite</code> builds a suite that can be used to run all of the tests
 * within its package as well as within any subpackages of its package.
 * 
 * @generatedBy CodePro at 10/13/05 4:18 PM
 * @author Jin Ruan
 * @version $Revision: 1.0 $
 */
public class PackageTestSuite {

	/**
	 * Launch the test.
	 * 
	 * @param args the command line arguments
	 * 
	 * @generatedBy CodePro at 10/13/05 4:18 PM
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	/**
	 * Create a test suite that can run all of the test cases in this package and all subpackages.
	 * 
	 * @return the test suite that was created
	 * 
	 * @generatedBy CodePro at 10/13/05 4:18 PM
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("Tests in package " + PackageTestSuite.class.getName());

		suite.addTestSuite(ContextManagerTest.class);
		suite.addTestSuite(RangeExpressionTest.class);
		suite.addTestSuite(TreebaseIDStringTest.class);
		suite.addTestSuite(TreebaseUtilTest.class);

		suite.addTest(org.cipres.treebase.auxdata.PackageTestSuite.suite());
		suite.addTest(org.cipres.treebase.core.PackageTestSuite.suite());
		suite.addTest(org.cipres.treebase.framework.PackageTestSuite.suite());
		suite.addTest(org.cipres.treebase.dao.PackageTestSuite.suite());
		suite.addTest(org.cipres.treebase.domain.PackageTestSuite.suite());
		suite.addTest(org.cipres.treebase.service.PackageTestSuite.suite());
		suite.addTest(org.cipres.treebase.util.PackageTestSuite.suite());

		return suite;
	}
}
