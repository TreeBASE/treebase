package org.cipres.treebase.domain.nexus;

import junit.framework.Assert;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.nexus.nexml.NexmlMatrixConverter;
import org.cipres.treebase.domain.nexus.NexusDataSet;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.nexml.model.DocumentFactory;
import org.nexml.model.Document;

public class NexmlMatrixConverterTest extends AbstractDAOTest {
	private TaxonLabelHome mTaxonLabelHome;

	/**
	 * Test  for {@link org.cipres.treebase.domain.nexus.nexml.NexmlMatrixConverter#fromTreeBaseToXml(CharacterMatrix)}.
	 */
	public void testNexmlMatrixConverter() {
		String testName = "testNexmlMatrixConverter";
		//signal beginning of test
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}
		
		long studyId = 1787;
		Study study = (Study)loadObject(Study.class, studyId);
		NexusDataSet nexusDataSet = new NexusDataSet();
		
		Document doc = DocumentFactory.safeCreateDocument();
		nexusDataSet.setNexmlProject(doc);
		NexmlMatrixConverter nmc = new NexmlMatrixConverter(study,getTaxonLabelHome(),doc);
		for (org.cipres.treebase.domain.matrix.Matrix matrix : nexusDataSet.getMatrices() ) {
			if ( matrix instanceof CharacterMatrix ) {
				//fromTreeBaseToXml(CharacterMatrix) is main method being tested here--it uses populateXmlMatrix() function
				String xml = ((Document) nmc.fromTreeBaseToXml((CharacterMatrix)matrix)).getXmlString();
				System.out.println(xml);
				Assert.assertNotNull(xml);
			}
		}
		//signal that test is over
		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end ");
		}
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
