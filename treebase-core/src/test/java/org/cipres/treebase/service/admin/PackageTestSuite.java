package org.cipres.treebase.service.admin;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The class <code>PackageTestSuite</code> builds a suite that can be used to run all
 * of the tests within its package as well as within any subpackages of its
 * package.
 *
 * @generatedBy CodePro at 10/13/05 4:18 PM
 * @author Jin Ruan
 * @version $Revision: 1.0 $
 */
public class PackageTestSuite
{

	/**
	 * Launch the test.
	 *
	 * @param args the command line arguments
	 *
	 * @generatedBy CodePro at 10/13/05 4:18 PM
	 */
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(suite());
	}

	/**
	 * Create a test suite that can run all of the test cases in this package
	 * and all subpackages.
	 *
	 * @return the test suite that was created
	 *
	 * @generatedBy CodePro at 10/13/05 4:18 PM
	 */
	public static Test suite()
	{
		TestSuite suite;
	
		suite = new TestSuite("Tests in package org.cipres.treebase.service.admin");
		suite.addTestSuite(UserServiceImplTest.class);
		suite.addTestSuite(HelpServiceImplTest.class);
		return suite;
	}
}
