package org.cipres.treebase.web.util;

import java.io.File;
import java.util.List;

import org.cipres.treebase.domain.admin.Person;
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
	private ArticleCitation citation;
	private static final Namespace xs = new Namespace("xs","http://www.w3.org/2001/XMLSchema");
	private static final Namespace dwc= new Namespace("dwc", "http://rs.tdwg.org/dwc/terms/");
	private static final Namespace dcterms= new Namespace("dcterms", "http://purl.org/dc/terms/");
	
	//VG 2010-11-18 fixing SF:3089438 - extracting journal name from bagit metadata
	//-- private static final Namespace prism= new Namespace("prism", "http://prismstandard.org/namespaces/basic/2.0/");
	private static final Namespace bibo = new Namespace("bibo", "http://purl.org/ontology/bibo/"); 

	private Element pubRoot;
	private Element pkgRoot;


	public CitationParser(File path){
	
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
		
		citation = new ArticleCitation();
		loadData();
	}

		
	private void loadData(){
	
		Node description = getNode(pubRoot,"description",dcterms);		
		if(description!=null)citation.setAbstract(description.getText());
		
		Node identifier = getNode(pubRoot,"identifier",dcterms);		
		if(identifier!=null)citation.setDoi(identifier.getText().replaceAll("doi:", ""));
		
	    Node title = getNode(pubRoot,"title",dcterms);		
	    if(title!=null)citation.setTitle(title.getText());
		
//VG 2010-11-18 fixing SF:3089438 - extracting journal name from bagit metadata
/*--
	    Node issueIdentifier = getNode(pubRoot,"issueIdentifier",prism);		
		if(issueIdentifier!=null)citation.setIssue(issueIdentifier.getText());
		
		Node publicationName = getNode(pubRoot,"publicationName",prism);		
		if(publicationName!=null)citation.setJournal(publicationName.getText());
		
		Node volume = getNode(pubRoot,"volume",prism);		
		if(volume!=null)citation.setVolume(volume.getText());		
		
		Node pageRange = getNode(pubRoot,"pageRange",prism);		
		if(pageRange!=null)citation.setPages(pageRange.getText());						
*/		
		Node journal = getNode(pubRoot,"Journal",bibo);	
		if (journal!=null) citation.setJournal(journal.getText());
		
		Node volume = getNode(pubRoot,"volume",bibo);		
		if (volume!=null) citation.setVolume(volume.getText());		
		
		Node issue = getNode(pubRoot,"Issue",bibo);		
		if (issue!=null) citation.setIssue(issue.getText());
		
		Node pages = getNode(pubRoot,"pages",bibo);		
		Node pageStart = getNode(pubRoot,"pageStart",bibo);		
		Node pageEnd = getNode(pubRoot,"pageEnd",bibo);	
		if (pages != null) 
			citation.setPages(pages.getText()); 
		else if (pageStart != null && pageEnd != null) 
			citation.setPages(pageStart.getText() + "-" + pageEnd.getText()); 
//end VG 2010-11-18		
		
		//set the URL to use the DOI passed by Dryad
		Node doiUrl = getNode(pkgRoot,"identifier",dcterms);
		if (doiUrl!=null) {
			citation.setURL("http://dx.doi.org/" + doiUrl.getText().replaceAll("doi:", ""));
		}

		List<Element> kl = getElements(pkgRoot,"subject",dcterms);
		String keywords="";
		for(int i = 0; i<kl.size(); i++) {
		     keywords+= kl.get(i).getText()+",";			
		}		
		citation.setKeywords(keywords.substring(0, keywords.length()-1));
				
		List<Element> al = getElements(pubRoot,"creator",dcterms);
		for(int i = 0; i<al.size(); i++) {
		    String []names = al.get(i).getText().split(",");
			if(names.length >1 ){
				Person p = new Person ();
				names[1]=names[1].trim();
				if(!(names[1].contains(" ")))p.setFirstName(names[1]);
				else {					
					
					p.setFirstName(names[1].trim().substring(0,names[1].indexOf(" ")));
					p.setMiddleName(names[1].trim().substring(names[1].indexOf(" ")));
				}
				p.setLastName(names[0]);
				citation.addAuthor(p);
			}
		}		
		
		try{
		    int issued = Integer.parseInt(getNode(pubRoot,"issued",dcterms).getText());
		    citation.setPublishYear(issued);			
		}catch(Exception e){
			
		}
		
		
		if(getNode(pubRoot,"pubStatus",null)!=null){
			if("published".compareToIgnoreCase(getNode(pubRoot,"pubStatus",null).getText())==0)
		     	citation.setPublished(true);
		    else citation.setPublished(false);
		}		  
		
	}

	private Node getNode(Element root, String localName, Namespace namespace){
        if(namespace==null)return root.element(localName);
		return root.element(new QName(localName, namespace));
	}

	private List<Element> getElements(Element root, String localName, Namespace namespace){
		return root.elements(new QName(localName, namespace));
	}

	public ArticleCitation getCitation() {
		return citation;
	}

}
