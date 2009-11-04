


package org.cipres.treebase.auxdata;

/* A regular parser, but the returned value is transformed via a filter function
 * before it is returned
 */
class FilterParser extends RDParser {
	Filter f;
	RDParser p;
	FilterParser(RDParser parser, Filter filter) {
		p = parser;
		f = filter;
	}
	
	RDParserResult Parse(LazyList<Token> source) {
		RDParserResult res = p.Parse(source);
		if (res.failed()) return res;
		return new RDParserResult(
				res.remainingTokens(),
				f.perform(res.value())
				);
	}
}
