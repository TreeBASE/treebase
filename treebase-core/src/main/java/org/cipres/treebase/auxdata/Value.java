
package org.cipres.treebase.auxdata;

/* Return value from a parser */ 
/**
 * A semantic value yielded from a parser after a successful parse
 * 
 * <p>When a parser successfully processes an input, it returns the semantic
 * value of the parsed construction.  For example, a parser for expressions, 
 * presented with the input "2 + 3 * 4" might yield the number 14, or it might
 * yield an object which represents machine code for computing the value of 2 + 3 * 4.
 * </p>
 * 
 * <p>Subclasses of <tt>Value</tt> represent various types of semantic value.</p>
 * 
 * @author mjd 20071203
 *
 */
public abstract class Value { 
	/**
	 * Is the semantic value void?
	 * 
	 * <p>Some valid, parseable constructs may have no associated value.
	 * Parsers can assign a void value to these constructs.    This method 
	 * says whether a value is void.  Since nearly all values are nonvoid, 
	 * the method is defined here, to be overridden by {@see Value_None}.</p>
	 * 
	 * @return whether this is a void value
	 */
	boolean is_none() { return false; }
}
