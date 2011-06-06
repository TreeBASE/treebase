package org.cipres.treebase.domain.nexus;

import java.util.List;

import junit.framework.Assert;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.matrix.CharacterMatrix;
import org.cipres.treebase.domain.nexus.nexml.NexmlDocumentConverter;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.nexml.model.CategoricalMatrix;
import org.nexml.model.ContinuousMatrix;
import org.nexml.model.DocumentFactory;
import org.nexml.model.Document;
import org.nexml.model.Matrix;
import org.nexml.model.MolecularMatrix;

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
		
		Document doc = DocumentFactory.safeCreateDocument();
		NexmlDocumentConverter ndc = new NexmlDocumentConverter(study,getTaxonLabelHome(),doc);
		Document document = ndc.fromTreeBaseToXml(study);
		List<Matrix<?>> matrices = document.getMatrices();
		Assert.assertTrue(matrices.size() != 0 );
		for ( Matrix<?> matrix : matrices ) {
			if ( matrix instanceof MolecularMatrix ) {
				System.out.println(matrix.getId() + " is a molecular character state matrix");
			}
			else if ( matrix instanceof ContinuousMatrix ) {
				System.out.println(matrix.getId() + " is a continuous-valued character matrix");
			}
			else if ( matrix instanceof CategoricalMatrix ) {
				System.out.println(matrix.getId() + " is a categorical character state matrix");
			}
			else {
				System.out.println("This should not ever happen.");
			}
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
