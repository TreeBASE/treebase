package org.cipres.treebase.util;

import java.io.Reader;

class HashFieldReader extends OptionalHashFieldReader {
	
	HashFieldReader(Reader source, String[] fieldNames) {
		super(source, fieldNames, new String[0]);
	}
	
	HashFieldReader(Reader source, String fieldSeparator, String[] fieldNames) {
		super(source, fieldSeparator, fieldNames, new String[0]);
	}
	
	
	HashFieldReader(String fieldSeparator, String[] fieldNames) {
		super(fieldSeparator, fieldNames, new String[0]);
	}
	
	HashFieldReader(String[] fieldNames) {
		super(fieldNames, new String[0]);
	}
}