
package org.cipres.treebase.domain.nexus;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.service.nexus.NexusServiceMesquite;

/**
 * NexusMesquiteDataSetTest.java
 * 
 * Created on Jun 12, 2007
 * 
 * @author Jin Ruan
 * 
 */
public class NexusMesquiteDataSetTest extends AbstractDAOTest {

	private NexusServiceMesquite mFixture;

	/**
	 * Constructor.
	 */
	public NexusMesquiteDataSetTest() {
		super();
	}

	/**
	 * Return the Fixture field.
	 * 
	 * @return NexusServiceMesquite mFixture
	 */
	public NexusServiceMesquite getFixture() {
		return mFixture;
	}

	/**
	 * Set the Fixture field.
	 */
	public void setFixture(NexusServiceMesquite pNewFixture) {
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
