package org.cipres.treebase.auxdata;

import junit.framework.TestCase;
import static org.junit.Assert.*;
import org.junit.Test;

public class AlternationParserTest extends TestCase {

	static final int STRING = java.io.StreamTokenizer.TT_WORD,
	EOL = java.io.StreamTokenizer.TT_EOL;	
	Token t_study[] = { new Token('>'), new Token("STUDY"), 
			new Token('['), new Token(1.0), new Token(']'), 
			new Token(EOL) };
	LazyList<Token> tok_study = new LazyList<Token> (t_study);
	Token t_author[] = { new Token('>'), new Token("AUTHOR"), 
			new Token('['), new Token(2.0), new Token(']'), 
			new Token(EOL) };
	LazyList<Token> tok_author = new LazyList<Token> (t_author);
	
	SpecificHeadlineParser study = new SpecificHeadlineParser ("STUDY"),
	    author = new SpecificHeadlineParser ("AUTHOR");
	AlternationParser ap = new AlternationParser(study, author);
	
	
	@Test

	
	
	public void testParse() {
		RDParserResult res1 = ap.Parse(tok_study);
		assertTrue(res1.success());
		assertSame(ValueHeadline.class, res1.value().getClass());
		ValueHeadline vh1 = (ValueHeadline) res1.value();
		assertEquals("STUDY", vh1.label);
		assertEquals(1, vh1.index);
		
		RDParserResult res2 = ap.Parse(tok_author);
		assertTrue(res1.success());
		assertSame(ValueHeadline.class, res2.value().getClass());
		ValueHeadline vh2 = (ValueHeadline) res2.value();
		assertEquals("AUTHOR", vh2.label);
		assertEquals(2, vh2.index);
		
		RDParserResult res3 = ap.Parse(tok_study.tail());
		assertFalse(res3.success());
	}

	@Test


	public void testAlternation_Parser() {
		assertNotNull(ap);
	}

}
