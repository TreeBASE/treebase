package org.cipres.treebase.domain.admin;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * The class <code>EmailAddressTest</code> contains tests for the class 
 * {@link EmailAddress}.
 * 
 * @author Copilot
 */
public class EmailAddressTest {

	/**
	 * Test that whitespace is trimmed from email addresses.
	 * This addresses issue #241 where pasted emails with whitespace fail validation.
	 */
	@Test
	public void testEmailAddressTrimsWhitespace() {
		EmailAddress email = new EmailAddress();
		
		// Test leading whitespace
		email.setEmailAddressString("  test@example.com");
		assertEquals("Leading whitespace should be trimmed", "test@example.com", email.getEmailAddressString());
		
		// Test trailing whitespace
		email.setEmailAddressString("test@example.com  ");
		assertEquals("Trailing whitespace should be trimmed", "test@example.com", email.getEmailAddressString());
		
		// Test both leading and trailing whitespace
		email.setEmailAddressString("  test@example.com  ");
		assertEquals("Both leading and trailing whitespace should be trimmed", "test@example.com", email.getEmailAddressString());
		
		// Test tabs and mixed whitespace
		email.setEmailAddressString("\t test@example.com \t");
		assertEquals("Tabs and spaces should be trimmed", "test@example.com", email.getEmailAddressString());
	}

	/**
	 * Test that normal email addresses without whitespace are not affected.
	 */
	@Test
	public void testNormalEmailAddressUnaffected() {
		EmailAddress email = new EmailAddress();
		
		email.setEmailAddressString("test@example.com");
		assertEquals("Normal email should be unchanged", "test@example.com", email.getEmailAddressString());
	}

	/**
	 * Test that null email addresses are handled correctly.
	 */
	@Test
	public void testNullEmailAddress() {
		EmailAddress email = new EmailAddress();
		
		email.setEmailAddressString(null);
		assertNull("Null email should remain null", email.getEmailAddressString());
	}

	/**
	 * Test that empty string email addresses are handled correctly.
	 */
	@Test
	public void testEmptyEmailAddress() {
		EmailAddress email = new EmailAddress();
		
		email.setEmailAddressString("");
		assertEquals("Empty string should remain empty", "", email.getEmailAddressString());
	}

	/**
	 * Test that whitespace-only email addresses become empty after trim.
	 */
	@Test
	public void testWhitespaceOnlyEmailAddress() {
		EmailAddress email = new EmailAddress();
		
		email.setEmailAddressString("   ");
		assertEquals("Whitespace-only should become empty string", "", email.getEmailAddressString());
	}

	/**
	 * Test that the constructor also trims whitespace.
	 */
	@Test
	public void testConstructorTrimsWhitespace() {
		EmailAddress email = new EmailAddress("  test@example.com  ");
		assertEquals("Constructor should trim whitespace", "test@example.com", email.getEmailAddressString());
	}
}
