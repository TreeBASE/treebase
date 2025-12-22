package org.cipres.treebase.auxdata;

import junit.framework.TestCase;
import static org.junit.Assert.*;

public class RDParserResultTest extends TestCase {
	Token tok = new Token("Snonk");
	LazyList<Token> empty = null;
	LazyList<Token> toks = new LazyList<Token>(tok, empty);
	ValueNone noval = new ValueNone ();
	RDParserResult res = new RDParserResult(toks, noval);
	
	public void testRemainingTokens() {
		assertSame(toks, res.remainingTokens());
	}

	public void testValue() {
		assertSame(noval, res.value());
	}

	public void testRDParserResultLazyListOfTokenValue() {
		assertNotNull(res);
		assertSame(noval, res.v);
		assertSame(toks, res.remainingTokens);
	}

	public void testRDParserResult() {
		assertNotNull(new RDParserResult());
	}

	public void testSuccess() {
		assertTrue(res.success());
	}

	public void testFailed() {
		assertFalse(res.failed());
	}

}
