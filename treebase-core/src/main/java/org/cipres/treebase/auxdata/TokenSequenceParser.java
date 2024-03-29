


package org.cipres.treebase.auxdata;

import java.util.LinkedList;

/**
 * Parser for languages that consist of a simple concatenation of tokens
 * 
 * <p>This class represents a parser that looks for a particular sequence of tokens.
 * If the upcoming input to a TokenSequenceParser is not the same as the
 * token sequence that was specified when the parser was created, parsing fails.</p>
 *  
 * <p>If parsing succeeds, the parser returns a sequence value
 * (see {@see ValueSequence} that contains the semantic values of all the
 * <em>interesting</em> tokens.</p>
 *  
 * <p>The parser is built atop {@see ConcatenationParser}, but it could be
 * reimplemented as a primitive without much trouble; this would probably
 * be more efficient.</p>
 * 
 * @author mjd 20080330
 *
 */
class TokenSequenceParser extends RDParser {
	ConcatenationParser me;
	LinkedList<Integer> interesting_result_indices 
	    = new LinkedList<Integer> ();
	
	/**
	 * Constructor
	 * 
	 * <p>Given an array of token type values (see {@see Token}) this constructs
	 * a parser which accepts only those tokens and only in the specified order.
	 * For example, suppose a typical headline looks like this:</p>
	 * 
	 * <pre>
	 *   &gt; STUDY [1]
	 * </pre>
	 * 
	 * <p>and so always consists of a "&gt;" token followed by a word token followed by 
	 * a "[" token followed by a number token followed by a "]" token followed by an end-of-line token.
	 * One could construct a parser for a headline by calling:</p>
	 * 
	 * <pre>
	 *   new TokenSequenceParser('&gt;', TT_WORD, '[', TT_NUMBER, ']', TT_EOL);
	 * </pre>
	 * 
	 * <p>On a successful parse, the parser would return a {@see ValueSequence}
	 * value containing two items: the values of the TT_WORD and TT_NUMBER tokens. 
	 * The useless values of the other four tokens would be discarded.</p>
	 * 
	 * @param ttypes a sequence of token type values
	 */
	TokenSequenceParser(int... ttypes) {
		RDParser _d[] = { };
		LinkedList<SingleTokenParser> subparsers = new LinkedList<SingleTokenParser> ();

		for (int i = 0; i < ttypes.length; i++) {
			int ttype = ttypes[i];
			subparsers.add(new SingleTokenParser(ttype));
			switch (ttype) {
			case TT_WORD:
			case TT_NUMBER:
				interesting_result_indices.add(i);
				break;
			default:
				break;
			}
		}
		me = new ConcatenationParser(subparsers.toArray(_d));
	}
	
	RDParserResult Parse(LazyList<Token> source) {
		Value _d[] = { };
		RDParserResult res = me.Parse(source);
		if (res.failed()) return res;
		ValueSequence values = (ValueSequence) res.value();
		
		LinkedList<Value> interestingValues = new LinkedList<Value> ();
		for (Integer i : interesting_result_indices) {
			interestingValues.add(values.subvalue(i));
		}
		return new RDParserResult(res.remainingTokens(),
				new ValueSequence(interestingValues.toArray(_d)));
	}
	
}
