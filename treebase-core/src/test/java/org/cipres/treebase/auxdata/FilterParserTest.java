package org.cipres.treebase.auxdata;

import junit.framework.TestCase;

/* TODO: Refactor to merge this code with Action_ParserTest */

public class FilterParserTest extends TestCase {
	
	boolean success = false;
	Value passedValue = null;
	FilterParser fp;
	ValueToken bogusValue;

	
	public void setUp() {
		success = false;
		passedValue = null;
		fp = new FilterParser(new trivParser(), new trivFilter());
		bogusValue = new ValueToken(new Token("Foo"));
	}
	
	class trivParser extends RDParser {		
		RDParserResult Parse(LazyList<Token> source) {
			return new RDParserResult(source, bogusValue);
		}
	}
	
	class trivFilter implements Filter {
		public Value perform(Value v) {
			passedValue = v;
			success = true;
			return new ValueNone();
		}
	}
	
	public void testFilter_Parser() {
		assertNotNull(fp);
	}

	public void testParse() {
		RDParserResult res = fp.Parse(null);
		assertTrue(success);
		assertTrue(res.success());
		assertNotNull(passedValue);
		assertFalse(passedValue.is_none());
		assertSame(bogusValue, passedValue);
		
		// Note that bogusValue *is* clobbered by the return value of 
		// trivFilter.perform here
		assertTrue(res.value().is_none());
	}

}
