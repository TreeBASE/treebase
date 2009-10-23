package org.cipres.treebase.auxdata;

import junit.framework.TestCase;

public class ValueTokenTest extends TestCase {
	Token t = new Token("Blarf");
	ValueToken v1 = new ValueToken(t);
	ValueToken v2 = new ValueToken(new Token(3.14159));
	ValueToken v3 = new ValueToken(new Token(119.0));
	ValueToken v4 = new ValueToken(new Token('>'));
	
	public void testToken() {
		assertSame(t, v1.token());
	}

	public void testValue_Token() {
		assertNotNull(v1);
		assertSame(t, v1.t);
	}

	public void testSval() {
		assertEquals("Blarf", v1.sval());
	}

	public void testNval() {
		assertEquals(3.14159, v2.nval());
	}

	public void testIval() {
		assertEquals(119, v3.ival());
	}

	public void testType() {
		assertEquals(java.io.StreamTokenizer.TT_WORD, v1.type());
		assertEquals(java.io.StreamTokenizer.TT_NUMBER, v2.type());
		assertEquals(java.io.StreamTokenizer.TT_NUMBER, v3.type());
		assertEquals('>', v4.type());
	}

	public void testIs_none() {
		assertFalse(v1.is_none());
		assertFalse(v2.is_none());
		assertFalse(v3.is_none());
		assertFalse(v4.is_none());
	}

}
