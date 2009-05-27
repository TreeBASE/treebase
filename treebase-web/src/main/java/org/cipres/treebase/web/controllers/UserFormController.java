/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
 * 
 * Permission to use, copy, modify, and distribute this software and its documentation for
 * educational, research and non-profit purposes, without fee, and without a written agreement is
 * hereby granted, provided that the above copyright notice, this paragraph and the following two
 * paragraphs appear in all copies.
 * 
 * Permission to incorporate this software into commercial products may be obtained by contacting
 * us: http://www.phylo.org/contactUs.html
 * 
 * The software program and documentation are supplied "as is". In no event shall the CIPRES project
 * be liable to any party for direct, indirect, special, incidental, or consequential damages,
 * including lost profits, arising out of the use of this software and its documentation, even if
 * the CIPRES project has been advised of the possibility of such damage. The CIPRES project
 * specifically disclaims any warranties, including, but not limited to, the implied warranties of
 * merchantability and fitness for a particular purpose. The CIPRES project has no obligations to
 * provide maintenance, support, updates, enhancements, or modifications.
 */

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
