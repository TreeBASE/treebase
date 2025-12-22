package org.cipres.treebase.auxdata;

import junit.framework.TestCase;
import static org.junit.Assert.*;

public class SingleTokenParserTest extends TestCase {

	SingleTokenParser stp = new SingleTokenParser ('>');
	Token a[] = { new Token('>'), new Token("Snonk"), new Token(java.io.StreamTokenizer.TT_EOL) };
	LazyList<Token> tok1 = new LazyList<Token> (a);
	
	
	public void testParse() {
		RDParserResult res = stp.Parse(tok1);
		assertTrue(res.success());
		assertSame(ValueToken.class, res.value().getClass());
		assertEquals('>', ((ValueToken) res.value()).token().type());
		
		RDParserResult res2 = stp.Parse(tok1.tail());
		assertFalse(res2.success());
		
		RDParserResult res3 = stp.Parse(null);
		assertFalse(res3.success());
	}

	public void testSingleTokenParser() {
		assertNotNull(stp);
		assertEquals('>', stp.ttype);
	}

}
