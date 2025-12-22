package org.cipres.treebase.domain.matrix;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.taxon.SpecimenLabel;
import org.junit.Assume;

public class SpecimenLabelTest extends AbstractDAOTest {

	public SpecimenLabelTest() {
		super();
	}

	public void testGetInfo() {
		SpecimenLabel label = null;
		RowSegment seg = (RowSegment) loadRandomObject(RowSegment.class);
		
		// Skip test if database is empty
		Assume.assumeNotNull("SKIPPED: testGetInfo - No RowSegment data found in database. Test requires populated database.", seg);
		
		while (label == null) {
			label = seg.getSpecimenLabel();
		}
		assertNotNull(label.getInfo());
	}
}
