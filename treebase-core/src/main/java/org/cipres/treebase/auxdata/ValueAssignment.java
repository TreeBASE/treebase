
package org.cipres.treebase.auxdata;

public class ValueAssignment extends Value {
   String key;
   Value val;
    
    public ValueAssignment(String k, Value v) {
        key = k;  val = v;  
    }
    
	public String key() {
		return key;
	}

	public Value val() {
		return val;
	}
    
}

