package org.cipres.treebase;

import java.util.HashSet;
import java.util.Set;


/**
 * This is intended to simulate the TB database as simply as possible, for testing.
 * <p>Right now it can only store TBMockObjects, but in the future it could store all
 * sorts of interrelated nonpersistent TBPersistables.
 * <p>The idea is that a mock StudyService could get one of these to operate on behind the scenes,
 * and a mock TaxonService could operate on the same underlying database.
 *   
 * @author mjd 20090414
 *
 */
public class MockDatabase {
	Set<TBMockObject> db;
	
	public MockDatabase() { 
		db = new HashSet<TBMockObject>();
	}
	
	public boolean has(TBMockObject x) { return db.contains(x); }
	public boolean has(int x) { return db.contains(new TBMockObject(x)); }
	public void put(TBMockObject x) { db.add(x); }
	public void put(int... xs) { for (int x : xs) { db.add(new TBMockObject(x)); } } 
	public void del(TBMockObject x) { if (has(x)) db.remove(x); else throw new Error("Can't delete object " + x + " from mock database"); }
	
}