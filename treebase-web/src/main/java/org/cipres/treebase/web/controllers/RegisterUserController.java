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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.framework.ExecutionResult;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.StringUtil;

/**
 * RegisterUserController.java
 * 
 * Controller to sign up for user-related functions
 * 
 * Created on May 1, 2006
 * 
 * @author lcchan
 * 
 */
public class RegisterUserController extends AbstractUserController {
	private static final Logger LOGGER = Logger.getLogger(RegisterUserController.class);

	/**
	 * 
	 * Creation date: May 8, 2006 5:18:08 PM
	 */
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering RegisterUserController:onsubmit()...");
		}
		User user = (User) command;

		if (!checkPasswords(request, user)) {
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"Passwords, you typed are not identical.");

		}

		if (request.getParameter(ACTION_SUBMIT) != null) {
			// TODO: need code to encrypt and decrypt password
			// user.setPassword(StringUtil.encodePassword(user.getPassword(),Constants.ENCRYPTION_ALGORITHM));
			StringUtil.encodePassword(user.getPassword(), Constants.ENCRYPTION_ALGORITHM);

			ExecutionResult execResult = getUserService().createUser(user);

			if (!execResult.isSuccessful()) {
				// possible reason: e.g. the username or the email address is already taken.

				return setAttributeAndShowForm(request, response, bindExp, "messages", execResult
					.getErrorMessage());

			}
		}
		String successView = getSuccessView();
		return new ModelAndView(successView);
	}
}