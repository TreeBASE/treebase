package org.cipres.treebase.auxdata;

import java.util.Collection;

import junit.framework.TestCase;
import static org.junit.Assert.*;

public class ValueSequenceTest extends TestCase {

	ValueNone vn = new ValueNone();
	ValueSequence seq3 = new ValueSequence(vn, vn, vn);
	ValueSequence seq0 = new ValueSequence();
	
	public void testValue_Sequence() {
		assertNotNull(seq3);
		assertNotNull(seq0);
	}

	public void testSubvalue() {
		assertSame(vn, seq3.subvalue(0));
		assertSame(vn, seq3.subvalue(1));
		assertSame(vn, seq3.subvalue(2));
	}
	
	public void testSubvalues() {
		Value v3[] = seq3.subvalues();
		assertEquals(3, v3.length);
		assertSame(vn, v3[0]);
		assertSame(vn, v3[1]);
		assertSame(vn, v3[2]);
		
		Value v0[] = seq0.subvalues();
		assertEquals(0, v0.length);
	}

	public void testLength() {
		assertEquals(3, seq3.length());
		assertEquals(0, seq0.length());
	}
	
	public void testCollection() {
		Collection<Value> c0 = seq0.collection();
		assertTrue(c0.isEmpty());
		
		Collection<Value> c3 = seq3.collection();
		assertEquals(3, c3.size());
		for (Value v : c3) {
			assertSame(vn, v);
		}
	}

}
