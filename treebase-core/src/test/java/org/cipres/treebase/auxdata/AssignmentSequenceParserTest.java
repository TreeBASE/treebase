package org.cipres.treebase.auxdata;

import junit.framework.TestCase;
import static org.junit.Assert.*;
import org.junit.Test;

public class AssignmentSequenceParserTest extends TestCase {
	
	AssignmentSequenceParser asp = new AssignmentSequenceParser();
	static final int STRING = java.io.StreamTokenizer.TT_WORD,
                     NUMBER = java.io.StreamTokenizer.TT_NUMBER,	
	                    EOL = java.io.StreamTokenizer.TT_EOL;	
	Token t_asgs[] = { new Token("author_id"), new Token('='), 
			new Token("A415"), new Token(EOL), new Token("first_name"), new Token('='),
			new Token("D."), new Token(EOL), new Token("last_name"), new Token('='),
			new Token("Schluter"), new Token(EOL), new Token("email"), new Token('='),
			new Token(EOL) };
	LazyList<Token> tok_asgs = new LazyList<Token>(t_asgs);

	@Test


	public void testAssignmentSequenceParser() {
		assertNotNull(asp);
	}
	
	@Test

	
	public void testParse() {
		RDParserResult res1 = asp.Parse(tok_asgs);
		assertTrue(res1.success());
	}

}
