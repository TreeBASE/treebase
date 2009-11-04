


package org.cipres.treebase.auxdata;



/*
 * Data file parsers
 */

/* Like Headline_Parser, 
 * but accept only a particular headline type, specified at construction time.
 */
class SpecificHeadlineParser extends RDParser {
	String requiredHeadlineType;
	SpecificHeadlineParser(String headlineType) {
		requiredHeadlineType = headlineType;
	}
	
	RDParserResult Parse(LazyList<Token> source) {
		RDParserResult res = (new HeadlineParser()).Parse(source);
		if (res.success() 
				&& ((ValueHeadline) res.value()).label().equals(requiredHeadlineType)) {
			return res;
		} else {
			return new RDParserFailure();
		}
	}
}
