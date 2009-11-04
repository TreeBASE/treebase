


package org.cipres.treebase.auxdata;



class IOParser extends RDParser {
	RDParser p = new AlternationParser(
			new OutputTreeParser(),
			new InputMatrixParser()
			);
	IOParser() { super(); }
	RDParserResult Parse(LazyList<Token> source) {
		return p.Parse(source);
	}
 }
