
package org.cipres.treebase.framework;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.cipres.treebase.TreebaseUtil;

import junit.framework.TestCase;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * TSVFileParserTest.java
 * 
 * Created on May 5, 2008
 * @author Jin Ruan
 *
 */
public class TSVFileParserTest extends TestCase {
	private static final Logger logger = LogManager.getLogger(TSVFileParserTest.class);

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
	@Test

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
