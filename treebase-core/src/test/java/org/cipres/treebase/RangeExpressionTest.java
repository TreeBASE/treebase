
package org.cipres.treebase;

import org.cipres.treebase.RangeExpression;
import org.cipres.treebase.RangeExpression.Bounds;
import org.cipres.treebase.RangeExpression.MalformedRangeExpression;
import org.cipres.treebase.dao.AbstractDAOTest;
import static org.junit.Assert.*;

/**
 * @author mjd 20081203
 */
public class RangeExpressionTest extends AbstractDAOTest {
	public void testNull() {
		
	}
	
	private void assertThrowsMalformedRangeException(String s) {
		try {
			RangeExpression re = new RangeExpression(s);
			re.buildCriteria("property");
		} catch (MalformedRangeExpression e) {
			// Okay!
			return;
		}
		fail();
	}
	
	public void testParsingErrors() {		
//		assertThrowsMalformedRangeException(""); // No, this is legal.
		
		assertThrowsMalformedRangeException("..");
		assertThrowsMalformedRangeException("foo");

		assertThrowsMalformedRangeException("50 50");
		assertThrowsMalformedRangeException("50 foo");
		assertThrowsMalformedRangeException("foo 50");
		assertThrowsMalformedRangeException(".. foo");
		assertThrowsMalformedRangeException("foo ..");
		assertThrowsMalformedRangeException("foo foo");

		assertThrowsMalformedRangeException("50 .. ..");
		assertThrowsMalformedRangeException(".. 50 ..");
		assertThrowsMalformedRangeException(".. .. 50");
		assertThrowsMalformedRangeException(".. .. ..");
		assertThrowsMalformedRangeException("50 50 ..");
		assertThrowsMalformedRangeException(".. 50 50");

		assertThrowsMalformedRangeException("50 .. foo");
		assertThrowsMalformedRangeException("foo .. bar");
	}
	
	private void assertBoundsOK(String s, Integer low, Integer high) throws RangeExpression.MalformedRangeExpression {
		RangeExpression re = new RangeExpression();
		Bounds b = re.new Bounds(s);
		
		if (low == null) {
			assertFalse(b.hasLow()); 
		} else {
			assertTrue(b.hasLow());
			assertEquals(low, b.getLow());
		}		
		
		if (high == null) {
			assertFalse(b.hasHigh()); 
		} else {
			assertTrue(b.hasHigh());
			assertEquals(high, b.getHigh());
		}
	}
	
	public void testParsing() throws MalformedRangeExpression {
		assertBoundsOK("", null, null);
		assertBoundsOK("37", 37, 37);
		assertBoundsOK("123..", 123, null);
		assertBoundsOK("..59", null, 59);
		assertBoundsOK("12..38", 12, 38);
		assertBoundsOK("5..5", 5, 5);
	}
}
