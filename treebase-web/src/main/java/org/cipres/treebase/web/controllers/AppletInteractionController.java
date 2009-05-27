/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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



package org.cipres.treebase.web.controllers;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import org.cipres.treebase.domain.tree.PhyloTreeService;

public class AppletInteractionController extends AbstractController {

	public static final String xmlHeader = "<?xml version=\"1.0\"?>\n";

	private PhyloTreeService mPhyloTreeService;

	@Override
	protected ModelAndView handleRequestInternal(
		HttpServletRequest pRequest,
		HttpServletResponse pResponse) throws Exception {

		System.out.println("I am in AppletInteraction Controller");
		InputStream in = pRequest.getInputStream();
		ObjectInputStream inputFromApplet = new ObjectInputStream(in);
		String echo = (String) inputFromApplet.readObject();
		System.out.println(echo);
		inputFromApplet.close();
		in.close();

		// echo it to the applet
		OutputStream outstr = pResponse.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(outstr);
		oos.writeObject("Sending.....OK");
		oos.flush();
		oos.close();
		outstr.flush();
		outstr.close();

		// set the namespace context for resolving prefixes of the Qnames
		// to NS URI, if the xpath expresion uses Qnames. XPath expression
		// would use Qnames if the XML document uses namespaces.
		// xpathprocessor.setNamespaceContext(NamespaceContext nsContext);
		// create XPath expressions

		InputSource inputSource = new InputSource(new StringReader(xmlHeader + echo));
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(inputSource);

		NodeList trees = doc.getElementsByTagName("treeid");

		Node treeId = trees.item(0);

		String treeid = getCharacterDataFromElement(treeId);

		NodeList newick = doc.getElementsByTagName("newickstring");

		Node newickString = (Node) newick.item(0);

		String newickstring = getCharacterDataFromElement(newickString);
		// String newickString = newick.getNodeValue();

		System.out.println("Tree Id-1: " + treeid);
		System.out.println("Newick-1: " + newickstring);

		getPhyloTreeService().updateByRearrangeNodes(new Long(treeid), newickstring);

		return null;
	}

	public static String getCharacterDataFromElement(Node e) {

		List<String> values = new ArrayList<String>();
		Node firstChild = e.getFirstChild();
		if (firstChild instanceof CharacterData) {
			CharacterData cd = (CharacterData) firstChild;
			return cd.getData();
		}
		return "?";
	}

	/**
	 * @return the phyloTreeService
	 */
	public PhyloTreeService getPhyloTreeService() {
		return mPhyloTreeService;
	}

	/**
	 * @param pPhyloTreeService the phyloTreeService to set
	 */
	public void setPhyloTreeService(PhyloTreeService pPhyloTreeService) {
		mPhyloTreeService = pPhyloTreeService;
	}

}
