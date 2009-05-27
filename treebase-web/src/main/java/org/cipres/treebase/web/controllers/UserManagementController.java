/*
 * CIPRES Copyright (c) 2005- 2007, The Regents of the University of California All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. *
 * Neither the name of the University of California or the San Diego Supercomputer Center nor the
 * names of its contributors may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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

/**
 * @author Madhu, created on December, 2007
 * 
 * Modified on March, 2008, to incorporate extra options.
 */
public class UserManagementController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(UserManagementController.class);

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
	

	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		String userinfo = getMessageSourceAccessor().getMessage("user.management.userinfo").trim();
		String requestUserInfo = request.getParameter(userinfo).trim();

		String userchoice = getMessageSourceAccessor()
			.getMessage("user.management.userchoice").trim();
		String requestUserChoice = request.getParameter(userchoice).trim();

		String studytype = getMessageSourceAccessor()
			.getMessage("user.management.studytype").trim();
		String requestStudyType = request.getParameter(studytype).trim();

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
		} else if (requestUserChoice.startsWith("Username")
			&& (users = getUserHome().findByUserNameLike(requestUserInfo)).size() == 0) {
			return setAttributeAndShowForm(request, response, bindExp, "errors", "Username: '"
				+ requestUserInfo + "' does not exist.");
		} else if (requestUserChoice.startsWith("Email")
			&& (users = getUserHome().findByEmail(requestUserInfo, true)).size() == 0) {
			//Note: search by email is exact search.
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"User with Email address: '" + requestUserInfo + "' does not exist.");
		} else if (requestUserChoice.startsWith("Last")
			&& (users = getUserHome().findUserByLastName(requestUserInfo)).size() == 0) {
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"User with last name: '" + requestUserInfo + "' does not exist.");
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("USER Choice: " + requestUserChoice);
			LOGGER.debug("STUDYTYPE: " + requestStudyType);
		}

		request.getSession().setAttribute(userinfo, users);
		//request.getSession().setAttribute(userchoice.toUpperCase(), USERCHOICE);
		request.getSession().setAttribute(studytype.toUpperCase(), requestStudyType);
		request.getSession().setAttribute("ADMIN_COMING_FROM", "Management");

		return new ModelAndView(getSuccessView());

	}

	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		return new Object();
	}

}
