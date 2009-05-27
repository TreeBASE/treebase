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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author madhu
 * 
 * Created on January 7, 2008
 */
public class AdminUpdatingUserInfoController extends AdminAndUserGenericController {

	private static final Logger LOGGER = Logger.getLogger(AdminUpdatingUserInfoController.class);

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
