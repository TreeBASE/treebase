
package org.cipres.treebase.web.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
	private static final Logger LOGGER = Logger.getLogger(UserFormController.class);

	public UserFormController() {
		super();

		setCancelView("redirect:/user/submissionList.html");
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

		if (!checkPasswords(request, user)) {
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"The passwords you typed are not identical.");
		}

		// Only the admin user can update the user role:
		String username = request.getRemoteUser();
		User currentUser = getUserService().findUserByName(username);
		String updateRole = user.getTmpRoleDescription();
		String uid = request.getParameter("id");
		user.setId(Long.parseLong(uid)); // XXX

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
