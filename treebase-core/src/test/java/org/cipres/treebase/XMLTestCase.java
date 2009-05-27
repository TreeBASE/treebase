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

package org.cipres.treebase;

import org.apache.log4j.Logger;
import org.dom4j.io.SAXReader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;
import org.jdom.filter.Filter;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import java.io.File;
import java.util.Iterator;

import junit.framework.TestCase;

/**
 * XMLTestCase.java
 * 
 * Created on Mar 27, 2008
 * @author JRUAN
 *
 */
public class XMLTestCase extends TestCase {

	private static final Logger logger = Logger.getLogger(XMLTestCase.class);

	/**
	 * Constructor.
	 * @param name
	 */
	public XMLTestCase(String name) {
		super(name);
	}

	/**
	 * Test XML parsing
	 */
	public void testJDOMSerach() throws Exception {
		String testName = "JDOMSearch";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}
		
		// 1. create a new submission:
		String path = "/simple.xml";
		File nexusFile = new File(getClass().getResource(path).toURI());

		//SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
		SAXBuilder builder = new SAXBuilder();
		
		Document doc = builder.build(nexusFile);	
		assertTrue("Empty doc.", doc != null);
		Element root = doc.getRootElement();
		Namespace ns = Namespace.getNamespace("");
		XPath xPath = XPath.newInstance("entity");
		Filter eleFilter = new ElementFilter("entity", null);
		
		// 6. verify search:
		//List<Element> allEles = xPath.selectNodes(doc);
		Iterator<Element> iter = doc.getDescendants(eleFilter);
		Element ele = iter.next();
		
		//Element ele = root.getChild("entity", ns);
		//logger.debug(" element size: " + allEles.size());
		
		assertTrue("Failed to search.", ele != null);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}

	/**
	 * Test XML parsing
	 */
	public void testDOM4jSerach() throws Exception {
		String testName = "DOM4jSearch";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}
		
		// 1. create file, parse file, create DOM:
		String path = "/simple.xml";
		File nexusFile = new File(getClass().getResource(path).toURI());

		SAXReader reader = new SAXReader();
		org.dom4j.Document docj = reader.read(nexusFile);
		
		//select all entity elements in the document:
		Iterator<org.dom4j.Element> iter = docj.selectNodes("//entity").iterator();
		
		org.dom4j.Element ele = iter.next();
		
		logger.debug(" element : " + ele.elementText("nameString") + " : " + ele.elementText("namebankID"));
		assertTrue("Failed to search.", ele != null);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}


}
