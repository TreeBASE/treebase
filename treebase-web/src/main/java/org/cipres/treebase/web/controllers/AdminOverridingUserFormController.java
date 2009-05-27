/*
 * Copyright 2008 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserRole;

/**
 * AdminOverridingUserFormController.java
 * 
 */
public class AdminOverridingUserFormController extends UserFormController {

	/**
	 * 
	 * 
	 * @see org.cipres.treebase.web.controllers.UserFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		User user = null;

		String username = null;
		String userId = ServletRequestUtils.getStringParameter(request, "id", null);

		if (!TreebaseUtil.isEmpty(userId)) {
			user = getUserHome().findPersistedObjectByID(User.class, Long.valueOf(userId));
		}

		if (user == null) {
			if (request.getSession().getAttribute("ADMIN_UPDATING_USERINFO_USERNAME") != null) {
				username = (String) request.getSession().getAttribute(
					"ADMIN_UPDATING_USERINFO_USERNAME");
			}

			if (username != null) {
				user = getUserService().findUserByName(username);
				if (user == null) {
					user = getUserService().findUserByName(request.getRemoteUser());
				}
			} else {
				user = getUserService().findUserByName(request.getRemoteUser());
			}
		}

		if (user == null) {
			user = new User();
		}
		return user;
	}

	/**
	 * make user role list.
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {

		Map<String, Object> dataMap = super.referenceData(request);
		if (dataMap == null) {
			dataMap = new HashMap<String, Object>();
		}

		String username = request.getRemoteUser();
		User currentUser = getUserService().findUserByName(username);
		if (currentUser.getRole().isAdmin()) {
			// drop down list for user role list
			List<String> allRoles = UserRole.allUserRoleList();
			dataMap.put("allRoles", allRoles);
		} else {
			dataMap.put("allRoles", "");
		}

		return dataMap;
	}

}
