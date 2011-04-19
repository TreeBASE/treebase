package org.cipres.treebase.domain.nexus;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import junit.framework.Assert;

import org.cipres.treebase.Constants;
import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.nexus.nexml.NexmlDocumentConverter;
import org.nexml.model.DocumentFactory;
import org.nexml.model.Document;

public class NexmlSerializationTest extends AbstractDAOTest  {
	private TaxonLabelHome mTaxonLabelHome;

	public void testSerializeStudy() {
		long studyId = 1787;
		Study study = (Study)loadObject(Study.class, studyId);
		Document doc = DocumentFactory.safeCreateDocument();
		NexmlDocumentConverter conv = new NexmlDocumentConverter(study,getTaxonLabelHome(),doc);
		String xml = conv.fromTreeBaseToXml(study).getXmlString();
//		File file = new File("/Users/rvosa/Desktop/NexmlSerializationTest.xml");
//		try {
//			Writer output = new BufferedWriter(new FileWriter(file));
//			output.write(xml);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
		System.out.println(xml);
		Assert.assertNotNull(xml);
	}
	
	/**
	 * Return the TaxonLabelHome field.
	 * 
	 * @return TaxonLabelHome mTaxonLabelHome
	 */
	public TaxonLabelHome getTaxonLabelHome() {
		return mTaxonLabelHome;
	}

	/**
	 * Set the TaxonLabelHome field.
	 */
	public void setTaxonLabelHome(TaxonLabelHome pNewTaxonLabelHome) {
		mTaxonLabelHome = pNewTaxonLabelHome;
	}	
}
