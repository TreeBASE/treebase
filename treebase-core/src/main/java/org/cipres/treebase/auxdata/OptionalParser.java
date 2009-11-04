


package org.cipres.treebase.auxdata;



/* Parse what p parses, except that if it isn't there, 
 * that's OK, return ValueNone instead. */
class OptionalParser extends RDParser {
	RDParser subparser;
	OptionalParser(RDParser p) {
		subparser = p;
	}
	RDParserResult Parse(LazyList<Token> source) {
		RDParserResult sub = subparser.Parse(source);
		if (sub.failed()) {
			return new RDParserResult(source, new ValueNone());
		} else {
			return sub;
		}
	}
}
