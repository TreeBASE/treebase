
package org.cipres.treebase.auxdata;

import java.util.Collection;
import java.util.LinkedList;

public class ValueSequence extends Value {
	Value values[];
	ValueSequence(Value... v) {
		values = v;
	}
	Value subvalue(int n) {
		return values[n];
	}
	Value[] subvalues() {
		return values;
	}
	int length() { return values.length; }
	Collection<Value> collection() { 
		LinkedList<Value> llv = new LinkedList<Value> ();
		for (Value v : values) {
			llv.add(v);
		}
		return llv;
	}
}

