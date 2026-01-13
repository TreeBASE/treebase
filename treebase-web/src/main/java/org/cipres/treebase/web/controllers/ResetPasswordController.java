package org.cipres.treebase.web.controllers;

import java.util.HashMap;
import java.util.Map;

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
	 * Provide reference data for the form view.
	 * This method is called by handleRequestInternal() for GET requests.
	 * Validates the reset token and adds token/username to the model if valid.
	 */
	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		String token = request.getParameter("token");

		LOGGER.info("ResetPasswordController.referenceData() called with token: " + token);

		if (TreebaseUtil.isEmpty(token)) {
			model.put("errors", getMessageSourceAccessor().getMessage(
				"user.password.reset.token.missing"));
			return model;
		}

		PasswordResetToken resetToken = getPasswordResetTokenHome().findByToken(token);

		if (resetToken == null || !resetToken.isValid()) {
			model.put("errors", getMessageSourceAccessor().getMessage(
				"user.password.reset.token.invalid"));
			return model;
		}

		// Token is valid, add token and username to model
		model.put("token", token);
		model.put("username", resetToken.getUser().getUsername());

		LOGGER.info("Token is valid, showing reset form for user: " + resetToken.getUser().getUsername());

		return model;
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
			return showFormWithError(request, errors,
				getMessageSourceAccessor().getMessage("user.password.reset.token.missing"),
				null, null);
		}

		// Find and validate the token
		PasswordResetToken resetToken = getPasswordResetTokenHome().findByToken(token);

		if (resetToken == null || !resetToken.isValid()) {
			return showFormWithError(request, errors,
				getMessageSourceAccessor().getMessage("user.password.reset.token.invalid"),
				null, null);
		}

		// Token is valid, validate password inputs
		if (TreebaseUtil.isEmpty(newPassword) || TreebaseUtil.isEmpty(confirmPassword)) {
			return showFormWithError(request, errors,
				getMessageSourceAccessor().getMessage("user.password.reset.password.required"),
				token, resetToken.getUser().getUsername());
		}

		if (!newPassword.equals(confirmPassword)) {
			return showFormWithError(request, errors,
				getMessageSourceAccessor().getMessage("user.password.reset.password.mismatch"),
				token, resetToken.getUser().getUsername());
		}

		// Validate password strength (minimum 8 characters)
		if (newPassword.length() < 8) {
			return showFormWithError(request, errors,
				getMessageSourceAccessor().getMessage("user.password.reset.password.weak"),
				token, resetToken.getUser().getUsername());
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
		// The success message will be handled by login.html based on the ?reset=success parameter
		return new ModelAndView(getSuccessView());
	}

	/**
	 * Helper method to show the form with an error message.
	 * Includes token and username in the model if available so the form can be redisplayed.
	 */
	private ModelAndView showFormWithError(HttpServletRequest request, BindException errors,
			String errorMessage, String token, String username) throws Exception {
		Map<String, Object> model = errors != null ? errors.getModel() : new HashMap<String, Object>();
		ModelAndView mav = new ModelAndView(getFormView(), model);
		mav.addObject("errors", errorMessage);
		if (token != null) {
			mav.addObject("token", token);
		}
		if (username != null) {
			mav.addObject("username", username);
		}
		return mav;
	}
}
