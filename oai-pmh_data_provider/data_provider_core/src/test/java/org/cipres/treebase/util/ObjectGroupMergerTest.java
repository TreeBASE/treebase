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
