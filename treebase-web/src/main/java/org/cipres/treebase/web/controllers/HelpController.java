
package org.cipres.treebase.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private static final Logger LOGGER = LogManager.getLogger(HelpController.class);
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
