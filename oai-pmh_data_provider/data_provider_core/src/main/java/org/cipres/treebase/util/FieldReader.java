package org.cipres.treebase.util;

class FieldReader extends FlatFileReader<String[]> {
	
	FieldReader(String regex, int nFields) {
		super();
		this.recordFactory = new FieldFactory(regex, nFields);
	}
	
	FieldReader(int nFields) {
		super();
		this.recordFactory = new FieldFactory(defaultFieldSeparator(), nFields);
	}

	static String defaultFieldSeparator() {
		return "[ \\t\\n\\r\\f]+";
	}
}