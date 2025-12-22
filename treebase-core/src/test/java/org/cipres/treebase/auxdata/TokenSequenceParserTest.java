package org.cipres.treebase.auxdata;

import junit.framework.TestCase;
import static org.junit.Assert.*;

public class TokenSequenceParserTest extends TestCase {

	static final int STRING = java.io.StreamTokenizer.TT_WORD,
	                 EOL = java.io.StreamTokenizer.TT_EOL;	
	Token t[] = { new Token('>'), new Token("This"), 
			new Token('-'), new Token("That"), 
			new Token(EOL) };
	TokenSequenceParser tsp = new TokenSequenceParser ('>', STRING, '-', STRING, EOL);
	LazyList<Token> tok1 = new LazyList<Token> (t);
	
	public void testParse() {
		RDParserResult res = tsp.Parse(tok1);
		assertTrue(res.success());
		assertSame(ValueSequence.class, res.value().getClass());
		ValueSequence val = (ValueSequence) res.value();
		assertEquals(2, val.length());
		assertEquals("This", ((ValueToken) val.subvalue(0)).sval());
		assertEquals("That", ((ValueToken) val.subvalue(1)).sval());
		
		RDParserResult res2 = tsp.Parse(tok1.tail());
		assertFalse(res2.success());
	}

	public void testToken_Sequence_Parser() {
		assertNotNull(tsp);
	}

}
