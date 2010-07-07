package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import org.cipres.treebase.domain.admin.UserRole;

/**
 * @author madhu
 * 
 * created on November 15th, 2007
 * 
 */
public class ProcessUserController implements Controller {

	private static final Logger LOGGER = Logger.getLogger(ProcessUserController.class);

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		String importKey = (String)request.getSession().getAttribute("importKey");
		if (importKey != null && importKey.length()>0) {

			return new ModelAndView("redirect:/user/studyForm.html");
		}		
		if (request.isUserInRole(UserRole.ROLE_ADMIN) || request.isUserInRole(UserRole.ROLE_ASSO_EDITOR)) {

			return new ModelAndView("redirect:/admin/administrationPage.html");
		}
		return new ModelAndView("redirect:/user/submissionList.html");
	}
}
