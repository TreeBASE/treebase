package org.cipres.treebase.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * DelegatingPasswordEncoder for backward compatibility with legacy plain text passwords.
 *
 * This encoder supports both:
 * - Plain text passwords (legacy, from database)
 * - BCrypt-encoded passwords (new format)
 *
 * All new passwords are encoded with BCrypt. During authentication, if a plain text
 * password is detected, the upgradeEncoding() method will return true to trigger
 * an automatic password upgrade to BCrypt.
 *
 * @author Spring Security Migration
 */
public class DelegatingPasswordEncoder implements PasswordEncoder {

	private final BCryptPasswordEncoder bcryptEncoder;

	/**
	 * Constructor. Initializes BCrypt encoder with default strength (10).
	 */
	public DelegatingPasswordEncoder() {
		this.bcryptEncoder = new BCryptPasswordEncoder();
	}

	/**
	 * Encodes the raw password using BCrypt.
	 *
	 * @param rawPassword the password to encode
	 * @return BCrypt-encoded password
	 */
	@Override
	public String encode(CharSequence rawPassword) {
		return bcryptEncoder.encode(rawPassword);
	}

	/**
	 * Verifies a raw password against an encoded password.
	 * Supports both plain text (legacy) and BCrypt formats.
	 *
	 * @param rawPassword the raw password to verify
	 * @param encodedPassword the encoded password from database
	 * @return true if passwords match
	 */
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (encodedPassword == null || rawPassword == null) {
			return false;
		}

		// Check if the encoded password is in BCrypt format
		if (isBCryptEncoded(encodedPassword)) {
			return bcryptEncoder.matches(rawPassword, encodedPassword);
		}

		// Legacy plain text password comparison
		return rawPassword.toString().equals(encodedPassword);
	}

	/**
	 * Determines if the encoded password should be upgraded to BCrypt.
	 * Returns true for plain text passwords (legacy format).
	 *
	 * @param encodedPassword the encoded password to check
	 * @return true if password should be upgraded
	 */
	@Override
	public boolean upgradeEncoding(String encodedPassword) {
		// If it's not BCrypt encoded, it needs to be upgraded
		return !isBCryptEncoded(encodedPassword);
	}

	/**
	 * Checks if a password string is BCrypt-encoded.
	 * BCrypt hashes start with $2a$, $2b$, or $2y$.
	 *
	 * @param password the password to check
	 * @return true if BCrypt-encoded
	 */
	private boolean isBCryptEncoded(String password) {
		if (password == null || password.length() < 4) {
			return false;
		}

		return password.startsWith("$2a$") ||
		       password.startsWith("$2b$") ||
		       password.startsWith("$2y$");
	}
}
