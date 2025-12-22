package org.cipres.treebase.auxdata;

import junit.framework.TestCase;
import static org.junit.Assert.*;

public class RepeatedParserTest extends TestCase {
	static final int STRING = java.io.StreamTokenizer.TT_WORD,
	EOL = java.io.StreamTokenizer.TT_EOL;	
	Token t_strings[] = { new Token("I"), new Token("like"), 
			new Token("pie"), new Token(EOL) };
	LazyList<Token> tok_strings = new LazyList<Token>(t_strings);
	LazyList<Token> tok_no_strings = tok_strings.cons(new Token('>'));
	
	SingleTokenParser tp = new SingleTokenParser(STRING);
	RepeatedParser star = new RepeatedParser(tp);
	RepeatedParser plus = new RepeatedParser(tp, false);
	
	public void testParse_star() {
		RDParserResult res1 = star.Parse(tok_strings);
		assertTrue(res1.success());
		assertSame(ValueSequence.class, res1.value().getClass());
		ValueSequence vals1 = (ValueSequence) res1.value();
		assertEquals(3, vals1.length());
		
		LazyList<Token> rest1 = res1.remainingTokens();
		assertNotNull(rest1);
		assertTrue(rest1.head().hasType(EOL));
		
		RDParserResult res2 = star.Parse(tok_no_strings);
		assertTrue(res2.success());
		assertSame(ValueSequence.class, res2.value().getClass());
		ValueSequence vals2 = (ValueSequence) res2.value();
		assertEquals(0, vals2.length());
		
		LazyList<Token> rest2 = res2.remainingTokens();
		assertNotNull(rest2);
		assertSame(tok_no_strings, rest2);
	}
	
	public void testParse_plus() {
		RDParserResult res1 = plus.Parse(tok_strings);
		assertTrue(res1.success());
		assertSame(ValueSequence.class, res1.value().getClass());
		ValueSequence vals1 = (ValueSequence) res1.value();
		assertEquals(3, vals1.length());
		
		LazyList<Token> rest1 = res1.remainingTokens();
		assertNotNull(rest1);
		assertTrue(rest1.head().hasType(EOL));
		
		RDParserResult res2 = plus.Parse(tok_no_strings);
		assertFalse(res2.success());
	}

	public void testRepeated_ParserRDParser() {
		assertNotNull(star);
		assertSame(tp, star.subparser);
		assertEquals(true, star.zero_allowed);
	}

	public void testRepeated_ParserRDParserBoolean() {
		assertNotNull(plus);
		assertSame(tp, plus.subparser);
		assertEquals(false, plus.zero_allowed);
	}

}
