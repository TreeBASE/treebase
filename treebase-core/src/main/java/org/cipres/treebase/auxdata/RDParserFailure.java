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

