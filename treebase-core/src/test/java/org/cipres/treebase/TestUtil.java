
package org.cipres.treebase;

import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import org.cipres.treebase.core.CoreServiceLauncher;

/**
 * TestUtil.java
 * 
 * Created on Oct 6, 2005
 * 
 * @author Jin Ruan
 * 
 */
public class TestUtil {
	private static final Logger LOGGER = Logger.getLogger(TestUtil.class);

	/**
	 * Constructor.
	 */
	public TestUtil() {
		super();
	}

	/**
	 * Initialize the application context.
	 * 
	 * @param pSuite
	 */
	public static void setUp(TestSuite pSuite) {
		if (pSuite != null) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("\n>>>>> >>>>> >>>>> >>>>> >>>>> >>>>> >>>>>>");
				LOGGER
					.info("Running " + pSuite.countTestCases() + " tests in: " + pSuite.getName());
			}

			// Create and initialize the context.
			// try {
			ContextManager.createSpringContext(CoreServiceLauncher.getSpringConfigurations());
			// } catch (Throwable ex) {
			// LOGGER.error("TestUtil() static initializer", ex);
			// }
		}
	}

	/**
	 * Close the application context.
	 */
	public static void tearDown() {
	// TODO: tearDown

	}

}
