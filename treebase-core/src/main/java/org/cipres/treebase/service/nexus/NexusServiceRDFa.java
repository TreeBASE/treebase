package org.cipres.treebase.service.nexus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.cipres.treebase.Constants;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.nexus.nexml.NexmlDocumentConverter;
import org.cipres.treebase.domain.study.Study;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.SAXReader;
import org.jdom.Namespace;
import org.nexml.model.Document;
import org.nexml.model.DocumentFactory;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;


/**
 * @author rvosa
 *
 */
public class NexusServiceRDFa extends NexusServiceNexml {
	private static final Logger LOGGER = Logger.getLogger(NexusServiceRDFa.class);
	
	/**
	 * 
	 */
	public String serialize(NexusDataSet nexusDataSet,Properties properties) {
		NexmlDocumentConverter ndc = getNexmlDocumentConverter(null, properties);
		return transform(ndc.fromTreeBaseToXml(nexusDataSet).getXmlString());
	}
	
	/**
	 * 
	 */
	public String serialize(NexusDataSet nexusDataSet) {
		NexmlDocumentConverter ndc = getNexmlDocumentConverter(null, null);
		return transform(ndc.fromTreeBaseToXml(nexusDataSet).getXmlString());		
	}

	/**
	 * 
	 */
	public String serialize(Study study,Properties properties) {
		NexmlDocumentConverter ndc = getNexmlDocumentConverter(study, properties);
		return transform(ndc.fromTreeBaseToXml(study).getXmlString());
	}
	
	/**
	 * 
	 */
	public String serialize(Study study) {
		NexmlDocumentConverter ndc = getNexmlDocumentConverter(study, null);
		return transform(ndc.fromTreeBaseToXml(study).getXmlString());		
	}
	
	/**
	 * Changes local URIs into global ones, based on owl:sameAs annotations
	 * @param cdaoDoc
	 */
	private void normalizeCdaoDoc (org.dom4j.Document cdaoDoc) {
		Map<String,String> resourceForId = new HashMap<String,String>();
		QName rdfAboutQName = QName.get("about", "rdf", Constants.RDFURI.toString());
		QName rdfResourceQName = QName.get("resource", "rdf", Constants.RDFURI.toString());
		QName rdfIDQName = QName.get("ID", "rdf", Constants.RDFURI.toString());
		
		LOGGER.info("inside normalizeCdaoDoc");
		List<org.dom4j.Element> sameAsObjects = cdaoDoc.selectNodes("//owl:sameAs[@rdf:resource]");
		for ( org.dom4j.Element sameAsElt : sameAsObjects ) {
			org.dom4j.Element subjectElt = sameAsElt.getParent();
			String id = subjectElt.attributeValue(rdfAboutQName).substring(1); // remove #
			String resource = sameAsElt.attributeValue(rdfResourceQName);
			resourceForId.put(id, resource);			
			subjectElt.detach();
		}
		
		// replace ID attributes
		List<org.dom4j.Element> identifiableObjects = cdaoDoc.selectNodes("//rdf:Description[@rdf:ID]");
		for ( org.dom4j.Element identifiableObject : identifiableObjects ) {
			String rdfID = identifiableObject.attributeValue(rdfIDQName);
			if ( resourceForId.containsKey(rdfID) ) {
				org.dom4j.Attribute idAttr = identifiableObject.attribute(rdfIDQName);
				identifiableObject.remove(idAttr);
				identifiableObject.addAttribute(rdfAboutQName, resourceForId.get(rdfID));					
			}
		}
		
		// replace about and resource references
		for ( String rdfID : resourceForId.keySet() ) {
			List<org.dom4j.Element> referencingObjects = cdaoDoc.selectNodes("//rdf:Description[@rdf:about='#"+rdfID+"']");
			referencingObjects.addAll(cdaoDoc.selectNodes("//*[@rdf:resource='#"+rdfID+"']"));
			for ( org.dom4j.Element referencingObject : referencingObjects ) {
				org.dom4j.Attribute attr = referencingObject.attribute(rdfAboutQName);
				if ( null == attr ) {
					attr = referencingObject.attribute(rdfResourceQName);
				}
				if ( null != attr ) {
					attr.setValue(resourceForId.get(rdfID));
				}										
			}	
		}		

	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String transform(String input) {
		SAXReader reader = new SAXReader();
		ByteArrayInputStream bs = new ByteArrayInputStream(input.getBytes());
		org.dom4j.Document nexmlDocument = null;
		try {
			nexmlDocument = reader.read( bs );
		} catch (DocumentException e) {
			System.out.println(input);
			e.printStackTrace();
		}
				
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer rdfaTransformer = null;
		Transformer cdaoTransformer = null;
		try {
			rdfaTransformer = factory.newTransformer( new StreamSource( "http://www.nexml.org/nexml/xslt/RDFa2RDFXML.xsl" ) );
			cdaoTransformer = factory.newTransformer( new StreamSource( "http://www.nexml.org/nexml/xslt/nexml2cdao.xsl"  ) );
		} catch (TransformerConfigurationException e1) {
			e1.printStackTrace();
		}		
		DocumentSource nexmlSource = new DocumentSource( nexmlDocument );
		DocumentResult rdfaResult = new DocumentResult();
		DocumentResult cdaoResult = new DocumentResult();
		try {
			rdfaTransformer.transform( nexmlSource, rdfaResult );
			cdaoTransformer.transform( nexmlSource, cdaoResult );
		} catch (TransformerException e) {		
			e.printStackTrace();
		}
				
		org.dom4j.Document rdfaDoc = rdfaResult.getDocument();
		org.dom4j.Document cdaoDoc = cdaoResult.getDocument();
		QName qName = QName.get("base", "xml", "http://www.w3.org/XML/1998/namespace");
		String sourceBase = nexmlDocument.getRootElement().attributeValue(qName);
		cdaoDoc.getRootElement().setAttributeValue(qName, sourceBase);
		Iterator<org.dom4j.Element> elementIterator = rdfaDoc.getRootElement().elementIterator();
		while ( elementIterator.hasNext() ) {
			org.dom4j.Element elt = elementIterator.next();
			elt.detach();
			cdaoDoc.getRootElement().add(elt);
		}
		normalizeCdaoDoc(cdaoDoc);
		return cdaoDoc.asXML();		
	}
}
