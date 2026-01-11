package org.cipres.treebase.security;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the DelegatingPasswordEncoder.
 * 
 * Tests the password encoding and matching logic for both:
 * - Legacy plain text passwords
 * - BCrypt-encoded passwords
 * 
 * @author Security Migration
 */
public class DelegatingPasswordEncoderTest {

	private DelegatingPasswordEncoder encoder;

	@Before
	public void setUp() {
		encoder = new DelegatingPasswordEncoder();
	}

	/**
	 * Test that encoding produces a BCrypt hash.
	 */
	@Test
	public void testEncode_producesBCryptHash() {
		String rawPassword = "testPassword123";
		String encoded = encoder.encode(rawPassword);
		
		assertNotNull("Encoded password should not be null", encoded);
		assertTrue("Encoded password should start with $2a$, $2b$, or $2y$",
			encoded.startsWith("$2a$") || encoded.startsWith("$2b$") || encoded.startsWith("$2y$"));
		assertEquals("BCrypt hash length should be 60", 60, encoded.length());
	}

	/**
	 * Test that encoding produces different hashes for the same password.
	 */
	@Test
	public void testEncode_producesUniqueHashes() {
		String rawPassword = "testPassword123";
		String encoded1 = encoder.encode(rawPassword);
		String encoded2 = encoder.encode(rawPassword);
		
		assertNotEquals("Each encoding should produce a unique hash due to salt",
			encoded1, encoded2);
	}

	/**
	 * Test matching BCrypt-encoded password.
	 */
	@Test
	public void testMatches_BCryptPassword() {
		String rawPassword = "mySecurePassword";
		String encodedPassword = encoder.encode(rawPassword);
		
		assertTrue("Password should match BCrypt hash",
			encoder.matches(rawPassword, encodedPassword));
		assertFalse("Wrong password should not match",
			encoder.matches("wrongPassword", encodedPassword));
	}

	/**
	 * Test matching legacy plain text password.
	 */
	@Test
	public void testMatches_plainTextPassword() {
		String rawPassword = "legacyPlainTextPassword";
		// Legacy passwords are stored as plain text (no encoding)
		String storedPassword = "legacyPlainTextPassword";
		
		assertTrue("Plain text password should match directly",
			encoder.matches(rawPassword, storedPassword));
		assertFalse("Wrong password should not match plain text",
			encoder.matches("wrongPassword", storedPassword));
	}

	/**
	 * Test upgradeEncoding for BCrypt passwords.
	 */
	@Test
	public void testUpgradeEncoding_BCryptPassword() {
		String encodedPassword = encoder.encode("anyPassword");
		
		assertFalse("BCrypt passwords should not need upgrading",
			encoder.upgradeEncoding(encodedPassword));
	}

	/**
	 * Test upgradeEncoding for plain text passwords.
	 */
	@Test
	public void testUpgradeEncoding_plainTextPassword() {
		String plainTextPassword = "legacyPassword";
		
		assertTrue("Plain text passwords should need upgrading",
			encoder.upgradeEncoding(plainTextPassword));
	}

	/**
	 * Test handling null passwords.
	 */
	@Test
	public void testMatches_nullPasswords() {
		assertFalse("Null raw password should not match",
			encoder.matches(null, "anyPassword"));
		assertFalse("Null encoded password should not match",
			encoder.matches("anyPassword", null));
		assertFalse("Both null should not match",
			encoder.matches(null, null));
	}

	/**
	 * Test that short passwords are not mistaken for BCrypt hashes.
	 */
	@Test
	public void testMatches_shortPassword() {
		String shortPassword = "abc";
		String storedPassword = "abc";
		
		assertTrue("Short plain text password should match",
			encoder.matches(shortPassword, storedPassword));
		assertTrue("Short passwords should need upgrading",
			encoder.upgradeEncoding(storedPassword));
	}

	/**
	 * Test BCrypt hash detection with various prefixes.
	 */
	@Test
	public void testUpgradeEncoding_variousBCryptPrefixes() {
		// All valid BCrypt prefixes
		assertFalse("$2a$ prefix should be recognized as BCrypt",
			encoder.upgradeEncoding("$2a$10$abcdefghijklmnopqrstuvwxyz012345678901234567890"));
		assertFalse("$2b$ prefix should be recognized as BCrypt",
			encoder.upgradeEncoding("$2b$10$abcdefghijklmnopqrstuvwxyz012345678901234567890"));
		assertFalse("$2y$ prefix should be recognized as BCrypt",
			encoder.upgradeEncoding("$2y$10$abcdefghijklmnopqrstuvwxyz012345678901234567890"));
		
		// Invalid prefixes
		assertTrue("$1$ (MD5) should need upgrading",
			encoder.upgradeEncoding("$1$abcdefgh$ijklmnopqrstuvwxyz01234"));
		assertTrue("$5$ (SHA-256) should need upgrading",
			encoder.upgradeEncoding("$5$rounds=5000$saltsalt$hash"));
	}

	/**
	 * Test empty string handling.
	 */
	@Test
	public void testMatches_emptyStrings() {
		String emptyPassword = "";
		String storedEmpty = "";
		
		assertTrue("Empty password should match empty stored password",
			encoder.matches(emptyPassword, storedEmpty));
		
		// Empty passwords should still need upgrading
		assertTrue("Empty passwords should need upgrading",
			encoder.upgradeEncoding(storedEmpty));
	}

	/**
	 * Test that password reset flow produces correctly encoded passwords.
	 */
	@Test
	public void testPasswordResetFlow() {
		// Simulate password reset: user enters new password
		String newPassword = "MyNewSecurePassword123";
		
		// Controller encodes the password before saving
		String encodedPassword = encoder.encode(newPassword);
		
		// User tries to login with the new password
		assertTrue("User should be able to login with new password",
			encoder.matches(newPassword, encodedPassword));
		
		// The encoded password should not need upgrading
		assertFalse("Newly encoded password should not need upgrading",
			encoder.upgradeEncoding(encodedPassword));
	}

	/**
	 * Test that legacy plain text password upgrade simulation works.
	 */
	@Test
	public void testLegacyPasswordUpgradeSimulation() {
		// Simulate a legacy user with plain text password
		String legacyPassword = "oldPlainPassword";
		String storedPassword = legacyPassword; // Plain text in database
		
		// User tries to login
		assertTrue("User should be able to login with legacy password",
			encoder.matches(legacyPassword, storedPassword));
		
		// System detects password needs upgrading
		assertTrue("Legacy password should need upgrading",
			encoder.upgradeEncoding(storedPassword));
		
		// System re-encodes with BCrypt
		String upgradedPassword = encoder.encode(legacyPassword);
		
		// User can still login with upgraded password
		assertTrue("User should be able to login after upgrade",
			encoder.matches(legacyPassword, upgradedPassword));
		
		// Upgraded password no longer needs upgrading
		assertFalse("Upgraded password should not need upgrading",
			encoder.upgradeEncoding(upgradedPassword));
	}
}
