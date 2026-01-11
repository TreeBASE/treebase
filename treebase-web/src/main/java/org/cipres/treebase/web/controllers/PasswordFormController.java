
package org.cipres.treebase.web.controllers;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.mail.MailSender;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.admin.PasswordResetToken;
import org.cipres.treebase.domain.admin.PasswordResetTokenHome;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserService;

/**
 * PasswordFormController.java
 * 
 * Controller to handle password reset requests.
 * Generates a secure reset token and emails a reset link to the user.
 * Note: This controller never sends passwords via email for security reasons.
 * 
 * Created on June 15, 2006
 * Modified for security compliance: passwords are no longer emailed.
 * 
 * @author lcchan
 * @author Security Migration
 * 
 */
public class PasswordFormController extends BaseFormController {
	private static final Logger LOGGER = LogManager.getLogger(PasswordFormController.class);

	private UserService mUserService;
	private PasswordResetTokenHome mPasswordResetTokenHome;
	private MailSender mailSender;

	/**
	 * Return the UserService field.
	 * 
	 * @return UserService mUserService
	 */
	public UserService getUserService() {
		return mUserService;
	}

	/**
	 * Set the UserService field.
	 */
	public void setUserService(UserService pNewUserService) {
		mUserService = pNewUserService;
	}

	/**
	 * Return the PasswordResetTokenHome field.
	 * 
	 * @return PasswordResetTokenHome
	 */
	public PasswordResetTokenHome getPasswordResetTokenHome() {
		return mPasswordResetTokenHome;
	}

	/**
	 * Set the PasswordResetTokenHome field.
	 */
	public void setPasswordResetTokenHome(PasswordResetTokenHome pPasswordResetTokenHome) {
		mPasswordResetTokenHome = pPasswordResetTokenHome;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) throws Exception {

		User user = (User) command;

		String username = user.getUsername() != null ? user.getUsername().trim() : "";
		String userEmail = user.getTmpEmailAddress() != null ? user.getTmpEmailAddress().trim() : "";
		
		if (TreebaseUtil.isEmpty(username) && TreebaseUtil.isEmpty(userEmail)) {
			request.setAttribute("errors", "Please provide either user name or email.");
			return showForm(request, response, errors);
		}
		
		// Record start time to normalize response time and prevent timing attacks
		long startTime = System.currentTimeMillis();
		final long MIN_RESPONSE_TIME_MS = 500; // Minimum response time to normalize timing
				
		// look up user information
		User u = null;
		if (!TreebaseUtil.isEmpty(username)) {
			u = mUserService.findUserByName(username);
		} else {
			List<User> users = mUserService.findUserByEmail(userEmail);			
			if (users != null && !users.isEmpty()) {
				u = users.iterator().next();
			}
		}
		
		if (u == null) {
			// For security: don't reveal whether a user exists or not
			// Always show a success message to prevent user enumeration attacks
			// Add delay to normalize response time and prevent timing attacks
			normalizeResponseTime(startTime, MIN_RESPONSE_TIME_MS);
			request.setAttribute("messages", getMessageSourceAccessor().getMessage(
				"user.password.reset.sent"));
			return showForm(request, response, errors);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Processing password reset request for user: " + u.getUsername());
		}

		// Invalidate any existing tokens for this user
		getPasswordResetTokenHome().invalidateTokensForUser(u);

		// Create a new password reset token
		PasswordResetToken resetToken = new PasswordResetToken(u);
		getPasswordResetTokenHome().store(resetToken);

		// Generate the reset URL
		String resetUrl = generateResetURL(request, resetToken.getToken());
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Password reset URL generated: " + resetUrl);
		}

		// Send the password reset email
		sendPasswordResetEmail(u, resetUrl);
		
		// Normalize response time for successful case too
		normalizeResponseTime(startTime, MIN_RESPONSE_TIME_MS);
		
		request.setAttribute("messages", getMessageSourceAccessor().getMessage(
			"user.password.reset.sent"));
		
		return showForm(request, response, errors);
	}

	/**
	 * Normalize response time to prevent timing attacks.
	 * Ensures the response takes at least minTimeMs from the start time.
	 *
	 * @param startTime the start time in milliseconds
	 * @param minTimeMs minimum response time in milliseconds
	 */
	private void normalizeResponseTime(long startTime, long minTimeMs) {
		long elapsed = System.currentTimeMillis() - startTime;
		if (elapsed < minTimeMs) {
			try {
				Thread.sleep(minTimeMs - elapsed);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Send a password reset email with a secure link.
	 * 
	 * @param user the user requesting the reset
	 * @param resetUrl the URL to reset the password
	 */
	private void sendPasswordResetEmail(User user, String resetUrl) throws Exception {
		// create mail session
		Properties props = new Properties();
		props.put("mail.smtp.host", TreebaseUtil.getSmtpHost());
		props.put("mail.smtp.port", TreebaseUtil.getSmtpPort());
		Session mailSession = Session.getDefaultInstance(props, null);
		
		// create email message header information
		MimeMessage message = new MimeMessage(mailSession);
		message.setRecipient(Message.RecipientType.TO, 
			new InternetAddress(user.getEmailAddressString(), 
				user.getLastName() + ", " + user.getFirstName()));
		message.setFrom(new InternetAddress("admin@treebase.org", "TreeBase administrator"));
		InternetAddress[] replyTo = { new InternetAddress("admin@treebase.org", "TreeBase administrator") };
		message.setReplyTo(replyTo);
		message.setSubject("TreeBASE Password Reset Request", "UTF8");
		
		// Security note: We NEVER include the password in the email
		message.setText(
			"Dear " + user.getFirstName() + " " + user.getLastName() + ",\n\n"
			+ "You have requested to reset your TreeBASE password.\n\n"
			+ "Please click the following link to reset your password:\n"
			+ resetUrl + "\n\n"
			+ "This link will expire in 24 hours.\n\n"
			+ "If you did not request this password reset, please ignore this email. "
			+ "Your password will remain unchanged.\n\n"
			+ "Best regards,\n"
			+ "TreeBASE Team\n"
			+ TreebaseUtil.getSiteUrl()
		);
		
		// send the message
		Transport.send(message);
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Password reset email sent to: " + user.getEmailAddressString());
		}
	}

	/**
	 * Generate the password reset URL with the token.
	 * 
	 * @param request the HTTP request
	 * @param token the reset token
	 * @return the full reset URL
	 */
	private String generateResetURL(HttpServletRequest request, String token) {
		StringBuilder bldrURL = new StringBuilder(request.getScheme());
		bldrURL.append("://").append(request.getServerName());
		
		int port = request.getServerPort();
		if ((request.getScheme().equals("http") && port != 80) ||
			(request.getScheme().equals("https") && port != 443)) {
			bldrURL.append(":").append(port);
		}
		
		bldrURL.append(request.getContextPath());
		bldrURL.append("/resetPassword.html?token=").append(token);

		return bldrURL.toString();
	}
}
