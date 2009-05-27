package org.cipres.treebase.auxdata;

import java.util.HashMap;

import junit.framework.TestCase;

public class ValueAssignmentMapTest extends TestCase {

	public void testValueAssignmentMap() {
		ValueAssignmentMap vam1 = new ValueAssignmentMap();
		assertNotNull(vam1);
		assertNotNull(vam1.map);
		assertEquals(0, vam1.map.size());
		HashMap<String,Value> map1 = vam1.map();
		assertTrue(map1.isEmpty());
		
		ValueAssignment va[] = { new ValueAssignment("none", new ValueNone()),
				new ValueAssignment("name", new ValueToken(new Token("Jin Ruan"))),
				new ValueAssignment("empty map", vam1),
				new ValueAssignment("age", new ValueToken(new Token(35.6)))
		};
		
		ValueAssignmentMap vam2 = new ValueAssignmentMap(va[0], va[1], va[2], va[3]);
		assertNotNull(vam2);
		assertNotNull(vam2.map);
		assertEquals(4, vam2.map.size());
		HashMap<String,Value> map2 = vam2.map();
		assertFalse(map2.isEmpty());
		assertEquals(4, map2.size());
		assertEquals(35.6, ((ValueToken) vam2.get("age")).nval());

		ValueAssignmentMap vam3 = new ValueAssignmentMap(vam2.map());
		assertNotNull(vam3);
		assertNotNull(vam3.map);
		assertEquals(4, vam3.map.size());
		HashMap<String,Value> map3 = vam3.map();
		assertFalse(map3.isEmpty());
		assertEquals(4, map3.size());
		assertEquals(35.6, ((ValueToken) map3.get("age")).nval());
	}
	
	public void test_Tokenaccessors() {
		ValueAssignment va[] = { 
				new ValueAssignment("name", new ValueToken(new Token("Jin Ruan"))),
				new ValueAssignment("age", new ValueToken(new Token(35.6)))
		};
		
		ValueAssignmentMap vam = new ValueAssignmentMap(va);
		assertEquals("Jin Ruan", vam.getsval("name"));
		assertEquals(35.6, vam.getnval("age"));
	}
	
	public void test_iteration() {
		ValueAssignment va[] = { 
				new ValueAssignment("name", new ValueToken(new Token("Jin Ruan"))),
				new ValueAssignment("age", new ValueToken(new Token(35.6)))
		};
		HashMap<String,Boolean> seen = new HashMap<String,Boolean> ();
		
		ValueAssignmentMap vam = new ValueAssignmentMap(va);
		for (ValueAssignment v : vam) {
			seen.put(v.key(), true);
		}
		assertEquals(2, seen.size());
		assertTrue(seen.get("name"));
		assertTrue(seen.get("age"));
	}

	public void test_null_retrieval() {
		ValueAssignment va[] = { 
				new ValueAssignment("name", new ValueToken(new Token("Jin Ruan"))),
				new ValueAssignment("age", new ValueToken(new Token(35.6)))
		};
		
		ValueAssignmentMap vam = new ValueAssignmentMap(va);
		assertNotNull(vam.getString("name"));
		// This next call aborts with NPE @5526
		assertNull(vam.getString("skip"));
	}

}
