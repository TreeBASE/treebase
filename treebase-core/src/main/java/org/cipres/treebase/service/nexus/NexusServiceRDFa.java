package org.cipres.treebase.service.nexus;

import java.io.ByteArrayInputStream;
import java.util.Properties;

import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.nexus.nexml.NexmlDocumentWriter;
import org.cipres.treebase.domain.study.Study;
import org.dom4j.DocumentException;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.SAXReader;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;


/**
 * @author rvosa
 *
 */
public class NexusServiceRDFa extends NexusServiceNexml {
	
	/**
	 * 
	 */
	public String serialize(NexusDataSet nexusDataSet,Properties properties) {
		NexmlDocumentWriter ndc = getNexmlDocumentConverter(null, properties);
		String result = null;
		try {
			result = transform(ndc.fromTreeBaseToXml(nexusDataSet).getXmlString());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 */
	public String serialize(NexusDataSet nexusDataSet,Properties properties,Study study) {
		NexmlDocumentWriter ndc = getNexmlDocumentConverter(study, properties);
		String result = null;
		try {
			result = transform(ndc.fromTreeBaseToXml(nexusDataSet).getXmlString());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}	
	
	/**
	 * 
	 */
	public String serialize(NexusDataSet nexusDataSet) {
		NexmlDocumentWriter ndc = getNexmlDocumentConverter(null, null);
		String result = null;
		try {
			result = transform(ndc.fromTreeBaseToXml(nexusDataSet).getXmlString());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return result;
	}

	/**
	 * 
	 */
	public String serialize(Study study,Properties properties) {
		NexmlDocumentWriter ndc = getNexmlDocumentConverter(study, properties);
		String result = null;
		try {
			result = transform(ndc.fromTreeBaseToXml(study).getXmlString());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 
	 */
	public String serialize(Study study) {
		NexmlDocumentWriter ndc = getNexmlDocumentConverter(study, null);
		String result = null;
		try {
			result = transform(ndc.fromTreeBaseToXml(study).getXmlString());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return result;
	}

	
	/**
	 * 
	 * @param input
	 * @return
	 * @throws DocumentException 
	 * @throws TransformerException 
	 */
	private String transform(String input) throws DocumentException, TransformerException {
		SAXReader reader = new SAXReader();
		org.dom4j.Document nexmlDocument = reader.read( new ByteArrayInputStream(input.getBytes()) );
						
		DocumentResult cdaoResult = new DocumentResult();
		
		Transformer cdaoTransformer = TransformerFactory.newInstance().newTransformer( new StreamSource( "http://www.nexml.org/nexml/xslt/nexml2cdao.xsl"  ) );		
		cdaoTransformer.transform( new DocumentSource( nexmlDocument ), cdaoResult );
				
		org.dom4j.Document cdaoDoc = cdaoResult.getDocument();
		return cdaoDoc.asXML();		
	}
}
