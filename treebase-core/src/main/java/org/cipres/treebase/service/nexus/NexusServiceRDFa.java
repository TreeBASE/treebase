package org.cipres.treebase.service.nexus;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.nexus.nexml.NexmlDocumentConverter;
import org.cipres.treebase.domain.study.Study;
import org.dom4j.DocumentException;
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
	public String serialize(NexusDataSet nexusDataSet,Properties properties) {
		NexmlDocumentConverter ndc = getNexmlDocumentConverter(null, properties);
		return transform(ndc.fromTreeBaseToXml(nexusDataSet).getXmlString());
	}
	public String serialize(NexusDataSet nexusDataSet) {
		/*
		Document document = null;
		try {
			document = DocumentFactory.createDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		NexmlDocumentConverter ndc = new NexmlDocumentConverter(null,getTaxonLabelHome(),document);
		String NeXML = ndc.fromTreeBaseToXml(nexusDataSet).getXmlString();
		return transform(NeXML);
		*/
		NexmlDocumentConverter ndc = getNexmlDocumentConverter(null, null);
		return transform(ndc.fromTreeBaseToXml(nexusDataSet).getXmlString());		
	}

	public String serialize(Study study,Properties properties) {
		NexmlDocumentConverter ndc = getNexmlDocumentConverter(study, properties);
		return transform(ndc.fromTreeBaseToXml(study).getXmlString());
	}
	public String serialize(Study study) {
		/*
		Document document = null;
		try {
			document = DocumentFactory.createDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		NexmlDocumentConverter ndc = new NexmlDocumentConverter(study,getTaxonLabelHome(),document);
		String NeXML = ndc.fromTreeBaseToXml(study).getXmlString();
		return transform(NeXML);
		*/
		NexmlDocumentConverter ndc = getNexmlDocumentConverter(study, null);
		return transform(ndc.fromTreeBaseToXml(study).getXmlString());		
	}
	
	private String transform(String input) {
		SAXReader reader = new SAXReader();
		ByteArrayInputStream bs = new ByteArrayInputStream(input.getBytes());
		org.dom4j.Document jDomDocument = null;
		try {
			jDomDocument = reader.read( bs );
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = factory.newTransformer( new StreamSource( "http://www.nexml.org/nexml/xslt/RDFa2RDFXML.xsl" ) );
		} catch (TransformerConfigurationException e1) {
			e1.printStackTrace();
		}		
		DocumentSource source = new DocumentSource( jDomDocument );
		DocumentResult result = new DocumentResult();
		try {
			transformer.transform( source, result );
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		org.dom4j.Document transformedDoc = result.getDocument();
		QName qName = QName.get("base", "xml", "http://www.w3.org/XML/1998/namespace");
		String sourceBase = jDomDocument.getRootElement().attributeValue(qName);
		transformedDoc.getRootElement().setAttributeValue(qName, sourceBase);
		return transformedDoc.asXML();		
	}
}
