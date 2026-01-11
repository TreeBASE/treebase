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

		LOGGER.info("ResetPasswordController.showForm() called with token: " + token);

		// Always start with the base form setup
		ModelAndView mav = super.showForm(request, response, errors);

		// Check if there's an error message set by onSubmit
		Object errorAttr = request.getAttribute("errors");
		if (errorAttr != null) {
			mav.addObject("errors", errorAttr);
		}

		if (TreebaseUtil.isEmpty(token)) {
			if (errorAttr == null) {
				mav.addObject("errors", getMessageSourceAccessor().getMessage(
					"user.password.reset.token.missing"));
			}
			return mav;
		}

		PasswordResetToken resetToken = getPasswordResetTokenHome().findByToken(token);

		if (resetToken == null || !resetToken.isValid()) {
			if (errorAttr == null) {
				mav.addObject("errors", getMessageSourceAccessor().getMessage(
					"user.password.reset.token.invalid"));
			}
			return mav;
		}

		// Token is valid, add token and username to both request and model
		request.setAttribute("token", token);
		request.setAttribute("username", resetToken.getUser().getUsername());
		mav.addObject("token", token);
		mav.addObject("username", resetToken.getUser().getUsername());

		LOGGER.info("Token is valid, showing reset form for user: " + resetToken.getUser().getUsername());
		LOGGER.info("Token in request: " + request.getAttribute("token"));
		LOGGER.info("Token in model: " + mav.getModel().get("token"));

		return mav;
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

		// Validate token exists
		if (TreebaseUtil.isEmpty(token)) {
			request.setAttribute("errors", getMessageSourceAccessor().getMessage(
				"user.password.reset.token.missing"));
			return showForm(request, response, errors);
		}

		// Find and validate the token
		PasswordResetToken resetToken = getPasswordResetTokenHome().findByToken(token);

		if (resetToken == null || !resetToken.isValid()) {
			request.setAttribute("errors", getMessageSourceAccessor().getMessage(
				"user.password.reset.token.invalid"));
			return showForm(request, response, errors);
		}

		// Token is valid, validate password inputs
		if (TreebaseUtil.isEmpty(newPassword) || TreebaseUtil.isEmpty(confirmPassword)) {
			request.setAttribute("errors", getMessageSourceAccessor().getMessage(
				"user.password.reset.password.required"));
			return showForm(request, response, errors);
		}

		if (!newPassword.equals(confirmPassword)) {
			request.setAttribute("errors", getMessageSourceAccessor().getMessage(
				"user.password.reset.password.mismatch"));
			return showForm(request, response, errors);
		}

		// Validate password strength (minimum 8 characters)
		if (newPassword.length() < 8) {
			request.setAttribute("errors", getMessageSourceAccessor().getMessage(
				"user.password.reset.password.weak"));
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

		// Redirect to login with success parameter
		// The success message will be handled by login.jsp based on the ?reset=success parameter
		return new ModelAndView(getSuccessView());
	}
}
