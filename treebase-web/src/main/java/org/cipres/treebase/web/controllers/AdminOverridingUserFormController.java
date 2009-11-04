
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
