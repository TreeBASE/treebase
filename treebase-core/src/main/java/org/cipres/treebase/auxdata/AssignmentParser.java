


package org.cipres.treebase.auxdata;

/*
 * Data file parsers
 */

/* Parser for assignments of the form "analysis_id = S280A42" */
/* TODO: rewrite to use Token_Sequence_Parser */
class AssignmentParser extends RDParser {
	static RDParser p = new ConcatenationParser(
			new SingleTokenParser(TT_WORD),
			new SingleTokenParser('='),
			assignment_RHS_Parser(),
			new SingleTokenParser(TT_EOL));

	RDParserResult Parse(LazyList<Token> source) {
		RDParserResult res = p.Parse(source);
		if (res.failed()) return res;
		
		// Wow, this sucks.  Is there a better way to express this?
		String lhs = ((ValueToken) ((ValueSequence) res.value()).subvalue(0)).sval();	
		Value rhs = ((ValueSequence) res.value()).subvalue(2);
		
		return new RDParserResult(res.remainingTokens(), 
				new ValueAssignment(lhs, rhs));
	}
}
