
package org.cipres.treebase.domain.matrix;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cipres.treebase.dao.AbstractDAOTest;

/**
 * MatrixTest.java
 * 
 * Created on Jun 25, 2008
 * @author Jin Ruan
 *
 */
public class MatrixTest extends AbstractDAOTest {

	/**
	 * Constructor.
	 */
	public MatrixTest() {
		super();
	}

	/**
	 * Test method for {@link org.cipres.treebase.domain.matrix.Matrix#getFormatInfo()}.
	 */
	public void testGetFormatInfo() throws Exception {
		String testName = "getFormatInfo";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		Long id = 265L; //262L;
		Matrix m = (Matrix) loadObject(Matrix.class, id);
		
		assertTrue("Empty matrix table.", m != null);
		logger.info("matrix id: " + m.getId());
		logger.info("format=" + m.getFormatInfo());

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	
	/**
	 * Test method for {@link org.cipres.treebase.domain.matrix.Matrix#generateNexusBlock()}.
	 */
	public void testGenerateNexusBlock() throws Exception {
		String testName = "generateNexusBlock";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}
		
		// 1. get matrix:
		Long id = 683L; //CodonPositionSet, 682L, 683L
		Matrix m = (Matrix) loadObject(Matrix.class, id);
		
		assertTrue("Empty matrix table.", m != null);
		logger.info("matrix id: " + m.getId());

		// 2. generate its nexus file:
		StringBuilder builder = new StringBuilder();
		m.generateNexusBlock(builder);
		logger.info("nexus=\n" + builder.toString());

		// 3. verify
		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
		
		String s = builder.toString();
		{ 
			Pattern p = Pattern.compile("^\\s*DIMENSIONS\\s+NCHAR=1585;", Pattern.MULTILINE);
			Matcher matcher = p.matcher(s);
			assertTrue(matcher.find());
			//assertEquals("NCHAR=1585;", matcher.group(1));
		}
	}
	

}
