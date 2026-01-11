package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.admin.PasswordResetToken;
import org.cipres.treebase.domain.admin.PasswordResetTokenHome;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserHome;

/**
 * ResetPasswordController.java
 * 
 * Controller to handle password reset form submission.
 * Validates the reset token and allows users to set a new password.
 * 
 * @author Security Migration
 */
public class ResetPasswordController extends BaseFormController {
	private static final Logger LOGGER = LogManager.getLogger(ResetPasswordController.class);

	private PasswordResetTokenHome mPasswordResetTokenHome;
	private UserHome mUserHome;
	private PasswordEncoder mPasswordEncoder;

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

	/**
	 * Return the UserHome field.
	 * 
	 * @return UserHome
	 */
	public UserHome getUserHome() {
		return mUserHome;
	}

	/**
	 * Set the UserHome field.
	 */
	public void setUserHome(UserHome pUserHome) {
		mUserHome = pUserHome;
	}

	/**
	 * Return the PasswordEncoder field.
	 * 
	 * @return PasswordEncoder
	 */
	public PasswordEncoder getPasswordEncoder() {
		return mPasswordEncoder;
	}

	/**
	 * Set the PasswordEncoder field.
	 */
	public void setPasswordEncoder(PasswordEncoder pPasswordEncoder) {
		mPasswordEncoder = pPasswordEncoder;
	}

	/**
	 * Show the reset password form if the token is valid.
	 */
	@Override
	protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response,
			BindException errors) throws Exception {
		
		String token = request.getParameter("token");
		
		if (TreebaseUtil.isEmpty(token)) {
			request.setAttribute("errors", getMessageSourceAccessor().getMessage(
				"user.password.reset.token.missing"));
			return new ModelAndView(getFormView());
		}

		PasswordResetToken resetToken = getPasswordResetTokenHome().findByToken(token);
		
		if (resetToken == null || !resetToken.isValid()) {
			request.setAttribute("errors", getMessageSourceAccessor().getMessage(
				"user.password.reset.token.invalid"));
			return new ModelAndView(getFormView());
		}

		// Token is valid, show the form
		request.setAttribute("token", token);
		request.setAttribute("username", resetToken.getUser().getUsername());
		
		return super.showForm(request, response, errors);
	}

	/**
	 * Handle the password reset form submission.
	 */
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) throws Exception {

		String token = request.getParameter("token");
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");

		// Validate inputs
		if (TreebaseUtil.isEmpty(token)) {
			request.setAttribute("errors", getMessageSourceAccessor().getMessage(
				"user.password.reset.token.missing"));
			return showForm(request, response, errors);
		}

		if (TreebaseUtil.isEmpty(newPassword) || TreebaseUtil.isEmpty(confirmPassword)) {
			request.setAttribute("errors", getMessageSourceAccessor().getMessage(
				"user.password.reset.password.required"));
			request.setAttribute("token", token);
			return showForm(request, response, errors);
		}

		if (!newPassword.equals(confirmPassword)) {
			request.setAttribute("errors", getMessageSourceAccessor().getMessage(
				"user.password.reset.password.mismatch"));
			request.setAttribute("token", token);
			return showForm(request, response, errors);
		}

		// Validate password strength (minimum 8 characters)
		if (newPassword.length() < 8) {
			request.setAttribute("errors", getMessageSourceAccessor().getMessage(
				"user.password.reset.password.weak"));
			request.setAttribute("token", token);
			return showForm(request, response, errors);
		}

		// Find and validate the token
		PasswordResetToken resetToken = getPasswordResetTokenHome().findByToken(token);
		
		if (resetToken == null || !resetToken.isValid()) {
			request.setAttribute("errors", getMessageSourceAccessor().getMessage(
				"user.password.reset.token.invalid"));
			return showForm(request, response, errors);
		}

		// Update the user's password
		User user = resetToken.getUser();
		String encodedPassword = getPasswordEncoder().encode(newPassword);
		user.setPassword(encodedPassword);
		getUserHome().save(user);

		// Mark the token as used
		resetToken.setUsed(true);
		getPasswordResetTokenHome().store(resetToken);

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Password reset successful for user: " + user.getUsername());
		}

		// Redirect to login with success message
		request.setAttribute("messages", getMessageSourceAccessor().getMessage(
			"user.password.reset.success"));
		
		return new ModelAndView(getSuccessView());
	}
}
