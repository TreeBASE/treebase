package org.cipres.treebase.auxdata;

import junit.framework.TestCase;
import static org.junit.Assert.*;
import org.junit.Test;

public class RDParserResultTest extends TestCase {
	Token tok = new Token("Snonk");
	LazyList<Token> empty = null;
	LazyList<Token> toks = new LazyList<Token>(tok, empty);
	ValueNone noval = new ValueNone ();
	RDParserResult res = new RDParserResult(toks, noval);
	
	@Test

	
	public void testRemainingTokens() {
		assertSame(toks, res.remainingTokens());
	}

	@Test


	public void testValue() {
		assertSame(noval, res.value());
	}

	@Test


	public void testRDParserResultLazyListOfTokenValue() {
		assertNotNull(res);
		assertSame(noval, res.v);
		assertSame(toks, res.remainingTokens);
	}

	@Test


	public void testRDParserResult() {
		assertNotNull(new RDParserResult());
	}

	@Test


	public void testSuccess() {
		assertTrue(res.success());
	}

	@Test


	public void testFailed() {
		assertFalse(res.failed());
	}

}
