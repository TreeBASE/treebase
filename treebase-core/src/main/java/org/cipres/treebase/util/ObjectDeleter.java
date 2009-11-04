package org.cipres.treebase.util;

public abstract class ObjectDeleter<T> {
	T preferredObject;
	
	public ObjectDeleter() {
		super();
	}
	
	public ObjectDeleter(T preferredObject) {
		super();
		setPreferredObject(preferredObject);
	}
	
	public abstract void delete(T target);

	public T getPreferredObject() {
		return preferredObject;
	}

	public void setPreferredObject(T preferredObject) {
		this.preferredObject = preferredObject;
	}
}
