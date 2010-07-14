package org.cipres.treebase.web.util;

import java.io.File;
import java.util.List;

import org.cipres.treebase.domain.study.ArticleCitation;
import org.cipres.treebase.domain.study.Citation;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;

public class CitationParser {

	private static final Namespace xs = new Namespace("xs","http://www.w3.org/2001/XMLSchema");
	private static final Namespace dwc= new Namespace("dwc", "http://rs.tdwg.org/dwc/terms/");
	private static final Namespace dcterms= new Namespace("dcterms", "http://purl.org/dc/terms/");
	private static final Namespace prism= new Namespace("prism", "http://prismstandard.org/namespaces/basic/2.0/");

	private ArticleCitation citation; 
	private Element pubRoot;
	private Element pkgRoot;


	public CitationParser(File path){

		citation = new ArticleCitation();

		File pubFile = new File(path, "dryadpub.xml");
		SAXReader pubReader = new SAXReader();
		Document pubDoc= null ;
		try {
			pubDoc = pubReader.read(pubFile);
			pubRoot = pubDoc.getRootElement();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		File pkgFile = new File(path, "dryadpkg.xml");
		SAXReader pkgReader = new SAXReader();
		Document pkgDoc= null ;
		try {
			pkgDoc = pkgReader.read(pkgFile);
			pkgRoot = pkgDoc.getRootElement();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		loadData();
	}

		
	private void loadData(){
	    		
		citation.setAbstract(getNode(pubRoot,"description",dcterms).getText());
		citation.setDoi(getNode(pubRoot,"identifier",dcterms).getText());;
		
		citation.setTitle(getNode(pubRoot,"title",dcterms).getText());
		citation.setIssue(getNode(pubRoot,"issueIdentifier",prism).getText());
		citation.setJournal(getNode(pubRoot,"publicationName",prism).getText());
		citation.setVolume(getNode(pubRoot,"volume",prism).getText());		
		citation.setPages(getNode(pubRoot,":pageRange",prism).getText());
		
		//citation.setKeywords(getNode(pubRoot,"description",dcterms).getText());
		//citation.setAuthors(pAuthors);
		
		//citation.setPublishYear(getNode(pubRoot,"issued",dcterms).getText());			
		//citation.setPublished(getNode(pubRoot,"description",dcterms).getText());
		
	}

	private Node getNode(Element root, String localName, Namespace namespace){

		return root.element(new QName(localName, namespace));
	}

	private List<Node> getNodes(Element root, String localName, Namespace namespace){

		return root.elements(new QName(localName, namespace));	
	}


	public Citation getCitation() {
		return citation;
	}

}
