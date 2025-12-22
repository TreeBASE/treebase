package org.cipres.treebase.auxdata;

import junit.framework.TestCase;
import static org.junit.Assert.*;

public class ActionTest extends TestCase {

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

	static GenericAction a;
	
	public void setUp() {
		Filter f =  new trivFilter();
		a = new GenericAction(f);	
	}
	
	public void testAction() {
		assertNotNull(a);
	}

	public void testPerform() {
		Value 
		  v123 = new ValueToken(new Token(123.0));
	
		Value v124 = a.perform(v123);
		assertSame(v124.getClass(), ValueToken.class);
		assertEquals(123.0, ((ValueToken) v124).nval());  // Note 123, NOT 124
	}

}
