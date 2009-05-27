/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ 
 * All Rights Reserved. 
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for educational, research and non-profit purposes, without
 * fee, and without a written agreement is hereby granted, provided that the
 * above copyright notice, this paragraph and the following two paragraphs
 * appear in all copies. 
 *
 * Permission to incorporate this software into commercial products may be
 * obtained by contacting us:
 *              http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event
 * shall the CIPRES project be liable to any party for direct, indirect,
 * special, incidental, or consequential damages, including lost profits, 
 * arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage.  
 * The CIPRES project specifically disclaims any warranties, including, but
 * not limited to, the implied warranties of merchantability and fitness for
 * a particular purpose. The CIPRES project has no obligations to provide 
 * maintenance, support, updates, enhancements, or modifications. 
 */

package org.cipres.treebase.web.exceptions;

/**
 * 
 * <p>Throw this error when some eliminated piece of code is invoked by mistake.</p>
 * 
 * <p>Treebase is full of old unfinidhed code put in by Lucie Chan and others. To eliminate this code, we first
 * identify a method <var>M</var> that can probably be eliminated.  If <var>M</var> is not called from anywhere, we can just cut it out.
 * But if <var>M</var> is called from other code, which we suspect is not itself called, we can replace <var>M</var>
 * with a method that throws this exception.  Later on, we can study and eliminate the callers of <var>M</var>.</p>
 * 
 * @author mjd 20081218
 *
 */
@SuppressWarnings("serial")
public class DeprecatedFeatureError extends Error {

	public DeprecatedFeatureError(String string) {
		super(string);
	}

}
