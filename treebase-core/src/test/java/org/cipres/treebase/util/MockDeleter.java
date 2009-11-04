package org.cipres.treebase.util;

import org.cipres.treebase.MockDatabase;
import org.cipres.treebase.TBMockObject;

/**
 * Delete TBMockObjects from a MockDatabase
 * 
 * @author mjd 20090414
 *
 */
public class MockDeleter extends ObjectDeleter<TBMockObject> {
	MockDatabase db;
	boolean debug = false;

	public MockDeleter(TBMockObject preferred) {
		super(preferred);
	}

	@Override
	public void delete(TBMockObject target) {
		if (debug) System.err.println("Deleting " + target.getId() + " in favor of " + getPreferredObject().getId());
		db.del(target);
	}

	public void setDb(MockDatabase db) {
		this.db = db;
	}

	public MockDatabase getDb() {
		return db;
	}

}
