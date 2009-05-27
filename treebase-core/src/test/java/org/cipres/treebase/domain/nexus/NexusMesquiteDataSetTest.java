/*
 * Copyright 2007 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.domain.nexus;

import org.cipres.treebase.dao.AbstractDAOTest;

/**
 * NexusMesquiteDataSetTest.java
 * 
 * Created on Jun 12, 2007
 * 
 * @author Jin Ruan
 * 
 */
public class NexusMesquiteDataSetTest extends AbstractDAOTest {

	private NexusService mFixture;

	/**
	 * Constructor.
	 */
	public NexusMesquiteDataSetTest() {
		super();
	}

	/**
	 * Return the Fixture field.
	 * 
	 * @return NexusService mFixture
	 */
	public NexusService getFixture() {
		return mFixture;
	}

	/**
	 * Set the Fixture field.
	 */
	public void setFixture(NexusService pNewFixture) {
		mFixture = pNewFixture;
	}

	/**
	 * Run the String[] getSpringConfigurations() method test.
	 * 
	 * @generatedBy CodePro at 9/27/05 10:24 AM
	 */
	public void testMesqutieFolderDir() throws Exception {
		String testName = "Test MesquiteFolder Dir";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		// assertTrue(false);
		String prop = System.getProperty("mesquite.folder_dir");

		System.out.println("    mesquite.folder_dir= " + prop);

		assertTrue(prop != null);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}
}
