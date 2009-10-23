package org.cipres.treebase.auxdata;

import junit.framework.TestCase;

public class AssignmentParserTest extends TestCase {
	AssignmentParser ap;
	
	public void setUp() {
		ap = new AssignmentParser();
	}
	
	public void testParseComplete() {
		RDParserResult res = ap.ParseString("name = \'Bill\'\n");
		assertTrue(res.success());
		assertSame(ValueAssignment.class, res.value().getClass());
		ValueAssignment va = (ValueAssignment) res.value();
		assertEquals("name", va.key());
		assertSame(ValueToken.class, va.val().getClass());
		assertEquals("Bill", ((ValueToken) va.val()).sval());
	}
	
	public void testParseTwo() {
		
	}
	
	public void testParseIncomplete() {
		RDParserResult res = ap.ParseString("name = \n");
		assertTrue(res.success());
		assertSame(ValueAssignment.class, res.value().getClass());
		ValueAssignment va = (ValueAssignment) res.value();
		assertEquals("name", va.key());
		assertSame(ValueNone.class, va.val().getClass());
	}
	
	public void testParseQuoted() {
		RDParserResult res = ap.ParseString("date = \'8/20/98\'\n");
		assertTrue(res.success());
		assertSame(ValueAssignment.class, res.value().getClass());
		ValueAssignment va = (ValueAssignment) res.value();
		assertEquals("date", va.key());
		assertSame(ValueToken.class, va.val().getClass());
		assertEquals("8/20/98", ((ValueToken) va.val()).sval());
	}
	
	public void testParseDots() {
		RDParserResult res = ap.ParseString("person = \'140.247.98.81\'\n");
		assertTrue(res.success());
		assertSame(ValueAssignment.class, res.value().getClass());
		ValueAssignment va = (ValueAssignment) res.value();
		assertEquals("person", va.key());
		assertSame(ValueToken.class, va.val().getClass());
		assertEquals("140.247.98.81", ((ValueToken) va.val()).sval());
		
		RDParserResult res2 = ap.ParseString("matrix_name = \'Fig._1\'\n");
		assertTrue(res2.success());
		assertSame(ValueAssignment.class, res2.value().getClass());
		ValueAssignment va2 = (ValueAssignment) res2.value();
		assertEquals("matrix_name", va2.key());
		assertSame(ValueToken.class, va2.val().getClass());
		assertEquals("Fig._1", ((ValueToken) va2.val()).sval());
	}
	
	public void testParseNumber() {
		RDParserResult res = ap.ParseString("nchar = 5\n");
		assertTrue(res.success());
		assertSame(ValueAssignment.class, res.value().getClass());
		ValueAssignment va = (ValueAssignment) res.value();
		assertEquals("nchar", va.key());
		assertSame(ValueToken.class, va.val().getClass());
		assertEquals(5.0, ((ValueToken) va.val()).nval());
	}

	// On 20071205 I changed the spec on these next three
	// I told Bill Piel that future dump files should have the
	// RHS of *every* property assignment in single quotes
	// -- MJD 20071207
	
	public void testParseAmpersand() {
		RDParserResult res = ap.ParseString("tree_label = 'Fig._1C_&_H'\n");
		assertTrue(res.success());
		assertSame(ValueAssignment.class, res.value().getClass());
		ValueAssignment va = (ValueAssignment) res.value();
		assertEquals("tree_label", va.key());
		assertSame(ValueToken.class, va.val().getClass());
		assertEquals("Fig._1C_&_H", ((ValueToken) va.val()).sval());
	}
	
	public void testParseLeadingDigits() {
		RDParserResult res = ap.ParseString("analysis_name = '16S_Alone'\n");
		assertTrue(res.success());
		assertSame(ValueAssignment.class, res.value().getClass());
		ValueAssignment va = (ValueAssignment) res.value();
		assertEquals("analysis_name", va.key());
		assertSame(ValueToken.class, va.val().getClass());
		assertEquals("16S_Alone", ((ValueToken) va.val()).sval());
	}
	
	public void testParseQuestionMark() {
		RDParserResult res = ap.ParseString("software = 'Clados?'\n");
		assertTrue(res.success());
		assertSame(ValueAssignment.class, res.value().getClass());
		ValueAssignment va = (ValueAssignment) res.value();
		assertEquals("software", va.key());
		assertSame(ValueToken.class, va.val().getClass());
		assertEquals("Clados?", ((ValueToken) va.val()).sval());
	}
}
