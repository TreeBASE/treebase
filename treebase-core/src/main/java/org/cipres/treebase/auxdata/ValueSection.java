
package org.cipres.treebase.auxdata;

import java.util.HashMap;

public class ValueSection extends ValueAssignmentMap {
	String label;
    int index;
    
    ValueSection(String s, int i, HashMap<String,Value> m) {
    	super(m);
    	label = s;
    	index = i;
    }
    
	public int getIndex() { return index; }
	public String getLabel() { return label; }
}

