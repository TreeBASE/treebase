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



/*
 * Data file parsers
 */

/* Parse a "headline" structure from the input
 * typically looks like ">STUDY [1]" or "    >AUTHOR [3]"
 * Returns a ValueHeadline
 */
class HeadlineParser extends RDParser {
	static RDParser p
	= new TokenSequenceParser('>', TT_WORD, '[', TT_NUMBER, ']', TT_EOL);

	HeadlineParser() { }
	
	RDParserResult Parse(LazyList<Token> source){
		RDParserResult res = p.Parse(source);
		if (res.failed()) return res;
		
		ValueSequence vs = (ValueSequence) res.value();				
		ValueHeadline v = new ValueHeadline (
				((ValueToken) vs.subvalue(0)).sval(),
				((ValueToken) vs.subvalue(1)).ival()
				);
		return new RDParserResult(res.remainingTokens(), v);
	}
}
