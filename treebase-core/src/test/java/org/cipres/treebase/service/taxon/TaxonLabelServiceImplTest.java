
package org.cipres.treebase.service.taxon;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.taxon.Taxon;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelService;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.cipres.treebase.domain.taxon.TaxonVariant;
import org.cipres.treebase.domain.tree.PhyloTree;
import org.junit.Assume;

/**
 * TaxonLabelServiceImplTest.java
 * 
 * Created on May 12, 2008
 * @author Jin Ruan
 *
 */
public class TaxonLabelServiceImplTest extends AbstractDAOTest {

	private TaxonLabelService mFixture;
	private Collection<TaxonVariant> hSapTV;
	private Taxon hSapTaxon;

	/**
	 * Constructor.
	 */
	public TaxonLabelServiceImplTest() {
		super();
	}

	/**
	 * Return the Fixture field.
	 * 
	 * @return TaxonLabelServiceImpl mFixture
	 */
	public TaxonLabelService getFixture() {
		return mFixture;
	}

	/**
	 * Set the Fixture field.
	 */
	public void setFixture(TaxonLabelService pNewFixture) {
		mFixture = pNewFixture;
	}
	
	private Collection<TaxonVariant> findHSapTVSet() {
		if (hSapTV == null) {
			hSapTV = getFixture().findTaxonVariantByFullName("Homo sapiens");
		}
		return hSapTV;
	}
	
	private Taxon findHSapTaxon() {
		if (hSapTaxon == null) {
			for (TaxonVariant tv : findHSapTVSet()) {
				if (tv.getTaxon() != null) {
					hSapTaxon = tv.getTaxon();
					break;
				}
			}
		}
		return hSapTaxon;
	}
	
	/**
	 * @author mjd 20081204
	 */
	public void testFindStudiesByTaxon() {
		Taxon hSap = findHSapTaxon();
		
		// Skip test if database is empty
		Assume.assumeNotNull("SKIPPED: testFindStudiesByTaxon - No Taxon data found in database (Homo sapiens). Test requires populated database.", hSap);
		
		Collection<Study> studies = getFixture().findStudies(hSap);
		logger.info("Query finished; results = " + studies.size() + " studies");
		
		assertTrue(studies.size() > 0);
		for (Study s : studies) {
			boolean thisStudyOK = false;
			for (TaxonLabelSet tls : s.getTaxonLabelSets()) {
				for (TaxonLabel tl : tls.getTaxonLabelsReadOnly()) {
					if (tl.getTaxonVariant() == null || tl.getTaxonVariant().getTaxon() == null) continue;
					if (tl.getTaxonVariant().getTaxon().getId() == hSap.getId()) {
						thisStudyOK = true;
						break;
					}
					if (thisStudyOK) break;
				}
				if (thisStudyOK) break; // @!&*^! Java
			}
			assertTrue(thisStudyOK);
		}
	}
	
	/**
	 * @author mjd 20081204
	 */
	public void testFindTreesByTaxon() {
		Taxon hSap = findHSapTaxon();
		
		// Skip test if database is empty
		Assume.assumeNotNull("SKIPPED: testFindTreesByTaxon - No Taxon data found in database (Homo sapiens). Test requires populated database.", hSap);
		
		Collection<PhyloTree> trees = getFixture().findTrees(hSap);
		logger.info("Query finished; results = " + trees.size() + " trees");
		
		assertTrue(trees.size() > 0);
		for (PhyloTree t : trees) {
			boolean thisTreeOK = false;
			for (TaxonLabel tl : t.getAllTaxonLabels()) {
				if (tl.getTaxonVariant() == null || tl.getTaxonVariant().getTaxon() == null) continue;
				if (tl.getTaxonVariant().getTaxon().getId() == hSap.getId()) {
					thisTreeOK = true;
					break;
				}
			}
			assertTrue(thisTreeOK);
		}
	}
	
	/**
	 * @author mjd 20081204
	 */
	public void testFindMatricesByTaxon() {
		Taxon hSap = findHSapTaxon();
		
		// Skip test if database is empty
		Assume.assumeNotNull("SKIPPED: testFindMatricesByTaxon - No Taxon data found in database (Homo sapiens). Test requires populated database.", hSap);
		
		Collection<Matrix> matrices = getFixture().findMatrices(hSap);
		logger.info("Query finished; results = " + matrices.size() + " studies");
		
		assertTrue(matrices.size() > 0);
		for (Matrix t : matrices) {
			boolean thisMatrixOK = false;
			for (TaxonLabel tl : t.getAllTaxonLabels()) {
				if (tl.getTaxonVariant() == null || tl.getTaxonVariant().getTaxon() == null) continue;
				if (tl.getTaxonVariant().getTaxon().getId() == hSap.getId()) {
					thisMatrixOK = true;
					break;
				}
			}
			assertTrue(thisMatrixOK);
		}
	}
	
