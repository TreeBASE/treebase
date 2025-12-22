package org.cipres.treebase.domain.taxon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private static final Logger LOGGER = LogManager.getLogger(TaxonLabelTest.class);

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
		
		// Skip test if database is empty
		if (res.isEmpty()) {
			LOGGER.info("SKIPPED: testFindBySubstring - No TaxonLabel data found in database. Test requires populated database.");
			return;
		}
		
		for (TaxonLabel tl : res) {
			assertTrue(tl.getTaxonLabel().toLowerCase().contains("homo"));
		}
	}
	
	public void testFindStudies() {
		{
			Collection<TaxonLabel> homoTL = findHomoSapiensTL();
			
			// Skip test if database is empty
			if (homoTL == null || homoTL.isEmpty()) {
				LOGGER.info("SKIPPED: testFindStudies - No TaxonLabel data found in database. Test requires populated database.");
				return;
			}
			
			Collection<Study> res = getTaxonLabelHome().findStudiesWithTaxonLabels(homoTL);
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
		Collection<TaxonLabel> homoTL = findHomoSapiensTL();
		
		// Skip test if database is empty
		if (homoTL == null || homoTL.isEmpty()) {
			LOGGER.info("SKIPPED: testFindMatricesByTaxonLabel - No TaxonLabel data found in database. Test requires populated database.");
			return;
		}
		
		Collection<Matrix> res = getTaxonLabelHome().findMatricesWithTaxonLabels(homoTL);
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
		
		// Skip test if database is empty
		if (tv == null) {
			LOGGER.info("SKIPPED: testFindMatricesByTaxonVariant - No TaxonVariant data found in database. Test requires populated database.");
			return;
		}
		
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
		
	public void testTaxonLabelLengthSorting() {
		int x = 0;
		boolean isTaxonSorted = false;
		
		List<TaxonLabel> res = (List<TaxonLabel>) findHomoSapiensTL();
		LOGGER.info("Homo matrices: " + res.size() + " result(s)");
		assertNotNull(res);
		
		// Skip test if database is empty
		if (res.isEmpty()) {
			LOGGER.info("SKIPPED: testTaxonLabelLengthSorting - No TaxonLabel data found in database. Test requires populated database.");
			return;
		}
		
		TaxonLabel newTaxon = new TaxonLabel();
		newTaxon.setTaxonLabel("Homo Sapiens ABCD");
		res.add(newTaxon);
		//res.get(1).setTaxonLabel("Homo Sapiens ABCD");
		TaxonLabelSet tlSet = new TaxonLabelSet();
		tlSet.sortByTaxonLabelLength(res);
		
		//for (TaxonLabel tLabel : res) {
		for (x = 0; x < res.size(); x++) {
			if (x > 0) {
				if (res.get(x-1).getTaxonLabel().length() >= res.get(x).getTaxonLabel().length()) {		
					isTaxonSorted = true;
				}
				else {
					LOGGER.debug("Taxon Labels are not sorted");
					isTaxonSorted = false;
					break;
				}
			}
		}	
		assertTrue(isTaxonSorted);
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

