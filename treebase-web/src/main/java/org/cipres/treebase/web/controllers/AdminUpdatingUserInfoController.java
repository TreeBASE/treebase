


package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author madhu
 * 
 * Created on January 7, 2008
 */
public class AdminUpdatingUserInfoController extends AdminAndUserGenericController {

	private static final Logger LOGGER = LogManager.getLogger(AdminUpdatingUserInfoController.class);

	@Override
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		String username = request.getParameter("username").trim();

		if (super.onSubmit(request, response, command, bindExp) != null) {
			return super.onSubmit(request, response, command, bindExp);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("USERNAME: " + username);
		}

		request.getSession().setAttribute("ADMIN_UPDATING_USERINFO_USERNAME", username);

		return new ModelAndView(getSuccessView());

	}

}
