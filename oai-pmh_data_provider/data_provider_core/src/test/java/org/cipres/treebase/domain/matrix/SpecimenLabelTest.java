package org.cipres.treebase.domain.matrix;

import org.cipres.treebase.dao.AbstractDAOTest;
import org.cipres.treebase.domain.taxon.SpecimenLabel;

public class SpecimenLabelTest extends AbstractDAOTest {

	public SpecimenLabelTest() {
		super();
	}

	public void testGetInfo() {
		SpecimenLabel label = null;
		while (label == null) {
			RowSegment seg = (RowSegment) loadRandomObject(RowSegment.class);
			label = seg.getSpecimenLabel();
		}
		assertNotNull(label.getInfo());
	}
}
