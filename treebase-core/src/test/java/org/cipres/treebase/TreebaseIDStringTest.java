/*
 * Copyright 2009 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

import junit.framework.TestCase;

import org.cipres.treebase.TreebaseIDString.MalformedTreebaseIDString;
import org.cipres.treebase.domain.matrix.Matrix;
import org.cipres.treebase.domain.taxon.TaxonLabel;

/**
 * @author mjd 20090312
 *
 */
public class TreebaseIDStringTest extends TestCase {

	public TreebaseIDStringTest() {
		// TODO Auto-generated constructor stub
	}

	public TreebaseIDStringTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	
	private void assertEqualLongs(Long a, Long b) {
		if (a == null) {
			assertNull(b);
		} else {
			assertNotNull(b);
			assertEquals(a.longValue(), b.longValue());
		}
	}
	
	public void testParseIDString() {
		assertEqualLongs(123L, TreebaseIDString.trimPrefix("123", null));
		assertEqualLongs(123L, TreebaseIDString.trimPrefix("123", ""));
		assertEqualLongs(123L, TreebaseIDString.trimPrefix("123", "xyz"));
		assertEqualLongs(123L, TreebaseIDString.trimPrefix("  123", null));
		assertEqualLongs(123L, TreebaseIDString.trimPrefix("  123   ", null));
		assertEqualLongs(123L, TreebaseIDString.trimPrefix("123   ", null));
		
		assertNull(TreebaseIDString.trimPrefix("S123", null));
		assertEqualLongs(123L, TreebaseIDString.trimPrefix("S123", "S"));
		assertNull(TreebaseIDString.trimPrefix("S123", "xyz"));
		assertEqualLongs(123L, TreebaseIDString.trimPrefix("  S123", "S"));
		assertEqualLongs(123L, TreebaseIDString.trimPrefix("  S123   ", "S"));
		assertEqualLongs(123L, TreebaseIDString.trimPrefix("S123   ", "S"));

		assertNull(TreebaseIDString.trimPrefix("S 123", "xyz"));
		assertNull(TreebaseIDString.trimPrefix("S 123", "S"));
		assertNull(TreebaseIDString.trimPrefix("123 456", null));
	}

	private void assertMalformedTreebaseIDString1(String arg) {
		boolean caught = false;
		try {
			new TreebaseIDString(arg);
		} catch (MalformedTreebaseIDString t) {
			caught = true;
		}
		assertTrue(caught);
	}

	public void testIDStringConstructorString() throws MalformedTreebaseIDString {
		{
			TreebaseIDString t1 = new TreebaseIDString("M1234");
			assertEquals(t1.getTBClass(), Matrix.class);
			assertEqualLongs(t1.getId(), 1234L);
		}

		{		
			TreebaseIDString t2 = new TreebaseIDString("Tl789");
			assertEquals(t2.getTBClass(), TaxonLabel.class);
			assertEqualLongs(t2.getId(), 789L);
		}		
		
		assertMalformedTreebaseIDString1("Fn567");  // Unrecognized prefix
		assertMalformedTreebaseIDString1("123");    // Missing prefix
		assertMalformedTreebaseIDString1(null);
		assertMalformedTreebaseIDString1("Tx");     // Missing ID
		assertMalformedTreebaseIDString1("Tx123a"); // Malformed ID
		assertMalformedTreebaseIDString1("Tx0x123");// Malformed ID
	}
	
