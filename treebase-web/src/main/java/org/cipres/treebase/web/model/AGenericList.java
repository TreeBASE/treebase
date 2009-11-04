
package org.cipres.treebase.web.model;

/**
 * 
 * @author madhu
 * 
 * @param <T>
 */
public class AGenericList<T> {

	private T mMyList;

	public AGenericList(T listValue) {
		setMyList(listValue);
	}

	public T getMyList() {
		return mMyList;
	}

	public void setMyList(T pMyList) {
		mMyList = pMyList;
	}
}
