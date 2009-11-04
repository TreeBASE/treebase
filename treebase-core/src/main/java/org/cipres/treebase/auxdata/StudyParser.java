


package org.cipres.treebase.auxdata;

import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;

/*
 * Data file parsers
 */

public class StudyParser extends RDParser {
	RDParser p = new ConcatenationParser(
			new SpecificHeadlineParser("STUDY"),
			new AssignmentSequenceParser(),
			new RepeatedParser(new StudyItemParser()),
			new RepeatedParser(new AnalysisSectionParser())
			);
	
	PrintStream out = null;	
	
	RDParserResult Parse(LazyList<Token> source) {
		RDParserResult res = p.Parse(source);
		if (res.failed()) return res;
		
		ValueSequence values = (ValueSequence) res.value();
		ValueHeadline head = (ValueHeadline) values.subvalue(0);
		ValueAssignmentMap attrs = (ValueAssignmentMap) values.subvalue(1);
		
		if (out != null) {
			out.println("Parsed study #" + head.index() 
					+ ": ID = " + attrs.getsval("study_id"));
		}
		
		ValueSequence items = (ValueSequence) values.subvalue(2);
		ValueSequence analysesSequence = (ValueSequence) values.subvalue(3);
		Collection<ValueAnalysisSection> analyses = new LinkedList<ValueAnalysisSection>();
		
		// Surely there's a better way to do this?
		for (Value a : analysesSequence.collection()) {
			analyses.add((ValueAnalysisSection) a);
		}
		
		return new RDParserResult(
				res.remainingTokens(), 
				new ValueStudy(attrs, items.collection(),  analyses)
		);
	}
	
	public void setOutputStream(PrintStream out) {
		this.out = out;
	}
}
