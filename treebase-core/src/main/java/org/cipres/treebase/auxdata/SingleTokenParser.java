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



/**
 * Parser for trivial languages that consist of one token
 * 
 * <p>This class represents a trivial parser that succeeds if the next token
 * in its input is of a particular type, and fails otherwise.  On success, it
 * yields a value of type {@see ValueToken} containing the token it consumed.</p>
 *  
 * @author mjd 20080330
 *
 */
class SingleTokenParser extends RDParser {
	int ttype;
	/**
	 * @param target the required token type (See {@see Token})
	 */
	SingleTokenParser(int target) {
		ttype = target;
	}
	RDParserResult Parse(LazyList<Token> source) {
		if (source == null) return new RDParserFailure();
		Token t = source.head();
		if (t.type == ttype) {
     		return new RDParserResult(source.tail(), new ValueToken(t));
    	} else {
    		return new RDParserFailure();
    	}
	}
}
