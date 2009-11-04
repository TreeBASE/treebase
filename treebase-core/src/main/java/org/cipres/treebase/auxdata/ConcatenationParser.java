


package org.cipres.treebase.auxdata;

import java.util.LinkedList;
import java.util.List;

/**
 * Parser for languages that consist of the concatenation of other languages
 * 
 * <p>This class represents a parser with several sub-parsers.
 * When asked to parse an input, it tries each sub-parser in sequence
 * on the input.  If one of the sub-parsers fails, then the
 * entire concatenation fails.  If all of
 * the sub-parsers succeed, the main parser succeeds and yields a {@see Value_Sequence}
 * value containing the vlaues yielded by the sub-parsers.</p>
 *  
 * <p>The subparsers represent concatenated components.  For example, 
 * a declaration might consist of a type name followed by an identifier name followed by a semicolon.
 * If
 * <var>ty</var>, <var>id</var>, and <var>semi</var> are parsers for a
 * type name, an identifier name, and a semicolon, respectively, then one can 
 * built the parser for a declaration by composing <var>ty</var>, <var>id</var>,
 *  and <var>semi</var>
 * using this class.  It will try <var>ty</var> on its input, 
 * then <var>id</var> on the following input,  then <var>semi</var> 
 * on the input after that, returning the three return values, unless one of the three fails.</p>
 *
 * @author mjd 20080330
 *
 */
class ConcatenationParser extends RDParser {
	List<RDParser> concatenatedParsers = new LinkedList<RDParser> ();
	ConcatenationParser(RDParser... parsers) {
		for (RDParser parser : parsers) {
			concatenatedParsers.add(parser);
		}
	}
	RDParserResult Parse(LazyList<Token> source){
		List<Value> subvalues = new LinkedList<Value> ();
		Value _d[] = { };
		for (RDParser p : concatenatedParsers) {
			RDParserResult r = p.Parse(source);
			if (r.failed()) { return r; }  // parse failed
			subvalues.add(r.value());
			source = r.remainingTokens();
		}

		ValueSequence v = new ValueSequence(subvalues.toArray(_d));
		return new RDParserResult(source, v);
	}
}
