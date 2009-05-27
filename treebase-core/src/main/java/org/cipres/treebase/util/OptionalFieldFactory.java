package org.cipres.treebase.util;

public class OptionalFieldFactory implements RecordFactory<String[]> {
	String fieldSeparator;
	int requiredFields;
	int optionalFields;

	public OptionalFieldFactory(String fieldSeparator, int requiredFields, int optionalFields) {
		this.fieldSeparator = fieldSeparator;
		this.requiredFields = requiredFields;
		this.optionalFields = optionalFields;
	}
	
	public int totalFields() {
		return this.requiredFields + this.optionalFields;
	}

	public String[] makeRecord(String line) throws RecordIOException {
		String[] f = line.split(this.fieldSeparator, -1);
		if (f.length < this.requiredFields) {
			throw new RecordIOException("Expected at least " + this.requiredFields + " fields, got " +
					f.length);
		}		
		if (f.length > this.totalFields()) {
			throw new RecordIOException("Expected at most " + this.totalFields() + " fields, got " +
					f.length);
		}
		
		return f;
	}

}
