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
