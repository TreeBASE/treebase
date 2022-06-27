
package org.cipres.treebase.web.controllers;

import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;

public class AdminDeletingUserStepTwoController extends AbstractUserController {

	private static Logger LOGGER = LogManager.getLogger(AdminDeletingUserStepTwoController.class);

	private SubmissionService mSubmissionService;

	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		if (command instanceof String) {

		} else {
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"There is a problem with username.");
		}

		String pName = (String) command;
		
		//TODO: move following logic to userservice.deleteUser() !!
		User pUser = getUserService().findUserByName(pName);
		Iterator<Submission> submissionIterator = pUser.getSubmissionsCopy().iterator();

		int submissioncount = pUser.getSubmissionCount();
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("NUMBER OF SUBMISSIONS: " + submissioncount);
		}

		while (submissionIterator.hasNext()) {
			Submission pSubmission = submissionIterator.next();
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Submission ID: " + pSubmission.getId());
			}
			getSubmissionService().deleteSubmission(pSubmission);

		}

		getUserService().deleteUser(pName);

		StringBuilder pMessage = new StringBuilder(pName + "'s account ");

		if (submissioncount > 0) {
			pMessage.append("and " + submissioncount).append(" Submission(s) have been deleted.");
		} else {
			pMessage.append("has been deleted.");
		}

		request.getSession().setAttribute("MESSAGE_TO_ADMINISTRATOR", pMessage.toString());

		return new ModelAndView("redirect:/admin/messageToAdminAfterAction.html");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		
		//first check whether the name is specified from url: (like from user list page)
		//
		String username = ServletRequestUtils.getStringParameter(request, "userid", null);

		if (username == null) {
			if (request.getSession().getAttribute("ADMIN_DELETING_USERNAME") != null) {
				username = (String) request.getSession().getAttribute("ADMIN_DELETING_USERNAME");
			}
		}

		if (username != null) {
			return username;
		}

		return new Object();
	}

	/**
	 * @return the submissionService
	 */
	public SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	/**
	 * @param pSubmissionService the submissionService to set
	 */
	public void setSubmissionService(SubmissionService pSubmissionService) {
		mSubmissionService = pSubmissionService;
	}

}
