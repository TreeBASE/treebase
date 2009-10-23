package org.cipres.treebase.auxdata;

import junit.framework.TestCase;

public class FilterTest extends TestCase{

	class trivFilter implements Filter {
		public Value perform(Value v) {
			if (v.getClass() == ValueToken.class) {
				double d = ((ValueToken) v).nval();
				return new ValueToken(new Token(d + 1));
			} else {
				return new ValueNone();
			}
		}
	}
	
	static Filter f;
	
	public void setUp() {
		f = new trivFilter();
	}
	
	public void testPerform() {
		Value 
		  v123 = new ValueToken(new Token(123.0));
	
		Value v124 = f.perform(v123);
		assertSame(v124.getClass(), ValueToken.class);
		assertEquals(124.0, ((ValueToken) v124).nval());
	}

}
