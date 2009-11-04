package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserHome;
import org.cipres.treebase.web.Constants;

/**
 * AdminSelectUsersController.java
 * 
 * Created on Aug 21, 2008
 * @author Jin Ruan
 */
public class AdminSelectUsersController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(AdminSelectUsersController.class);

	private UserHome mUserHome;

	
	/**
	 * Constructor.
	 */
	public AdminSelectUsersController() {
		super();
	}

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
	 * 
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		String userchoice = getMessageSourceAccessor()
			.getMessage("user.management.userchoice").trim();
		String requestUserChoice = request.getParameter(userchoice).trim();

		String userinfo = getMessageSourceAccessor().getMessage("user.management.userinfo").trim();
		String requestUserInfo = request.getParameter(userinfo).trim();

		String userRoleKey = "User Role"; //"user.role";
		String requestUserRole = request.getParameter(userRoleKey);

		List<User> users = new ArrayList<User>();
		
		if (requestUserChoice.startsWith("Username") && requestUserInfo.length() == 0) {
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"Please provide username!");
		} else if (requestUserChoice.startsWith("Email") && requestUserInfo.length() == 0) {
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"Please provide email address!");
		} else if (requestUserChoice.startsWith("Last") && requestUserInfo.length() == 0) {
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"Please provide last name!");
		}
			
		else if (requestUserChoice.startsWith("Username")
			&& (users = getUserHome().findByUserNameLike(requestUserInfo)).size() == 0) {
			return setAttributeAndShowForm(request, response, bindExp, "errors", "Could not find matches for Username: '"
				+ requestUserInfo + "'.");
		} else if (requestUserChoice.startsWith("Email")
			&& (users = getUserHome().findByEmail(requestUserInfo, false)).size() == 0) {
			//Note: search by email is exact search.
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"Could not find matches for Email address: '" + requestUserInfo + "'.");
		} else if (requestUserChoice.startsWith("Last")
			&& (users = getUserHome().findUserByLastName(requestUserInfo)).size() == 0) {
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"Could not find matches with last name: '" + requestUserInfo + "'.");
		} else if (requestUserChoice.startsWith("User Role")) {
			users = getUserHome().findbyUserRole(requestUserRole);
			
			if (users.size() == 0) {
				return setAttributeAndShowForm(
					request,
					response,
					bindExp,
					"errors",
					"Could not find matches with user role: '" + requestUserRole + "'.");
			}
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("USER Choice: " + requestUserChoice);
			LOGGER.debug("user role: " + requestUserRole);
		}

		request.getSession().setAttribute(Constants.USER_LIST, users);
		//request.getSession().setAttribute(userchoice.toUpperCase(), USERCHOICE);
		//request.getSession().setAttribute(studytype.toUpperCase(), requestStudyType);
		request.getSession().setAttribute("ADMIN_COMING_FROM", "Management");

		return new ModelAndView(getSuccessView());

	}

	/**
	 * 
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		return new Object();
	}

}
