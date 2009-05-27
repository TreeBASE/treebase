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
