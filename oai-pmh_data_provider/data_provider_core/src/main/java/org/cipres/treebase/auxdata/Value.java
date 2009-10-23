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
