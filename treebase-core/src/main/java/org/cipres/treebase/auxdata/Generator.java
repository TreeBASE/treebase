
package org.cipres.treebase.auxdata;

/**
 * Interface specification for a non-rewindable source of data of type <var>E</var>
 * @author mjd 2007
 *
 * @param <E>
 */
public interface Generator<E> {
	E another();
}
