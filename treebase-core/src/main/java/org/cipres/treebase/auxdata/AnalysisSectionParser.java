


package org.cipres.treebase.auxdata;

class AnalysisSectionParser extends RDParser {
	RDParser p = new ConcatenationParser(
			new SpecificHeadlineParser("ANALYSIS"),
			new AssignmentSequenceParser(),
			new RepeatedParser(new IOParser())
			);
	
	RDParserResult Parse(LazyList<Token> source) {
		RDParserResult res = p.Parse(source);
		if (res.failed()) return res;
		ValueSequence vals = (ValueSequence) res.value();
		
		ValueHeadline headline = (ValueHeadline) vals.subvalue(0);
		ValueAssignmentMap attrs = (ValueAssignmentMap) vals.subvalue(1);
		ValueSequence io_sections = (ValueSequence) vals.subvalue(2);
		
		ValueAnalysisSection vas = new ValueAnalysisSection(
				io_sections,
				headline.index(),
				attrs.map());
		return new RDParserResult(res.remainingTokens(), vas);
	}
}
