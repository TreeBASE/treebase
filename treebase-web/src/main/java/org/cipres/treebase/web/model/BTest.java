package org.cipres.treebase.web.model;


public class BTest<T> {

	private T mMyList;

	public BTest(T listValue) {
		setMyList(listValue);
	}

	public T getMyList() {
		return mMyList;
	}

	public void setMyList(T pMyList) {
		mMyList = pMyList;
	}
}
