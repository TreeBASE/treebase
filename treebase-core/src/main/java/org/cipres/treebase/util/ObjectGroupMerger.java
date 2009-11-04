package org.cipres.treebase.util;

import java.util.Iterator;
import java.util.Set;

import org.cipres.treebase.domain.TBPersistable;

public class ObjectGroupMerger<T extends TBPersistable> {
	ObjectDeleterFactory<T> deleterFactory;
	
	public ObjectGroupMerger(ObjectDeleterFactory<T> deleterFactory) {
		this.deleterFactory = deleterFactory;
	}
	
	/**
	 * Main method: delete all objects in the specified group, except one; update references to the deleted obvjects to refer to the remaining one instead.
	 * 
	 * @author mjd 20090414
	 * @param group - set of objects to merge into a single object
	 * @return the one remaining object
	 */
	public T mergeGroup(Set<T> group) {
		T preferred = preferredObject(group);
		ObjectDeleter<T> deleter = getDeleterFactory().deleter(preferred);
		for (T obj : group) {
			if (! obj.equals(preferred)) {
				deleter.delete(obj);
			}
		}
		return preferred;
	}
	
	/**
	 * Given a set of objects, which one will <i>not</i> be deleted by {@see #mergeGroup(Set)}?
	 * 
	 * @param group
	 * @return the "preferred" object in the group, or <b>null</b> if the group is empty
	 */
	public T preferredObject(Set<T> group) {
		if (group.isEmpty()) return null;
		
		Iterator<T> it = group.iterator();
		T preferredObject = it.next();
		Long minId = preferredObject.getId();
		
		while (it.hasNext()) {
			T obj = it.next();
			if (obj.getId().compareTo(minId) < 0) {
				preferredObject = obj;
				minId = obj.getId();
			}
		}
		
		return preferredObject;
	}

	public ObjectDeleterFactory<T> getDeleterFactory() {
		return deleterFactory;
	}

	public void setDeleterFactory(ObjectDeleterFactory<T> deleterFactory) {
		this.deleterFactory = deleterFactory;
	}
}