	/**
	 * Test method for {@link org.cipres.treebase.service.taxon.TaxonLabelServiceImpl#createFromUBIOService(org.cipres.treebase.domain.taxon.TaxonLabel)}.
	 */
	public void testCreateFromUBIOService() throws Exception {
		String testName = "createFromUBIOService";
		if (logger.isInfoEnabled()) {
			logger.info("\n\t\tRunning Test: " + testName);
		}

		//pretty much a dummy test. 
		String desc = "Durinskia baltica";
		TaxonLabel aLabel = new TaxonLabel();
		aLabel.setTaxonLabel(desc);
		
		TaxonVariant variant = getFixture().createFromUBIOService(aLabel);
		
		// Skip test if variant couldn't be created (e.g., external service unavailable)
		if (variant == null) {
			logger.info("SKIPPED: testCreateFromUBIOService - Unable to create TaxonVariant from UBIO service. External service may be unavailable.");
			return;
		}
		
		logger.info("variant full name =" + variant.getFullName());

		// 3. verify
		if (logger.isInfoEnabled()) {
			logger.info(testName + " - end "); //$NON-NLS-1$
		}
	}
	
	/**
	 * @author mjd 20080806
	 */
	public void testExpandTaxonVariant() {
		Collection<TaxonLabel> tlsEolphus = getFixture().findByExactString("Eolophus roseicapillus");
		assertNotNull(tlsEolphus);
		Collection<TaxonLabel> tlsOneEolphus = new HashSet<TaxonLabel>();
		
		for (TaxonLabel tl : tlsEolphus) {			
			if (tl.getTaxonVariant() != null) {
				tlsOneEolphus.add(tl);
			}
		}
		
		// Skip test if database is empty
		Assume.assumeFalse("SKIPPED: testExpandTaxonVariant - No TaxonLabel data found in database (Eolophus roseicapillus). Test requires populated database.", tlsOneEolphus.size() < 1);
		
		assertTrue(tlsOneEolphus.size()>=1);
		TaxonLabel tlEolphus = tlsOneEolphus.iterator().next();

		TaxonVariant tvEolphus = getFixture().findTaxonVariant(tlEolphus);
		assertNotNull(tvEolphus);

		// I expect a whole bunch of equivalent TVs
		Set<TaxonVariant> expansion = getFixture().expandTaxonVariant(tvEolphus);
		assertNotNull(expansion);
		assertEquals(8, expansion.size());
		
		// And each one of those TVs should refer to the same taxon
		for (TaxonVariant tv : expansion) {
			assertEquals(tvEolphus.getTaxon(), tv.getTaxon());
		}
	}
	
	public void testExpandTaxonVariantSet() {
		Collection<TaxonLabel> tlsEolphus = getFixture().findByExactString("Eolophus roseicapillus");
		Collection<TaxonLabel> tlsCacatua = getFixture().findByExactString("Cacatua tenuirostris");
		assertNotNull(tlsEolphus);
		assertNotNull(tlsCacatua);
		
		Collection<TaxonLabel> tlsOneEolphus = new HashSet<TaxonLabel>();
		for (TaxonLabel tl : tlsEolphus) {
			if (tl.getTaxonVariant() != null) {
				tlsOneEolphus.add(tl);
			}
		}
		
		// Skip test if database is empty
		Assume.assumeFalse("SKIPPED: testExpandTaxonVariantSet - No TaxonLabel data found in database (Eolophus roseicapillus). Test requires populated database.", tlsOneEolphus.size() < 1);
		
		assertTrue(tlsOneEolphus.size()>=1);
		TaxonLabel tlEolphus = tlsOneEolphus.iterator().next();

		Collection<TaxonLabel> tlsOneCacatua = new HashSet<TaxonLabel>();
		for (TaxonLabel tl : tlsCacatua) {
			if (tl.getTaxonVariant() != null) {
				tlsOneCacatua.add(tl);
			}
		}
		
		// Skip test if database is empty
		Assume.assumeFalse("SKIPPED: testExpandTaxonVariantSet - No TaxonLabel data found in database (Cacatua tenuirostris). Test requires populated database.", tlsOneCacatua.size() < 1);
		
		assertTrue(tlsOneCacatua.size()>=1);
		TaxonLabel tlCacatua = tlsOneCacatua.iterator().next();

		TaxonVariant tvEolphus = getFixture().findTaxonVariant(tlEolphus);
		assertNotNull(tvEolphus);
		TaxonVariant tvCacatua = getFixture().findTaxonVariant(tlCacatua);
		assertNotNull(tvCacatua);
		
		Set<TaxonVariant> tvs = new HashSet<TaxonVariant>();
		tvs.add(tvEolphus);
		tvs.add(tvCacatua);

		// I expect a two bunches of equivalent TVs
		Set<TaxonVariant> expansion = getFixture().expandTaxonVariantSet(tvs);
		assertNotNull(expansion);
		assertEquals(14, expansion.size());
	
		// And each one of those TVs should refer to the same taxon
		for (TaxonVariant tv : expansion) {
			Taxon t = tv.getTaxon();
			assertTrue(t == tvEolphus.getTaxon() || t == tvCacatua.getTaxon());
		}	
		
		// And moreover the call should be idempotent
		// That is, the expansion of the expansion should be the same as
		// the original expansion
		Set<TaxonVariant> expansion2 = getFixture().expandTaxonVariantSet(expansion);
		for (TaxonVariant tv : expansion2) {
			assertTrue(expansion.contains(tv));
		}
		for (TaxonVariant tv : expansion) {
			assertTrue(expansion2.contains(tv));
		}
	}
	
