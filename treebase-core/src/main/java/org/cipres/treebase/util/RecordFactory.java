package org.cipres.treebase.util;

public interface RecordFactory<RecordType> {
	RecordType makeRecord(String line) throws RecordIOException;
}