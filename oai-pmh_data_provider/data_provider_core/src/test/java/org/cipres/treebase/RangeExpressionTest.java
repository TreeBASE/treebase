/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

package org.cipres.treebase;

import org.cipres.treebase.RangeExpression;
import org.cipres.treebase.RangeExpression.Bounds;
import org.cipres.treebase.RangeExpression.MalformedRangeExpression;
import org.cipres.treebase.dao.AbstractDAOTest;

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
