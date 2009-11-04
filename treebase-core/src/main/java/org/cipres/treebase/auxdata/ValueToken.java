
package org.cipres.treebase.auxdata;

public class ValueToken extends Value {
	Token t;
	Token token() { return t; }
	ValueToken(Token pt) { 
		t = pt;
	}
	String sval() { return t.sval(); }
	double nval() { return t.nval(); }
	int ival() { return t.ival(); }
	int type() { return t.type(); }
}

