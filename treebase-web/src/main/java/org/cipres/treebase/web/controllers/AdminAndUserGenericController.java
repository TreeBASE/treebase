package org.cipres.treebase.web.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.admin.UserService;

/**
 * @author madhu
 * 
 * Created on January 8th, 2008
 * 
 */
public abstract class AdminAndUserGenericController extends BaseFormController {
	private static final Logger LOGGER = Logger.getLogger(AdminAndUserGenericController.class);
	private UserService mUserService;

	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindexp) throws Exception {

		String username = request.getParameter("username").trim();

		if (username.length() == 0) {
			return setAttributeAndShowForm(
				request,
				response,
				bindexp,
				"errors",
				"Please provide username!");
		} else if (getUserService().findUserByName(username) == null) {
			return setAttributeAndShowForm(request, response, bindexp, "errors", "Username: '"
				+ username + "' does not exist.");
		}

		return null;

	}

	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		return new Object();
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService() {
		return mUserService;
	}

	/**
	 * @param pUserService the userService to set
	 */
	public void setUserService(UserService pUserService) {
		mUserService = pUserService;
	}

}
