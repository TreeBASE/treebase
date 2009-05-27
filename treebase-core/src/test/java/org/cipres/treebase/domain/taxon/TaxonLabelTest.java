package org.cipres.treebase.domain.taxon;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;

/**
 * The class <code>StudyTest</code> contains tests for the class {@link <code>Study</code>}
 * 
 * @pattern JUnit Test Case
 * 
 * @generatedBy CodePro at 6/7/06 12:05 PM
 * 
 * @author mjd
 *  
 * @version $Revision$
 */
public class TaxonLabelTest extends AbstractDAOTest {
	private static final Logger LOGGER = Logger.getLogger(TaxonLabelTest.class);

	private TaxonLabelHome mTaxonLabelHome;

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
	
	public void testFindBySubstring() {
		Collection<TaxonLabel> res = findHomoSapiensTL();
		assertNotNull(res);
		assertTrue(res.size() != 0);
		for (TaxonLabel tl : res) {
			assertTrue(tl.getTaxonLabel().toLowerCase().contains("homo"));
		}
	}
	
	public void testFindStudies() {
		{
			Collection<Study> res = getTaxonLabelHome().findStudiesWithTaxonLabels(findHomoSapiensTL());
			assertNotNull(res);
			LOGGER.info("Homo studies: " + res.size() + " result(s)");
			assertTrue(res.size() != 0);
			// TODO: look for the taxon label in the study, make sure it's there
		}
		{
			Collection<TaxonLabel> empty = new ArrayList<TaxonLabel> ();
			Collection<Study> res = getTaxonLabelHome().findStudiesWithTaxonLabels(empty);
			assertNotNull(res);
			assertEquals(res.size(), 0);
		}
	}
	
	public void testFindMatricesByTaxonLabel() {
		Collection<Matrix> res = getTaxonLabelHome().findMatricesWithTaxonLabels(findHomoSapiensTL());
		assertNotNull(res);
		LOGGER.info("Homo matrices: " + res.size() + " result(s)");
		assertTrue(res.size() != 0);
		// TODO: look for the taxon label in the matrix, make sure it's there
		boolean matricesOkay = true;
		for (Matrix m : res) {
			boolean thisMatrixOkay = false;
			for (TaxonLabel tl : m.getAllTaxonLabels()) {
				if (tl.getTaxonLabel().equals("Homo sapiens")) {
					thisMatrixOkay = true;
					break;
				}
			}
			if (! thisMatrixOkay) {
				LOGGER.debug("matrix " + m + "(" + m.getId() + ") failed to contain Homo Sapiens");
				matricesOkay = false;
			}
		}
		assertTrue(matricesOkay);
	}
	
	public void testFindMatricesByTaxonVariant() {
		TaxonVariant tv = findHomoSapiensTV();
		Collection<Matrix> res = getTaxonLabelHome().findMatrices(tv);
		assertNotNull(res);
		LOGGER.info("Homo matrices: " + res.size() + " result(s)");
		assertTrue(res.size() != 0);
		// TODO: look for the taxon label in the matrix, make sure it's there
		boolean matricesOkay = true;
		for (Matrix m : res) {
			boolean thisMatrixOkay = false;
			for (TaxonLabel tl : m.getAllTaxonLabels()) {
				TaxonVariant theTv = tl.getTaxonVariant();
				if (theTv != null && theTv.equals(tv)) {
					thisMatrixOkay = true;
					break;
				}
			}
			if (! thisMatrixOkay) {
				LOGGER.debug("matrix " + m + "(" + m.getId() + ") failed to contain Homo Sapiens");
				matricesOkay = false;
			}
		}
		assertTrue(matricesOkay);
	}
	
	
	private Collection<TaxonLabel> findHomoSapiensTL() {
		return getTaxonLabelHome().findByExactString("Homo sapiens");
	}
	
	private TaxonVariant findHomoSapiensTV() {
		Collection<TaxonVariant> hSap = getTaxonLabelHome().findTaxonVariantByFullName("Homo sapiens");
		if (hSap == null) return null;
		if (hSap.isEmpty()) return null;
		return hSap.iterator().next();
	}
}

