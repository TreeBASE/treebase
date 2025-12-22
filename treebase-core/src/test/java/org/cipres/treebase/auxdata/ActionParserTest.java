package org.cipres.treebase.auxdata;

import junit.framework.TestCase;
import static org.junit.Assert.*;

/* TODO: Refactor to merge this code with Filter_ParserTest */

public class ActionParserTest extends TestCase {
	
	boolean success = false;
	Value passedValue = null;
	ActionParser ap;
	ValueToken bogusValue;

	
	public void setUp() {
		success = false;
		passedValue = null;
		ap = new ActionParser(new trivParser(), new GenericAction(new trivFilter()));
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
	
	public void testAction_Parser() {
		assertNotNull(ap);
	}

	public void testParse() {
		RDParserResult res = ap.Parse(null);
		assertTrue(success);
		assertTrue(res.success());
		assertNotNull(passedValue);
		assertFalse(passedValue.is_none());
		assertSame(bogusValue, passedValue);
		
		// Note that bogusValue is *not* clobbered by the return value of 
		// trivFilter.perform here
		assertFalse(res.value().is_none());
		assertSame(bogusValue, res.value());
	}

}
