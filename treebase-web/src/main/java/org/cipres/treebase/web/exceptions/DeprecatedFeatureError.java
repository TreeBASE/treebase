
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
