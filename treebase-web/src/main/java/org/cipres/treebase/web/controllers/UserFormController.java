
package org.cipres.treebase.web.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserRole;

/**
 * UserFormController.java
 * 
 * Controller to update an existing user
 * 
 * Created on May 1, 2006
 * 
 * @author lcchan
 * 
 */
public class UserFormController extends AbstractUserController {
	private static final Logger LOGGER = LogManager.getLogger(UserFormController.class);

	private PasswordEncoder mPasswordEncoder;

	public UserFormController() {
		super();

		setCancelView("redirect:/user/submissionList.html");
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
	 * 
	 * Creation date: May 8, 2006 5:20:02 PM
	 */
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		User user = (User) command;
		String uid = request.getParameter("id");
		
		// Security check: verify the authenticated user has permission to modify this account
		String authenticatedUsername = request.getRemoteUser();
		User currentUser = getUserService().findUserByName(authenticatedUsername);
		
		if (currentUser == null) {
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"Access denied. Please log in.");
		}
		
		// Users can only modify their own profile unless they are an admin
		Long requestedId = Long.parseLong(uid);
		if (!currentUser.getId().equals(requestedId) && !currentUser.getRole().isAdmin()) {
			LOGGER.warn("User {} attempted to modify profile of user ID {}", 
				authenticatedUsername, requestedId);
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"Access denied. You can only modify your own profile.");
		}
		
		user.setId(requestedId);

		// Get the original user to preserve password if not changed
		User existingUser = getUserHome().findByUserName(user.getUsername());
		if (existingUser == null) {
			existingUser = (User) getUserHome().findPersistedObjectByID(User.class, requestedId);
		}

		// Handle password update
		String newPassword = user.getPassword();
		String retypedPassword = request.getParameter("retypedpassword");
		
		if (TreebaseUtil.isEmpty(newPassword) && TreebaseUtil.isEmpty(retypedPassword)) {
			// User didn't enter a new password - keep the existing one
			if (existingUser != null) {
				user.setPassword(existingUser.getPassword());
			}
		} else {
			// User entered a new password - validate and encode it
			if (!newPassword.equals(retypedPassword)) {
				return setAttributeAndShowForm(
					request,
					response,
					bindExp,
					"errors",
					"The passwords you typed are not identical.");
			}
			
			// Encode the new password
			String encodedPassword = getPasswordEncoder().encode(newPassword);
			user.setPassword(encodedPassword);
		}

		// Only the admin user can update the user role (reusing currentUser from auth check):
		String updateRole = user.getTmpRoleDescription();

		// update role if it is changed:
		if (!TreebaseUtil.isEmpty(updateRole) && !updateRole.equals(user.getRoleDescription())) {
			if (currentUser.getRole().isAdmin()) {

				if (!TreebaseUtil.isEmpty(updateRole)) {
					UserRole newRole = getUserService().findUserRole(updateRole);

					if (newRole != null) {
						user.setRole(newRole);
					}
				}
			}
		}

		getUserService().update(user);

		return setAttributeAndShowForm(
			request,
			response,
			bindExp,
			"messages",
			getMessageSourceAccessor().getMessage("user.profile.updated"));

	}

	/**
	 * Retrieve authenticated user, and looked up User object
	 * 
	 * Creation date: May 1, 2006 3:42:52 PM
	 * 
	 * Users can change their own profile.
	 * 
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		String username = request.getRemoteUser();
		User user;

		if (username != null) {
			user = getUserService().findUserByName(username);
		} else {
			user = new User();
		}
		return user;
	}
}
