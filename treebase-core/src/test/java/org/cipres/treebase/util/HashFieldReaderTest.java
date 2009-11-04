package org.cipres.treebase.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import junit.framework.TestCase;

public class HashFieldReaderTest extends TestCase {
	String testData = "1\tcherry\tred\n" + "2\tapple\tred\n" + 
	"3\tbanana\tyellow\n" + "4\torange\torange\n";
	HashFieldReader hf;
	String[] fieldNames = {"id", "fruit", "color"};
	
	public void setUp() {
		hf = new HashFieldReader(fieldNames);
		hf.setSource(new StringReader(testData));
	}
	
	// TODO: promote this to FlatFileReaderTest when you write it
	public void testEmptyFile() throws IOException {
		hf.setSource(new StringReader(""));
		Map<String,String> rec = hf.getRecord();
		assertNull(rec);
	}
	
	public void testCountRecs() throws IOException {
		Map<String,String> rec;
		int count = 0;
		for (rec = hf.getRecord(); rec != null; rec = hf.getRecord() ) {
			count++;
		}
		assertEquals(4, count);
	}
	
	public void testKeys() throws IOException {
		Map<String,String> rec;
		for (rec = hf.getRecord(); rec != null; rec = hf.getRecord() ) {
			assertEquals(3, rec.keySet().size());
			assertTrue(rec.containsKey("id"));
			assertTrue(rec.containsKey("fruit"));
			assertTrue(rec.containsKey("color"));
		}
	}
	
	public void testIDs() throws IOException {
		Map<String,String> rec;
		int count = 0;
		for (rec = hf.getRecord(); rec != null; rec = hf.getRecord() ) {
			count++;
			assertEquals(String.valueOf(count), rec.get("id"));
		}
	}
	
	public void testPartialRecord() throws IOException {
		hf.setSource(new StringReader("1\traspberry\tred\n" +
				"2\tblackberry\tblack")); // no trailing newline
		assertEquals("red",   hf.getRecord().get("color"));
		assertEquals("black", hf.getRecord().get("color"));
		assertNull(hf.getRecord());
	}
	
	public void testShortRecordException() {
		hf.setSource(new StringReader("1\traspberry\tred\n" +
		"2\tblackberryblack\n" +  // missing separator in record 2
		"3\tgooseberry\tgreen\n")); 
		int count = 0;
		boolean thrown = false;
		try {
			while (hf.getRecord() != null) { count++; }
		} catch (IOException e) {
			assertTrue(e.getMessage().contains(hf.atLine()));
			thrown = true;
		}
		assertTrue(thrown);
		assertEquals(count, 1);
	}
	
	public void testIterator() {
		int count = 0;
		for (Map<String,String> rec : hf) {
			count++;
			assertEquals(String.valueOf(count), rec.get("id"));	
		}
		assertEquals(4, count);
	}
}
