package org.cipres.treebase.auxdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

import junit.framework.TestCase;

public class TreebaseStreamTokenizerTest extends TestCase {
	
	
	public void testTreebaseStreamTokenizer() {
		BufferedReader fh = new BufferedReader(new StringReader(""));
		TreebaseStreamTokenizer tst = new TreebaseStreamTokenizer(fh);
		assertNotNull(tst);
	}

	public void testNextTokenObject() throws IOException {
		final int TT_WORD = StreamTokenizer.TT_WORD;
		final int TT_NUMBER = StreamTokenizer.TT_NUMBER;
		final int TT_EOL = StreamTokenizer.TT_EOL;
		
		BufferedReader fh = 
			new BufferedReader(
					new StringReader(
							"LABEL[13] book='Bardo Thodol'\n"
							));
		TreebaseStreamTokenizer tst = new TreebaseStreamTokenizer(fh);
		
		Token t1 = tst.nextTokenObject();
		assertTrue(t1.hasType(TT_WORD));
		assertEquals("LABEL", t1.sval());
		
		Token t2 = tst.nextTokenObject();
		assertTrue(t2.hasType('['));
		
		Token t3 = tst.nextTokenObject();
		assertTrue(t3.hasType(TT_NUMBER));
		assertEquals(13, t3.ival());
		
		Token t3a = tst.nextTokenObject();
		assertTrue(t3a.hasType(']'));
		
		Token t4 = tst.nextTokenObject();
		assertTrue(t4.hasType(TT_WORD));
		assertEquals("book", t4.sval());
		
		Token t5 = tst.nextTokenObject();
		assertTrue(t5.hasType('='));
		
		Token t6 = tst.nextTokenObject();
		assertTrue(t6.hasType(TT_WORD));
		assertEquals("Bardo Thodol", t6.sval());
		
		Token t7 = tst.nextTokenObject();
		assertTrue(t7.hasType(TT_EOL));
		
	}
	
	// Regression: when I reimplemented TreebaseStreamTokenizer, I broke the handling of '_'
	// It was treated as a punctuation instead of a word character
	public void testUnderscores() throws IOException {
		final int TT_WORD = StreamTokenizer.TT_WORD;
		
		BufferedReader fh = 
			new BufferedReader(
					new StringReader(
							"word_1 word_number_2"
							));
		
		TreebaseStreamTokenizer tst = new TreebaseStreamTokenizer(fh);
		
		Token t1 = tst.nextTokenObject();
		assertTrue(t1.hasType(TT_WORD));
		assertEquals("word_1", t1.sval());
		
		Token t2 = tst.nextTokenObject();
		assertTrue(t2.hasType(TT_WORD));
		assertEquals("word_number_2", t2.sval());
		
		Token t3 = tst.nextTokenObject();
		assertNull(t3); // EOF
	}

	// Regression: did this ever work?
	public void testEscapedQuotes() throws IOException {
		final int TT_WORD = StreamTokenizer.TT_WORD;
		String s = "'Can\\'t I parse quoted strings?'";
		
		BufferedReader fh = 
			new BufferedReader(
					new StringReader(s));
		
		TreebaseStreamTokenizer tst = new TreebaseStreamTokenizer(fh);
		
		Token t1 = tst.nextTokenObject();
		assertTrue(t1.hasType(TT_WORD));
		assertEquals("Can't I parse quoted strings?", t1.sval());
		
		Token t2 = tst.nextTokenObject();
		assertNull(t2); // EOF
	}
	
	public void testEOFInQuotedString1() {
		String s = "'aaa";
		BufferedReader fh = new BufferedReader(new StringReader(s));	
		TreebaseStreamTokenizer tst = new TreebaseStreamTokenizer(fh);
		boolean exceptionOccurred = false;
	
		try {
			tst.nextTokenObject();
		} catch (IOException e) {
			exceptionOccurred = true;
		}
		assertTrue(exceptionOccurred);
	}
	
	public void testEOFInQuotedString2() {
		String s = "'bbb\\'";
		BufferedReader fh = new BufferedReader(new StringReader(s));	
		TreebaseStreamTokenizer tst = new TreebaseStreamTokenizer(fh);
		boolean exceptionOccurred = false;
	
		try {
			tst.nextTokenObject();
		} catch (IOException e) {
			exceptionOccurred = true;
		}
		assertTrue(exceptionOccurred);
	}
	
	public void testEOFInQuotedString3() {
		String s = "'ccc\\";  // Escape sequence at end of input
		BufferedReader fh = new BufferedReader(new StringReader(s));	
		TreebaseStreamTokenizer tst = new TreebaseStreamTokenizer(fh);
		boolean exceptionOccurred = false;
	
		try {
			tst.nextTokenObject();
		} catch (IOException e) {
			exceptionOccurred = true;
		}
		assertTrue(exceptionOccurred);
	}
	
}
