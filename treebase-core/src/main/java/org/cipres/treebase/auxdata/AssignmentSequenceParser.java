


package org.cipres.treebase.auxdata;

import java.util.LinkedList;

/*
 * Data file parsers
 */

class AssignmentSequenceParser extends RDParser {
	
	static RDParser p = new RepeatedParser(new AssignmentParser());
	
	AssignmentSequenceParser() { super(); }	
	
	RDParserResult Parse(LazyList<Token> source) {
		RDParserResult res = p.Parse(source);
		if (res.failed()) return res;
		
		ValueSequence seq = (ValueSequence) res.value();
		LinkedList<ValueAssignment> subvalues = new LinkedList<ValueAssignment> ();
		for (Value va: seq.subvalues()) {
			subvalues.add((ValueAssignment) va);
		}
		ValueAssignmentMap vam = new ValueAssignmentMap(subvalues);
		return new RDParserResult(res.remainingTokens(), vam);
	}
}
