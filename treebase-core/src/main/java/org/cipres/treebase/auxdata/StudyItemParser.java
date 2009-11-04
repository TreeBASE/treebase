


package org.cipres.treebase.auxdata;



/*
 * Data file parsers
 */

class StudyItemParser extends RDParser {
		RDParser p = new AlternationParser(
				new HistoryParser(),
				new AuthorParser(),
				new MatrixParser()
		);
		StudyItemParser() { super(); }
		RDParserResult Parse(LazyList<Token> source) {
			return p.Parse(source);
		}
}
