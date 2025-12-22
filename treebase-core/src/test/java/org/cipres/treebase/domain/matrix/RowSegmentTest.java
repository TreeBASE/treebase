package org.cipres.treebase.domain.matrix;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.junit.Assume;
import org.junit.Test;
import static org.junit.Assert.*;

public class RowSegmentTest extends AbstractDAOTest {

	public RowSegmentTest() {
		super();
	}
	
	@Test
	
	public void testGetSpecimenInfo() {
		boolean testedSpecimen = false;

		RowSegment seg = (RowSegment) loadRandomObject(RowSegment.class);
		
		// Skip test if database is empty
		Assume.assumeNotNull("SKIPPED: testGetSpecimenInfo - No RowSegment data found in database. Test requires populated database.", seg);
		
		assertNotNull(seg.getSpecimenInfo());

	}
}

