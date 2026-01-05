package org.cipres.treebase.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserHome;

/**
 * Authentication success handler that automatically upgrades plain text passwords to BCrypt.
 *
 * When a user successfully authenticates with a plain text password (legacy format),
 * this handler will automatically re-encode the password using BCrypt and save it
 * to the database. This provides a seamless migration path from plain text to
 * secure password hashing without requiring users to reset their passwords.
 *
 * @author Spring Security Migration
 */
public class PasswordUpgradeAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private static final Logger LOGGER = LogManager.getLogger(PasswordUpgradeAuthenticationSuccessHandler.class);

	private UserHome userHome;
	private PasswordEncoder passwordEncoder;

	/**
	 * Constructor.
	 *
	 * @param userHome the user data access object
	 * @param passwordEncoder the password encoder (should be DelegatingPasswordEncoder)
	 */
	public PasswordUpgradeAuthenticationSuccessHandler(UserHome userHome, PasswordEncoder passwordEncoder) {
		this.userHome = userHome;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Called when authentication is successful.
	 * Checks if the user's password needs upgrading and performs the upgrade if necessary.
	 *
	 * @param request the HTTP request
	 * @param response the HTTP response
	 * @param authentication the successful authentication object
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {

		// Check if password needs upgrading
		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) principal;
			String currentPassword = userDetails.getPassword();

			// Check if password needs to be upgraded (plain text -> BCrypt)
			if (passwordEncoder.upgradeEncoding(currentPassword)) {
				upgradePassword(userDetails, request.getParameter("j_password"));
			}
		}

		// Continue with normal success handling (redirect to saved request or default target)
		super.onAuthenticationSuccess(request, response, authentication);
	}

	/**
	 * Upgrades a user's password from plain text to BCrypt.
	 *
	 * @param userDetails the authenticated user details
	 * @param rawPassword the raw password from the login form
	 */
	private void upgradePassword(UserDetails userDetails, String rawPassword) {
		if (rawPassword == null || rawPassword.isEmpty()) {
			LOGGER.warn("Cannot upgrade password: raw password not available");
			return;
		}

		try {
			// Load the user entity
			User user = userHome.findByName(userDetails.getUsername());
			if (user == null) {
				LOGGER.warn("Cannot upgrade password: user not found: {}", userDetails.getUsername());
				return;
			}

			// Encode the password with BCrypt
			String encodedPassword = passwordEncoder.encode(rawPassword);

			// Update the user's password
			user.setPassword(encodedPassword);
			userHome.save(user);

			LOGGER.info("Password upgraded to BCrypt for user: {}", userDetails.getUsername());

		} catch (Exception e) {
			LOGGER.error("Failed to upgrade password for user: {}", userDetails.getUsername(), e);
			// Don't throw exception - password upgrade is a best-effort operation
			// User can still continue with their session
		}
	}
}
