package org.cipres.treebase.auxdata;

import junit.framework.TestCase;
import static org.junit.Assert.*;

public class SpecificHeadlineParserTest extends TestCase {

	static final int STRING = java.io.StreamTokenizer.TT_WORD,
	EOL = java.io.StreamTokenizer.TT_EOL;	
	Token t[] = { new Token('>'), new Token("STUDY"), 
			new Token('['), new Token(1.0), new Token(']'), 
			new Token(EOL) };
	LazyList<Token> tok1 = new LazyList<Token> (t);
	SpecificHeadlineParser study = new SpecificHeadlineParser ("STUDY"),
	    author = new SpecificHeadlineParser ("AUTHOR");

	public void testParse() {
		RDParserResult res = study.Parse(tok1);
		assertTrue(res.success());
		assertSame(ValueHeadline.class, res.value().getClass());
		ValueHeadline vh = (ValueHeadline) res.value();
		assertEquals("STUDY", vh.label);
		assertEquals(1, vh.index);
		
		RDParserResult res2 = author.Parse(tok1);
		assertFalse(res2.success());
	}
	
	public void testHeadline_Parser() {
		assertNotNull(study);
		assertNotNull(author);
	}

}