	public void testIDStringConstructorStringClass() throws MalformedTreebaseIDString {
		{
			// Mismatched prefix type is OK since "mustMatch" is not set
			TreebaseIDString t1 = new TreebaseIDString("M1234", TaxonLabel.class);
			assertEquals(t1.getTBClass(), Matrix.class);
			assertEqualLongs(t1.getId(), 1234L);
		}
		
		{
			TreebaseIDString t2 = new TreebaseIDString("Tl789", TaxonLabel.class);
			assertEquals(t2.getTBClass(), TaxonLabel.class);
			assertEqualLongs(t2.getId(), 789L);
		}
		
		{ // Missing prefix in string is acceptable here
			TreebaseIDString t3 = new TreebaseIDString("789", TaxonLabel.class);
			assertEquals(t3.getTBClass(), TaxonLabel.class);
			assertEqualLongs(t3.getId(), 789L);
		}
		
		assertMalformedTreebaseIDString2("Fn567", TaxonLabel.class);  // Unrecognized prefix
		assertMalformedTreebaseIDString2(null, TaxonLabel.class);
		assertMalformedTreebaseIDString2("Tx", TaxonLabel.class);     // Missing ID
		assertMalformedTreebaseIDString2("Tx123a", TaxonLabel.class); // Malformed ID
		assertMalformedTreebaseIDString2("Tx0x123", TaxonLabel.class);// Malformed ID	
	}
	
	public void testGetPrefixOf() {
		assertEquals("Fn", TreebaseIDString.getPrefixOf("Fn567"));
		assertEquals("M", TreebaseIDString.getPrefixOf("M1234"));
		assertEquals("", TreebaseIDString.getPrefixOf("119"));
	}

	private void assertMalformedTreebaseIDString2(String string,
			Class<TaxonLabel> class1) {
		boolean caught = false;
		try {
			TreebaseIDString tbs = new TreebaseIDString(string, class1);
		} catch (MalformedTreebaseIDString t) {
			caught = true;
		}
		assertTrue(caught);	
	}

	
	public void testIDStringConstructorStringClassMandatory() throws MalformedTreebaseIDString {
		{
			TreebaseIDString t2 = new TreebaseIDString("Tl789", TaxonLabel.class, true);
			assertEquals(t2.getTBClass(), TaxonLabel.class);
			assertEqualLongs(t2.getId(), 789L);
		}
		
		{ // Missing prefix in string is acceptable here
			TreebaseIDString t3 = new TreebaseIDString("789", TaxonLabel.class, true);
			assertEquals(t3.getTBClass(), TaxonLabel.class);
			assertEqualLongs(t3.getId(), 789L);
		}
		
		assertMalformedTreebaseIDString2("Fn567", TaxonLabel.class, true);  // Unrecognized prefix
		assertMalformedTreebaseIDString2(null, TaxonLabel.class, true);
		assertMalformedTreebaseIDString2("Tx", TaxonLabel.class, true);     // Missing ID
		assertMalformedTreebaseIDString2("Tx123a", TaxonLabel.class, true); // Malformed ID
		assertMalformedTreebaseIDString2("Tx0x123", TaxonLabel.class, true);// Malformed ID	
		assertMalformedTreebaseIDString2("M1234", TaxonLabel.class, true);  // Mismatched prefix type
	}
	
	private void assertMalformedTreebaseIDString2(String string,
			Class<TaxonLabel> class1, boolean mandatory) {
		boolean caught = false;
		try {
			TreebaseIDString tbs = new TreebaseIDString(string, class1, mandatory);
		} catch (MalformedTreebaseIDString t) {
			caught = true;
		}
		assertTrue(caught);	
	}

	
	public void testWhitespaceLiberality() throws MalformedTreebaseIDString {
		String[] args = { "  M1234", "M1234  ", "\tM1234\n" };
		for (String arg : args) {
			TreebaseIDString tbs = new TreebaseIDString(arg);
			assertEquals(tbs.getTBClass(), Matrix.class);
			assertEqualLongs(tbs.getId(), 1234L);
		}
		assertMalformedTreebaseIDString1("M567  foo");  // Something post-trailing-whitespace
		assertMalformedTreebaseIDString1("M567  8");  // Something post-trailing-whitespace
		assertMalformedTreebaseIDString1("M 567");  // Embedded whitespace not allowed (today)
		assertMalformedTreebaseIDString1("   567");  // Missing prefix

	}

}
