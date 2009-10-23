/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */



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
