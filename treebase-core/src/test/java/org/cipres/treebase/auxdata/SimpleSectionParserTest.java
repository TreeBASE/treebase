package org.cipres.treebase.auxdata;

import junit.framework.TestCase;

public class SimpleSectionParserTest extends TestCase {

	String input = "	>AUTHOR [1]\n		author_id = A415\n		first_name = D.\n		last_name = Schluter\n		email =\n";
	SimpleSectionParser ssp = new SimpleSectionParser("AUTHOR");
	
	public void testParseString () {
	  RDParserResult res = ssp.ParseString(input);
	  assertTrue(res.success());
	  assertSame(ValueSection.class, res.value().getClass());
	  ValueSection val = (ValueSection) res.value();
	  assertEquals("AUTHOR", val.getLabel());
	  assertEquals(1, val.getIndex());
	  assertEquals(4, val.size());
	  assertEquals("A415", val.getsval("author_id"));
	  assertEquals("D.", val.getsval("first_name"));
	  assertEquals("Schluter", val.getsval("last_name"));
	  assertSame(ValueNone.class, val.get("email").getClass());
	}
	
	public void testStaticScrewup () {
		SimpleSectionParser ssp_author = new SimpleSectionParser("AUTHOR");
		SimpleSectionParser ssp_buthor = new SimpleSectionParser("BUTHOR");
		String input_author = "	>AUTHOR [1]\n		author_id = A415\n		first_name = D.\n		last_name = Schluter\n		email =\n";
		String input_buthor = "	>BUTHOR [1]\n		buthor_id = A415\n		first_name = D.\n		last_name = Schluter\n		email =\n";
		
		assertTrue(ssp_author.ParseString(input_author).success());
		assertTrue(ssp_buthor.ParseString(input_buthor).success());
		assertFalse(ssp_author.ParseString(input_buthor).success());
		assertFalse(ssp_buthor.ParseString(input_author).success());
	}
}
