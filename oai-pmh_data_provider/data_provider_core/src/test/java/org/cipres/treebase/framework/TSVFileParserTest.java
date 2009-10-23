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

package org.cipres.treebase.framework;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import org.cipres.treebase.TreebaseUtil;

import junit.framework.TestCase;

/**
 * TSVFileParserTest.java
 * 
 * Created on May 5, 2008
 * @author Jin Ruan
 *
 */
public class TSVFileParserTest extends TestCase {
	private static final Logger logger = Logger.getLogger(TSVFileParserTest.class);

	/**
	 * Constructor.
	 * @param name
	 */
	public TSVFileParserTest(String name) {
		super(name);
	}

	/**
	 * Test method for {@link org.cipres.treebase.framework.TSVFileParser#parseFile(java.io.File, boolean, org.cipres.treebase.framework.ExecutionResult)}.
	 * @throws Exception 
	 */
	public void testParseFile() throws Exception {
		String testName = "testParseFile";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}
		
		String path = "/segment-testdata.txt"; //TSV sample file
		File tsvFile = new File(getClass().getResource(path).toURI());
		ExecutionResult returnVal = new ExecutionResult();
		
		TSVFileParser parser = new TSVFileParser();
		List<List<String>> values = parser.parseFile(tsvFile, false, returnVal);
		
		logger.info("row count=" + values.size());
		assertTrue(values.size() > 0);
		
		List<String> firstRow = values.get(0);
		logger.info("col count=" + firstRow.size());
		assertTrue(firstRow.size() > 0);
		
		String firstRowStr = TreebaseUtil.printElement(firstRow, true);
		logger.info("firstRow=" + firstRowStr);
		
		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

}
