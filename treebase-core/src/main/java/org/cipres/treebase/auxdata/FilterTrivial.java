


package org.cipres.treebase.auxdata;

/**
 * FilterTrivial is a parser filter that performs no action and 
 * returns its argument value unchanged.  That is, it does nothing.
 * 
 * @author mjd 20080804
 *
 */
public class FilterTrivial implements Filter {

	public Value perform(Value v) {
		return v;
	}
	
}