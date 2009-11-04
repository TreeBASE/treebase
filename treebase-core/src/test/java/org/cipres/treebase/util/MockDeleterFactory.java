package org.cipres.treebase.util;

import org.cipres.treebase.MockDatabase;
import org.cipres.treebase.TBMockObject;

public class MockDeleterFactory extends ObjectDeleterFactory<TBMockObject> {
	MockDatabase db;
	
	public MockDeleterFactory(MockDatabase db) {
		super();
		setDb(db);
	}

	@Override
	public MockDeleter deleter(TBMockObject preferred) {
		MockDeleter del = new MockDeleter(preferred);
		del.setDb(getDb());
		return del;
	}

	public MockDatabase getDb() {
		return db;
	}

	public void setDb(MockDatabase db) {
		this.db = db;
	}
}