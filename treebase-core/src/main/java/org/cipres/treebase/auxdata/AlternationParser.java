


package org.cipres.treebase.auxdata;

import java.util.LinkedList;

/**
 * Parser for languages that consist of two or more alternatives
 * 
 * <p>This class represents a parser with several sub-parsers.
 * When asked to parse an input, it tries the sub-parsers in sequence
 * on the input until it finds one that succeeds; it then returns
 * the value that was returned by the successful sub-parser.  If none of
 * the sub-parsers succeeds, the main parser fails also.</p>
 *  
 * <p>The subparsers represent alternatives.  For example, a numeric 
 * literal 
 * might have three possible forms: it might be a floating point literal,
 * a decimal integer literal, or a hexadecimal integer literal.  If
 * <var>a</var>, <var>b</var>, and <var>c</var> are parsers for the three
 * possible forms that a numeric literal might take, then one can 
 * built the parser for a numeric literal by composing <var>a</var>, <var>b</var>, and <var>c</var>
 * with this class.  It will try each of <var>a</var>, <var>b</var>, and <var>c</var> in
 * turn on an input until it finds one that works, or gives up.</p>
 * <p><b>TODO:</b> some way to find out <em>which</em> subparser succeeded?<p>
 *
 * @author mjd 20080330
 *
 */
class AlternationParser extends RDParser {
	LinkedList<RDParser> alternatives = new LinkedList<RDParser> ();;
	AlternationParser(RDParser... parsers) {
		for (RDParser parser : parsers) {
			alternatives.add(parser);
		}
	}
	RDParserResult Parse(LazyList<Token> source){
		for (RDParser p : alternatives) {
			RDParserResult r = p.Parse(source);
			if (r.success()) { return r; } 
		}
		return new RDParserFailure();
	}
}
