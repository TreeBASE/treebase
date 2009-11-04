
package org.cipres.treebase.auxdata;

public class ValueHeadline extends Value {
	String label;
	int index;
	
	public ValueHeadline(String s, int i) {
		label = s;
		index = i;
	}
	public int index() { return index; }
	public String label() { return label; }
}

