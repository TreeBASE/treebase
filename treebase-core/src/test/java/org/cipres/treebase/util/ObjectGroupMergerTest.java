package org.cipres.treebase.util;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.cipres.treebase.MockDatabase;
import org.cipres.treebase.TBMockObject;

public class ObjectGroupMergerTest extends TestCase {
	ObjectGroupMerger<TBMockObject> merger;
	MockDatabase db;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		db = new MockDatabase();
		merger = new ObjectGroupMerger<TBMockObject> (new MockDeleterFactory(db));
	}
	
	public void testNull() {
		// Nothing
	}
	
	public void testMergeGroup() {
		int[] ids = { 119, 5, 23, 37, };
		
		db.put(ids);
		
		Set<TBMockObject> group = new HashSet<TBMockObject> ();

		group.add(new TBMockObject(119));
		group.add(new TBMockObject(5));
		group.add(new TBMockObject(23));
		
		merger.mergeGroup(group);
		
		assertTrue(db.has(5));
		assertTrue(db.has(37));
		assertFalse(db.has(23));
		assertFalse(db.has(119));
	}
	

}
