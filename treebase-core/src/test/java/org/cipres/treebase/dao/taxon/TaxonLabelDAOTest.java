package org.cipres.treebase.dao.taxon;

import java.util.Set;

import org.apache.log4j.Logger;
import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.taxon.TaxonLabel;
import org.cipres.treebase.domain.taxon.TaxonLabelHome;
import org.cipres.treebase.domain.taxon.TaxonLabelSet;

/**
 * @author mjd 20090223
 *
 */
public class TaxonLabelDAOTest extends AbstractDAOTest {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(TaxonLabelDAOTest.class);

	private TaxonLabelHome mFixture;

	public TaxonLabelHome getFixture() {
		return mFixture;
	}

	public void setFixture(TaxonLabelHome fixture) {
		mFixture = fixture;
	}

	public void testFindTaxonLabelSets() {
		TaxonLabel tl = (TaxonLabel) loadObject(TaxonLabel.class);
		
		Set<TaxonLabelSet> tlSets = getFixture().findTaxonLabelSets(tl);
		assertFalse(tlSets.isEmpty());
		
		for (TaxonLabelSet tls : tlSets) {
			assertTrue(tls.getTaxonLabelsReadOnly().contains(tl));
		}
	}
}
