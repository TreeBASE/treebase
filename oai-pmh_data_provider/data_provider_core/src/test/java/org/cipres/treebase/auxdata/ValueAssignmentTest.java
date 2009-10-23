package org.cipres.treebase.auxdata;

import junit.framework.TestCase;

public class ValueAssignmentTest extends TestCase {
	ValueNone d = new ValueNone();
	ValueAssignment v = new ValueAssignment("Key", d);

	public void testValue_Assignment() {
		assertNotNull(v);
		assertEquals("Key", v.key);
		assertSame(d, v.val);
	}

	public void testIs_none() {
		assertFalse(v.is_none());
	}

}
