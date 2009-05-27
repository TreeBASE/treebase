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



/* Given a parser p, apply p repeatedly to the input
 * until it fails.  Return a sequence of the values returned by p.
 * 
 * If the constructor is given an optional zero-allowed? boolean argument,
 * it specifies whether it is acceptable for p to match zero times.
 * The default is true.  
 */
class RepeatedParser extends RDParser {
	RDParser subparser;
	boolean zero_allowed = true;
	RepeatedParser(RDParser p) {
		subparser = p;
	}
	RepeatedParser(RDParser p, boolean za) {
		subparser = p;
		zero_allowed = za;
	}
	RDParserResult Parse(LazyList<Token> source) {
		LinkedList<Value> values = new LinkedList<Value> ();
		while (true) {
			RDParserResult res = subparser.Parse(source);
			if (res.failed()) {
				if (values.isEmpty() && ! zero_allowed) return new RDParserFailure();
				else
					return new RDParserResult(source, 
							new ValueSequence(values.toArray(new Value[values.size()])));
			}
			values.add(res.value());
			source = res.remainingTokens();
		}
	}	
}
