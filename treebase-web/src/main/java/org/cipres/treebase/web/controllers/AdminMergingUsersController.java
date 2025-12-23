package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.admin.UserService;

/**
 * @author madhu
 * 
 */
public class AdminMergingUsersController extends AbstractWizardFormController {

	private static final Logger LOGGER = LogManager.getLogger(AdminMergingUsersController.class);

	private UserService mUserService;
	private String mUserNameSource;
	private String mUserNameTarget;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView processFinish(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		boolean userproblem = false;

		List<String> problemList = new ArrayList<String>();

		if (TreebaseUtil.isEmpty(mUserNameSource)) {
			userproblem = true;
			problemList.add("Please provide Source User Name!");
		}
		if (TreebaseUtil.isEmpty(mUserNameTarget)) {
			userproblem = true;
			problemList.add("Please provide Target User Name!");
		}

		if (userproblem) {
			return setAttributeAndShowForm(request, response, bindExp, "errors", problemList);
		}

		if (getUserService().findUserByName(mUserNameSource) == null) {
			problemList.add("Source Username: '" + mUserNameSource + "' does not exist.");
			userproblem = true;
		}
		if (getUserService().findUserByName(mUserNameTarget) == null) {
			problemList.add("Target Username: '" + mUserNameTarget + "' does not exist.");
			userproblem = true;
		}

		if (userproblem) {
			return setAttributeAndShowForm(request, response, bindExp, "errors", problemList);
		}

		if (mUserNameSource.equals(mUserNameTarget)) {
			problemList.add("Two users cannot be identical.");
			userproblem = true;
		}
		if (getUserService().findUserByName(mUserNameSource).getSubmissionCount() == 0) {
			problemList.add("Source Username: '" + mUserNameSource + "' has Zero submissions.");
			userproblem = true;
		}

		if (userproblem) {
			return setAttributeAndShowForm(request, response, bindExp, "errors", problemList);
		}

		int movedSubmissionCount = getUserService().moveSubmissions(
			getUserNameSource(),
			getUserNameTarget());

		if (movedSubmissionCount <= 0) {
			return setAttributeAndShowForm(request, response, bindExp, "errors", problemList);
		}

		StringBuilder pMessage = new StringBuilder("" + movedSubmissionCount);
		pMessage.append(" Submissions have been moved from ").append(getUserNameSource());
		pMessage.append(" to ").append(getUserNameTarget()).append(".");

		request.getSession().setAttribute("MESSAGE_TO_ADMINISTRATOR", pMessage.toString());

		// TODO Auto-generated method stub
		return new ModelAndView("redirect:/admin/messageToAdminAfterAction.html");
	}

	@Override
	protected void postProcessPage(
		HttpServletRequest request,
		Object command,
		Errors errors,
		int page) throws Exception {

		if (page == 0) {
			// Two different ways of doing the same thing.
			setUserNameSource(ServletRequestUtils.getStringParameter(
				request,
				"sourceusername",
				null));
			setUserNameTarget(request.getParameter("targetusername"));
		}

	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) {
		return new Object();
	}

	/**
	 * @param request
	 * @param response
	 * @param bindExp
	 * @param attributeType
	 * @param problems
	 * @throws Exception
	 */
	private ModelAndView setAttributeAndShowForm(
		HttpServletRequest request,
		HttpServletResponse response,
		BindException bindExp,
		String attributeType,
		List<String> problems) throws Exception {

		request.setAttribute("errors", problems);
		return showForm(request, response, bindExp);
	}

	protected int getTargetPage(
		HttpServletRequest request,
		Object command,
		Errors errors,
		int currentPage) {

		int nextPage = 0;
		if (currentPage == 0) {
			StringBuilder aMessage = new StringBuilder(
				"Are you sure you want to move Submission from user '");
			aMessage
				.append(getUserNameSource()).append("' to '").append(getUserNameTarget()).append(
					"'.");

			request.getSession().setAttribute("ADMIN_TEST_CONDITION", aMessage.toString());
			nextPage = 1;
		}
		return nextPage;
	}

	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processCancel(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView processCancel(
		HttpServletRequest pRequest,
		HttpServletResponse pResponse,
		Object pCommand,
		BindException pErrors) throws Exception {
		return getNextView();
	}

	/**
	 * 
	 * @return
	 */
	private ModelAndView getNextView() {
		return new ModelAndView("redirect:/admin/administrationPage.html");
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService() {
		return mUserService;
	}

	/**
	 * @param pUserService the userService to set
	 */
	public void setUserService(UserService pUserService) {
		mUserService = pUserService;
	}

	/**
	 * @return the userNameA
	 */
	private String getUserNameSource() {
		return mUserNameSource;
	}

	/**
	 * @param pUserNameA the userNameA to set
	 */
	private void setUserNameSource(String pUserNameA) {
		mUserNameSource = pUserNameA.trim();
	}

	/**
	 * @return the userNameB
	 */
	private String getUserNameTarget() {
		return mUserNameTarget;
	}

	/**
	 * @param pUserNameB the userNameB to set
	 */
	private void setUserNameTarget(String pUserNameB) {
		mUserNameTarget = pUserNameB.trim();
	}
}
