package org.cipres.treebase.domain.matrix;

import org.cipres.treebase.dao.AbstractDAOTest;

public class RowSegmentTest extends AbstractDAOTest {

	public RowSegmentTest() {
		super();
	}
	
	public void testGetSpecimenInfo() {
		boolean testedSpecimen = false;

		RowSegment seg = (RowSegment) loadRandomObject(RowSegment.class);
		assertNotNull(seg.getSpecimenInfo());

	}
}

