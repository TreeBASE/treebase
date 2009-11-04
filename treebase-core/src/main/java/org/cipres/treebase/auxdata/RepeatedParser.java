


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
