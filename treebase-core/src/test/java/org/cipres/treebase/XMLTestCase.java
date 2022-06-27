
package org.cipres.treebase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

	private static final Logger logger = LogManager.getLogger(XMLTestCase.class);

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
		builder.setExpandEntities(false); // defensive against CVE-2021-33813
		
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
		Iterator<org.dom4j.Node> iter = docj.selectNodes("//entity").iterator();
		
		org.dom4j.Node ele = iter.next();

		logger.debug(" element : " + ele.selectSingleNode("nameString").getText() + " : " + ele.selectSingleNode("namebankID").getText());
		assertTrue("Failed to search.", ele != null);

		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}


}
