


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
