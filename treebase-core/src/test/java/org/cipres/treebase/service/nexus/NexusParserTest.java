package org.cipres.treebase.service.nexus;

import java.io.File;

import org.cipres.CipresIDL.api1.DataMatrix;
import org.cipres.CipresIDL.api1.Tree;
import org.cipres.datatypes.PhyloDataset;

import org.cipres.treebase.service.AbstractServiceTest;

/**
 * @author Jin Ruan
 * 
 */
public class NexusParserTest extends AbstractServiceTest {

	private static final String TEST_NEX_FILE = "/TestNexusFile.nex";

	/**
	 * Test the phyloDataSet
	 * 
	 * Creation date: Apr 18, 2006 2:50:18 PM
	 */
	//cipres datatype will not be used anymore
	public void testLoadPhyloDataSet() throws Exception {
		String testName = "loadPhyloDataSet";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		File nexusFile = new File(getClass().getResource(TEST_NEX_FILE).toURI());
		assertTrue("Test File " + TEST_NEX_FILE + " cannot be found.", nexusFile.exists());

		/*PhyloDataset data = new PhyloDataset(nexusFile);

		DataMatrix matrix = data.getDataMatrix();
		assertTrue("Empty matrix.", matrix != null);

		Tree tree = data.getFirstTree();
		assertTrue("Empty tree.", tree != null);
        */
		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}
}
