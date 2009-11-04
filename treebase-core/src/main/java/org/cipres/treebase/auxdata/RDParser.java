


package org.cipres.treebase.auxdata;

import java.io.StreamTokenizer;
import java.io.StringReader;

/**
 * Abstract class for recursive descent parsers
 * 
 * <p>This class is the base from which the other parser classes inherit.
 * Its main method, {@see RDParser#Parse(LazyList)}, is abstract.  It also 
 * provides a few static utilities for parsers to use.  For example, it
 * re-exports the {@see StreamTokenizer} token type constants.  Some of these
 * utilities  should probably
 * be moved out to a utility class.</p>
 * 
 * <p>The input to a parser is a lazy list of tokens; see {@see LazyList} and {@see Token}.</p>
 * 
 * <p>The output from a parser is an {@see RDParserResult} value.</p>
 * 
 * <p><b>Important note:</b> The current implementation 
 * <b>does not backtrack</b>.  </p>
 * 
 * @author mjd 20071203
 *
 */
abstract class RDParser {
	static final int TT_WORD = StreamTokenizer.TT_WORD;
	static final int TT_EOL = StreamTokenizer.TT_EOL;
	static final int TT_NUMBER = StreamTokenizer.TT_NUMBER;
	static final int TT_EOF = StreamTokenizer.TT_EOF;
	/** Parse the right-hand side of an assignment, 
	 * which is usually a string, but possibly absent
	 */
	static RDParser assignment_RHS_Parser() {
		return new OptionalParser(
				new AlternationParser(
						new SingleTokenParser(TT_WORD),
						new SingleTokenParser(TT_NUMBER)
						)
		);
	}

	/** 
	 * Parse the assignments in the body of a simple structure, such as an AUTHOR, a HISTORY, or a MATRIX
	 * @return
	 */
	static RDParser Simple_Body_Parser() {
		return new RepeatedParser(
				new AssignmentParser(),
					false);
	}
	
	/**
	 * Main method for parsers: parse a sequence of tokens and return the result
	 * 
	 * <p>Given an input source,k represented as a (lazily-generated) list of tokens,
	 * apply this parser to the source to see if the input has the specified format.
	 * Return an {@see RDParserResult} value to indicate success or failure, and,
	 * if successful, what semantic value the parser assiged to the input.</p>
	 * 
	 * @param source a stream of input tokens
	 * @return the semantic value and result of the parse attempt
	 */
	abstract RDParserResult Parse(LazyList<Token> source);
	
	
	/**
	 * Like {@see #Parse}, but input is a string instead of a token stream
	 * 
	 * @param s the input string
	 * @return the result of the parse
	 */
	RDParserResult ParseString(String s) {
		return Parse(RDParser.tokenizeString(s));
	}
	
	
	/**
	 * Utility function for turning a string into a token stream
	 * 
	 * <p>Given an input string, apply {@see TreebaseStreamTokenizer} to it to produce 
	 * a token stream.  </p>
	 * 
	 * <p><b>TODO:</b> Replace TreebaseStreamTokenizer with a factory method.</p>
	 * 
	 * @param s the input string to tokenize
	 * @return the stream of tokens
	 * @author mjd
	 */
	static LazyList<Token> tokenizeString(String s) {
		TreebaseStreamTokenizer t = new TreebaseStreamTokenizer(new StringReader(s));
		Generator<Token> gen = new TokenReader(t);
		Token head = gen.another();
		
		if (head == null) return null;
		else return new LazyList<Token> (head, gen);
	}
}


	
