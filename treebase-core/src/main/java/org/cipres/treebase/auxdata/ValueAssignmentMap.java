
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

