/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

package org.cipres.treebase.auxdata;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ValueAssignmentMap extends Value implements Iterable<ValueAssignment> {
	HashMap<String,Value> map;

	ValueAssignmentMap(ValueAssignment... asgs) {
		map = new HashMap<String,Value> ();
		for (ValueAssignment asg : asgs) {
			put(asg.key, asg.val);
		}
	}
	
	ValueAssignmentMap(Collection<ValueAssignment> asgs) {
		map = new HashMap<String,Value> ();
		for (ValueAssignment asg : asgs) {
			put(asg.key, asg.val);
		}
	}
	
	Value get(String key) {
		return map.get(key);
	}
	
	String getString(String key) {
		ValueToken v = (ValueToken) get(key);
		return v == null ? null : v.sval();
	}
	
	ValueAssignmentMap(HashMap<String,Value> m) {
		map = m;
	}
	
	public ValueAssignmentMapIterator iterator() {
		return new ValueAssignmentMapIterator(this);
	}

	HashMap<String,Value> map() {
		return map;
	}
		
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public Value get(Object key) {
		return map.get(key);
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	public Value put(String key, Value value) {
		return map.put(key, value);
	}

	public Value remove(Object key) {
		return map.remove(key);
	}

	public int size() {
		return map.size();
	}
	
	public String getsval(String key) {
		return ((ValueToken) get(key)).sval();
	}
	
	public double getnval(String key) {
		return ((ValueToken) get(key)).nval();
	}
	
	public int gettype(String key) {
		 return ((ValueToken) get(key)).type();
	}

	private class ValueAssignmentMapIterator implements Iterator {
		Iterator<Map.Entry<String,Value>> entryIter;
		
    	ValueAssignmentMapIterator(ValueAssignmentMap base) {
    		entryIter = base.map.entrySet().iterator();
    	}
    	
    	public void remove() {
    		new java.lang.UnsupportedOperationException();
    	}
    	
    	public boolean hasNext() {
    		return entryIter.hasNext();
    	}
    	
    	public ValueAssignment next() {
    		Map.Entry<String,Value> entry = entryIter.next();
    		ValueAssignment va
    		  = new ValueAssignment(entry.getKey(),
    				                 entry.getValue()
    				);
    		return va;
    	}
    	
    }
    
}

