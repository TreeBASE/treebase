/*
 * Copyright 2009 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */
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