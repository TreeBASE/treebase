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
import org.cipres.treebase.domain.admin.Help;
import org.cipres.treebase.domain.admin.HelpService;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserRole;
import org.cipres.treebase.domain.admin.UserService;
import org.cipres.treebase.web.model.HelpControllerAction;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * StudySearchController.java
 * 
 * Created on November 17, 2008
 * 
 * @author mjd 20081117
 * 
 */
public class HelpController extends BaseFormController {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(HelpController.class);
	private HelpService helpService;
	private UserService userService;
	String failureView;

	public HelpService getHelpService() {
		return helpService;
	}

	public void setHelpService(HelpService helpService) {
		this.helpService = helpService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) {
		if (! isAdmin(request)) {
			throw new Error("No longer an admin??");
//			return new ModelAndView(getFailureView());		
		}
			
		HelpControllerAction fbo = (HelpControllerAction) command;
		String helpTag = request.getParameter("helpTag");
		String newText = request.getParameter("newHelpText");
		
		Help newHelp = getHelpService().findByTag(helpTag);
		if (newHelp == null) {
			newHelp = getHelpService().createHelp(helpTag);
		}

		fbo.setHelp(newHelp);
	
		LOGGER.debug("Setting new help text and saving object");
		getHelpService().save(newHelp);
		newHelp.setHelpText(newText);
		getHelpService().update(newHelp);
		
		return new ModelAndView(getSuccessView(), getCommandName(), fbo);
	}

	@Override
	protected HelpControllerAction formBackingObject(HttpServletRequest request)
			throws Exception {
		HelpControllerAction fbo = new HelpControllerAction(getHelpService());;
		String helpTag = request.getParameter("helpTag");
		
		if (helpTag != null) {
			fbo.setTag(helpTag);
		} else {
			fbo.setHelp(helpService.unknownHelp());
		}
		fbo.setAdmin(isAdmin(request));
			
		return fbo;
	}
	
	protected boolean isAdmin(HttpServletRequest request) {
        String username = request.getRemoteUser();
        User currentUser = getUserService().findUserByName(username);
        if (currentUser == null) return false;
        UserRole role = currentUser.getRole();
        if (role == null) return false;
        return role.isAdmin();
	}

	public String getFailureView() {
		return failureView;
	}

	public void setFailureView(String failureView) {
		this.failureView = failureView;
	}
}
