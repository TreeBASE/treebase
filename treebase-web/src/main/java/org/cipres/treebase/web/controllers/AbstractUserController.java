package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserHome;
import org.cipres.treebase.domain.admin.UserService;

/**
 * @author madhu
 * 
 */
public abstract class AbstractUserController extends BaseFormController {

	private UserService mUserService;
	private UserHome mUserHome;

	/**
	 * Return the UserHome field.
	 * 
	 * @return UserHome mUserHome
	 */
	public UserHome getUserHome() {
		return mUserHome;
	}

	/**
	 * Set the UserHome field.
	 */
	public void setUserHome(UserHome pNewUserHome) {
		mUserHome = pNewUserHome;
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

	/**
	 * 
	 * 
	 * @param request
	 * @param auser
	 * @return
	 */
	protected boolean checkPasswords(HttpServletRequest request, User auser) {

		return auser.getPassword().equals((String) request.getParameter("retypedpassword"));
	}

}
