


package org.cipres.treebase.auxdata;

import java.io.StreamTokenizer;

public class Token {
	String sval;
	double nval;
	int type;
	Token (String s) {
		type = StreamTokenizer.TT_WORD;
		sval = s;
	}
	Token (double d) {
		type = StreamTokenizer.TT_NUMBER;
		nval = d;
	}
	Token (int pType) {
		type = pType;
	}
	boolean hasType (int pType) {
		return type == pType;
	}
	// TODO: The ?val methods should throw an exception or perform a conversion if the type is wrong. 20090223 MJD
	String sval() { return sval; }
	int type() { return type; }
	double nval() { return nval; }
	int ival() { return (int) nval; }
	
	public String toString() {
		String s = "Token[";
	    switch (type) {
	    case StreamTokenizer.TT_WORD: s += "\"" + sval + "\""; break;
	    case StreamTokenizer.TT_NUMBER: s += nval; break;
	    case StreamTokenizer.TT_EOL: s += "[EOL]"; break;
	    default: s += "\'" + (char) type + "\'"; break;
	    }
	    s += "]";
	    return s;
	}
}

