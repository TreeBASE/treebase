


package org.cipres.treebase.auxdata;

import java.util.LinkedList;



/*
 * Data file parsers
 */

/* Parser for parsing simple sections that don't have subsections
 * such as "AUTHOR" and "HISTORY"
 */
class SimpleSectionParser extends RDParser {
	
    RDParser p;
	String expected_label;
	
	SimpleSectionParser(String label) { 
		expected_label = label;
		p = new ConcatenationParser(
				new SpecificHeadlineParser(label),
				new AssignmentSequenceParser()
				);		
	}	
	
	RDParserResult Parse(LazyList<Token> source) {
		RDParserResult res = p.Parse(source);
		if (res.failed()) return res;
		
		ValueSequence seq = (ValueSequence) res.value();
		ValueHeadline head = (ValueHeadline) seq.subvalue(0);
		int index = head.index();
		
		LinkedList<ValueAssignment> subvalues = new LinkedList<ValueAssignment> ();
		for (Value va: ((ValueAssignmentMap) seq.subvalue(1))) {
			subvalues.add((ValueAssignment) va);
		}
		ValueAssignmentMap vam = new ValueAssignmentMap(subvalues);
		ValueSection vss = new ValueSection(expected_label, index, vam.map());
		return new RDParserResult(res.remainingTokens(), vss);
	}
}
