
package org.cipres.treebase.util;

public abstract class ObjectDeleterFactory<T> {
	public ObjectDeleterFactory() {
		super();
	}

	public abstract ObjectDeleter<T> deleter(T preferred);
}
