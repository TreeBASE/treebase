/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ 
 * All Rights Reserved. 
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for educational, research and non-profit purposes, without
 * fee, and without a written agreement is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies. 
 *
 * Permission to incorporate this software into commercial products may be
 * obtained by contacting us:
 *              http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event
 * shall the CIPRES project be liable to any party for direct, indirect,
 * special, incidental, or consequential damages, including lost profits, 
 * arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage.  
 * The CIPRES project specifically disclaims any warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for
 * a particular purpose. The CIPRES project has no obligations to provide 
 * maintenance, support, updates, enhancements, or modifications. 
 */

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
			Pattern p = Pattern.compile("^\\s*DIMENSIONS\\s+(\\S+)[ \\t]*\\n", Pattern.MULTILINE);
			Matcher matcher = p.matcher(s);
			assertTrue(matcher.find());
			assertEquals("NCHAR=1585;", matcher.group(1));
		}
	}
	

}
