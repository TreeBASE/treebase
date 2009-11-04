


package org.cipres.treebase.auxdata;



/**
 * Parser for trivial languages that consist of one token
 * 
 * <p>This class represents a trivial parser that succeeds if the next token
 * in its input is of a particular type, and fails otherwise.  On success, it
 * yields a value of type {@see ValueToken} containing the token it consumed.</p>
 *  
 * @author mjd 20080330
 *
 */
class SingleTokenParser extends RDParser {
	int ttype;
	/**
	 * @param target the required token type (See {@see Token})
	 */
	SingleTokenParser(int target) {
		ttype = target;
	}
	RDParserResult Parse(LazyList<Token> source) {
		if (source == null) return new RDParserFailure();
		Token t = source.head();
		if (t.type == ttype) {
     		return new RDParserResult(source.tail(), new ValueToken(t));
    	} else {
    		return new RDParserFailure();
    	}
	}
}
