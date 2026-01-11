package org.cipres.treebase.domain.admin;

import java.util.List;

/**
 * PasswordResetTokenHome.java
 * 
 * Home interface for PasswordResetToken data access.
 * 
 * @author Security Migration
 */
public interface PasswordResetTokenHome {

	/**
	 * Store a new password reset token.
	 *
	 * @param token the token to store
	 */
	void store(PasswordResetToken token);

	/**
	 * Find a token by its string value.
	 *
	 * @param tokenString the token string
	 * @return the PasswordResetToken or null if not found
	 */
	PasswordResetToken findByToken(String tokenString);

	/**
	 * Find all tokens for a user.
	 *
	 * @param user the user
	 * @return list of tokens for the user
	 */
	List<PasswordResetToken> findByUser(User user);

	/**
	 * Delete a token.
	 *
	 * @param token the token to delete
	 */
	void delete(PasswordResetToken token);

	/**
	 * Delete all expired tokens for cleanup.
	 *
	 * @return number of tokens deleted
	 */
	int deleteExpiredTokens();

	/**
	 * Invalidate all existing tokens for a user.
	 * Called when a new reset token is created to ensure only one valid token exists.
	 *
	 * @param user the user
	 */
	void invalidateTokensForUser(User user);
}
