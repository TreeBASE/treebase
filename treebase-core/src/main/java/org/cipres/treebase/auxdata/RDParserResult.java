


package org.cipres.treebase.auxdata;
import org.cipres.treebase.auxdata.LazyList;
import org.cipres.treebase.auxdata.Token;
import org.cipres.treebase.auxdata.Value;

/**
 * Class for return values from {@see RDParser} parsers.
 * 
 * <p>A parser must return three values:
 * <ol><li>Whether the parsing attempt succeeded or failed
 *     <li>If successful, what semantic value the parser assigned to the parsed portion of the input
 *     <li>If successful, what portion of the input remained unread
 * </ol></p>
 * <p>The RDParserResult class encapsulates the three values
 * that are returned from a successful parse.</p>
 * <p>Success and failure 
 * can be queried with the {@see #success()} and {@see #failed()} methods.
 * If successful, the semantic value is returned by {@see #value()},
 * and the unparsed portion of the input is returned by
 * {@see #remainingTokens()}</p>
 * 
 * @author mjd
 */
public class RDParserResult {
	LazyList<Token> remainingTokens;
	Value v;
	
	LazyList<Token> remainingTokens() { return remainingTokens; }
	Value value() { return v; }
	RDParserResult(LazyList<Token> toks, Value nv) {
		remainingTokens = toks;
		v = nv;
	}
	public RDParserResult() { }
	/**
	 * @return true
	 */
	/* TODO: Why success / failed and not succeeded / failed ? */
	public boolean success() { return true; } 
	/**
	 * @return false
	 */
	public boolean failed() { return false; } 
}
