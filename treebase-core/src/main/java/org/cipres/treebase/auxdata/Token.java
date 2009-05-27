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

