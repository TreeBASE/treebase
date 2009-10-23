package org.cipres.treebase.util;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class OptionalHashFieldReader extends FlatFileReader<Map<String,String>> {

	OptionalHashFieldReader(Reader source, String[] requiredFieldNames, String[] optionalFieldNames) {
		this.recordFactory = new OptionalHashFieldFactory(FieldReader.defaultFieldSeparator(), requiredFieldNames, optionalFieldNames);
		this.source = source;
	}

	OptionalHashFieldReader(Reader source, String fieldSeparator, String[] requiredFieldNames, String[] optionalFieldNames) {
		this.recordFactory = new OptionalHashFieldFactory(fieldSeparator, requiredFieldNames, optionalFieldNames);
		this.source = source;
	}


	OptionalHashFieldReader(String fieldSeparator, String[] requiredFieldNames, String[] optionalFieldNames) {
		this.recordFactory = new OptionalHashFieldFactory(fieldSeparator, requiredFieldNames, optionalFieldNames);
	}

	OptionalHashFieldReader(String[] requiredFieldNames, String[] optionalFieldNames) {
		this.recordFactory = new OptionalHashFieldFactory(FieldReader.defaultFieldSeparator(), requiredFieldNames, optionalFieldNames);
	}

	class OptionalHashFieldFactory implements RecordFactory<Map<String,String>> {
		String[] fieldNames;
		OptionalFieldFactory fieldFactory;

		public OptionalHashFieldFactory(String fieldSeparator, String[] requiredFieldNames, String[] optionalFieldNames) {
			fieldFactory = new OptionalFieldFactory(fieldSeparator, requiredFieldNames.length, optionalFieldNames.length);
			fieldNames = new String[requiredFieldNames.length + optionalFieldNames.length];
			System.arraycopy(requiredFieldNames, 0, fieldNames, 0, requiredFieldNames.length);
			System.arraycopy(optionalFieldNames, 0, fieldNames, requiredFieldNames.length, optionalFieldNames.length);
		}

		public Map<String, String> makeRecord(String line) throws RecordIOException {
			String[] fields = fieldFactory.makeRecord(line);
			Map<String, String> record = new HashMap<String, String>();
			for (int i = 0; i < fields.length; i++) {
				record.put(fieldNames[i], fields[i]);
			}
			return record;
		}

	}
}

