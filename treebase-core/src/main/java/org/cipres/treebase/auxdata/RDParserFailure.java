
package org.cipres.treebase.auxdata;

/**
 * Trivial subclass of {@see RDParserResult} that indicates a failed parse
 * 
 * <p><b>TODO:</b> Override {@see #value()} and {@see #remainingTokens()}
 * to throw a fatal exception; they should never be called unless the parse
 * was successful.</p>
 * 
 * @author mjd
 */
public class RDParserFailure extends RDParserResult {
	RDParserFailure() { }
	
	/**
	 * Always returns false
	 */
	public boolean success() { return false; }
	/**
	 * Always returns true
	 */
	public boolean failed() { return true; } 
}

