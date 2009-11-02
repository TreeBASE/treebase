/*
 * Copyright 2005 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

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