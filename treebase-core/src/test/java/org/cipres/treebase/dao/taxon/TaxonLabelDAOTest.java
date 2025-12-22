package org.cipres.treebase.dao.taxon;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;
import org.junit.Assume;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author mjd 20090223
 *
 */
public class TaxonLabelDAOTest extends AbstractDAOTest {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = LogManager.getLogger(TaxonLabelDAOTest.class);

	private TaxonLabelHome mFixture;

	public TaxonLabelHome getFixture() {
		return mFixture;
	}

	public void setFixture(TaxonLabelHome fixture) {
		mFixture = fixture;
	}

	@Test

	public void testFindTaxonLabelSets() {
		String testName = "testFindTaxonLabelSets";
		TaxonLabel tl = (TaxonLabel) loadObject(TaxonLabel.class);
		
		Assume.assumeNotNull(testName + " - empty database, test skipped", tl);
		
		Set<TaxonLabelSet> tlSets = getFixture().findTaxonLabelSets(tl);
		assertFalse(tlSets.isEmpty());
		
		for (TaxonLabelSet tls : tlSets) {
			assertTrue(tls.getTaxonLabelsReadOnly().contains(tl));
		}
	}
}
