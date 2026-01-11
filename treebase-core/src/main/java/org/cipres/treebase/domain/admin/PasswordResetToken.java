package org.cipres.treebase.domain.admin;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PasswordResetToken.java
 * 
 * Entity for storing password reset tokens.
 * Tokens expire after a configurable time period.
 * 
 * @author Security Migration
 */
@Entity
@Table(name = "PASSWORD_RESET_TOKEN")
public class PasswordResetToken {

	private static final int DEFAULT_EXPIRATION_HOURS = 24;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TOKEN_ID")
	private Long id;

	@Column(name = "TOKEN", nullable = false, unique = true, length = 100)
	private String token;

	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;

	@Column(name = "EXPIRY_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryDate;

	@Column(name = "USED", nullable = false)
	private boolean used;

	/**
	 * Default constructor.
	 */
	public PasswordResetToken() {
		super();
	}

	/**
	 * Constructor to create a new token for a user.
	 *
	 * @param user the user requesting password reset
	 */
	public PasswordResetToken(User user) {
		this.user = user;
		this.token = generateToken();
		this.expiryDate = calculateExpiryDate(DEFAULT_EXPIRATION_HOURS);
		this.used = false;
	}

	/**
	 * Constructor to create a token with custom expiration.
	 *
	 * @param user the user requesting password reset
	 * @param expirationHours hours until token expires
	 */
	public PasswordResetToken(User user, int expirationHours) {
		this.user = user;
		this.token = generateToken();
		this.expiryDate = calculateExpiryDate(expirationHours);
		this.used = false;
	}

	/**
	 * Generate a secure random token.
	 *
	 * @return a unique token string
	 */
	private String generateToken() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Calculate the expiry date based on hours from now.
	 *
	 * @param expirationHours hours until expiration
	 * @return the expiry date
	 */
	private Date calculateExpiryDate(int expirationHours) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, expirationHours);
		return calendar.getTime();
	}

	/**
	 * Check if the token has expired.
	 *
	 * @return true if the token has expired
	 */
	public boolean isExpired() {
		return new Date().after(expiryDate);
	}

	/**
	 * Check if the token is still valid (not expired and not used).
	 *
	 * @return true if the token is valid
	 */
	public boolean isValid() {
		return !isExpired() && !used;
	}

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}
}
