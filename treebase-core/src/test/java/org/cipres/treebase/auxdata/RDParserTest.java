package org.cipres.treebase.auxdata;

import junit.framework.TestCase;
import static org.junit.Assert.*;

public class RDParserTest extends TestCase {

	public void testTokenizeString() {
		RDParser headline = new HeadlineParser();

		String s = "   >STUDY [1]\n";
		LazyList<Token> stoks = RDParser.tokenizeString(s);
		RDParserResult sres = headline.Parse(stoks);
		assertTrue(sres.success());
//		assertNull(sres.remainingTokens());
		
		String f = "   >STUDY [1]"; // missing EOL
		LazyList<Token> ftoks = RDParser.tokenizeString(f);
		RDParserResult fres = headline.Parse(ftoks);
		assertFalse(fres.success());
	}

}
