package org.cipres.treebase.auxdata;

import junit.framework.TestCase;
import static org.junit.Assert.*;
import org.junit.Test;

public class ValueAssignmentTest extends TestCase {
	ValueNone d = new ValueNone();
	ValueAssignment v = new ValueAssignment("Key", d);

	@Test


	public void testValue_Assignment() {
		assertNotNull(v);
		assertEquals("Key", v.key);
		assertSame(d, v.val);
	}

	@Test


	public void testIs_none() {
		assertFalse(v.is_none());
	}

}
