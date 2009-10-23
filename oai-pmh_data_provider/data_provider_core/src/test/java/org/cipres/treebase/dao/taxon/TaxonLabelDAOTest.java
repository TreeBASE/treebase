/*
 * Copyright 2009 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */
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
