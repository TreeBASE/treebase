
package org.cipres.treebase.auxdata;

/**
 * @author mjd 20090223
 *
 */
public interface Action extends Filter {
	boolean isDisabled();
	void setDisabled(boolean d);
	
}
