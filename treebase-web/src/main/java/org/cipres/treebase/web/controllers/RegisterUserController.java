
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