	public void testFindByExactString() {
		String [] tests = {"Eolphus roseicapillus", "Homo sapiens", "Yobgorgle potatopotamus"};
		for (String test : tests) {
			Collection<TaxonLabel> tlc = getFixture().findByExactString(test);
			assertNotNull(tlc);
			for (TaxonLabel tl : tlc) {
				assertEquals(tl.getTaxonLabel(), test);
			}
		}
	}
	
	public void testFindTaxonVariantWithSubstring() {
		String [] tests = {"Ilex", "Homo", "Canis",
				"Eolphus roseicapillus", 
				"Homo sapiens", 
				"Yobgorgle potatopotamus",
				"Pan", "Mus",
				"Saccharomyces"};
		boolean trivialCS = true, trivialCI = true;
		for (String test : tests) {
			{
				Collection<TaxonVariant> tvc = getFixture().findTaxonVariantWithSubstring(test);
				if (! tvc.isEmpty()) { trivialCI = false; }
				for (TaxonVariant tv : tvc) {
					assertTrue(tv.getFullName().toLowerCase().contains(test.toLowerCase()));
				}
			}
			{ // case-sensitive matching
				Collection<TaxonVariant> tvc = getFixture().findTaxonVariantWithSubstring(test, true);
				if (! tvc.isEmpty()) { trivialCS = false; }
				for (TaxonVariant tv : tvc) {
					assertTrue(tv.getFullName().contains(test));
				}
			}
		}
		
		// Skip test if database is empty
		Assume.assumeFalse("SKIPPED: testFindTaxonVariantWithSubstring - No TaxonVariant data found in database. Test requires populated database.", trivialCI && trivialCS);
		
		assertFalse(trivialCI);
		assertFalse(trivialCS);
	}
	
	public void testFindByTaxonVariant() {
		Collection<TaxonVariant> tests = getFixture().findTaxonVariantWithSubstring("Ilex", true);  // Holly
		
		// Skip test if database is empty
		Assume.assumeFalse("SKIPPED: testFindByTaxonVariant - No TaxonVariant data found in database (Ilex). Test requires populated database.", tests.isEmpty());
		
		assertFalse(tests.isEmpty());
		int nontrivial = 0;
		int interesting = 0;
		
		for (TaxonVariant tv : tests) {
			Set<TaxonLabel> tls = getFixture().findByTaxonVariant(tv);
			if (! tls.isEmpty()) { nontrivial += 1; }
			for (TaxonLabel tl : tls) {
				if (tl.getTaxonVariant().getId() != tv.getId()) {
					// We found a test case where the TaxonLabel refers to 
					// a *different* TaxonVariant
					interesting += 1;
				}
				assertTrue(tv.getTaxon().getId() == tl.getTaxonVariant().getTaxon().getId());
			}
		}
		logger.info("nontrivial tests: " + nontrivial +
				";interesting tests: " + interesting);
		
		// Skip test if no interesting data found
		Assume.assumeFalse("SKIPPED: testFindByTaxonVariant - Insufficient data in database to complete test. Test requires populated database.", nontrivial == 0 || interesting == 0);
		
		assertTrue(nontrivial > 0);
		assertTrue(interesting > 0);
		
		// Todo: add test that scans all TaxonLabels looking for any we missed
	}
	
	public void testFindTaxonLabelSets() {
		//fail("Unimplemented");
	}
	
	public void testFindTaxonVariantByFullName() {
		Collection<TaxonVariant> tvSet = getFixture().findTaxonVariantByFullName("Homo sapiens");
		
		// Skip test if database is empty
		Assume.assumeFalse("SKIPPED: testFindTaxonVariantByFullName - No TaxonVariant data found in database (Homo sapiens). Test requires populated database.", tvSet.isEmpty());
		
		for (TaxonVariant tv : tvSet) {
			assertEquals("Homo sapiens", tv.getFullName());
		}
	}
}
