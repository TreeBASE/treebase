/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */



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
