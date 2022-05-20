package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author madhu
 * 
 * Created on January 8th, 2008
 * 
 */
public class AdminDeletingUserStepOneController extends AdminAndUserGenericController {

	private static final Logger LOGGER = LogManager.getLogger(AdminDeletingUserStepOneController.class);

	@Override
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		String username = request.getParameter("username").trim();

		if (username.equals(request.getRemoteUser())) {
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"Self destruction is against the law.");
		}

		if (super.onSubmit(request, response, command, bindExp) != null) {
			return super.onSubmit(request, response, command, bindExp);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("USERNAME: " + username);
		}

		StringBuilder aMessage = new StringBuilder("Are you sure you want to delete the user '");
		aMessage.append(username).append("'?");

		request.getSession().setAttribute("ADMIN_TEST_CONDITION", aMessage.toString());
		request.getSession().setAttribute("ADMIN_DELETING_USERNAME", username);

		return new ModelAndView(getSuccessView());

	}

}
