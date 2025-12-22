package org.cipres.treebase.auxdata;

import junit.framework.TestCase;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest extends TestCase {

	@Test


	public void testTokenString() {
		String s = "String";
		Token t = new Token(s);
		assertNotNull(t);
		assertEquals(java.io.StreamTokenizer.TT_WORD, t.type);
		assertEquals(s, t.sval);
	}

	@Test


	public void testTokenDouble() {
		double d = 3.14159;
		Token t = new Token(d);
		assertNotNull(t);
		assertEquals(java.io.StreamTokenizer.TT_NUMBER, t.type);
		assertEquals(d, t.nval);
	}

	@Test


	public void testTokenInt() {
		char c = '>';
		Token t = new Token(c);
		assertNotNull(t);
		assertEquals(c, t.type);
	}

	@Test


	public void testHasType() {
		Token t = new Token('>');
		assertTrue(t.hasType('>'));
		assertFalse(t.hasType('['));
		assertFalse(t.hasType(java.io.StreamTokenizer.TT_WORD));
		
		Token u = new Token("Potato");
		assertFalse(u.hasType('>'));
		assertFalse(u.hasType('['));
		assertTrue(u.hasType(java.io.StreamTokenizer.TT_WORD));
	}

	@Test


	public void testSval() {
		String s = "String";
		Token t = new Token(s);
		assertNotNull(t);
		assertEquals(s, t.sval());
	}

	@Test


	public void testType() {
		String s = "String";
		Token t = new Token(s);
		assertNotNull(t);
		assertEquals(java.io.StreamTokenizer.TT_WORD, t.type());
	}

	@Test


	public void testNval() {
		double d = 3.14159;
		Token t = new Token(d);
		assertNotNull(t);
		assertEquals(d, t.nval());
	}

	@Test


	public void testIval() {
		int i = 119;
		Token t = new Token((double) i);
		assertNotNull(t);
		assertEquals(i, t.ival());
	}

}
