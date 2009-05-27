/*
 * CIPRES Copyright (c) 2005- 2008, The Regents of the University of California All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source code must retain the
 * above copyright notice, this list of conditions and the following disclaimer. * Redistributions
 * in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution. *
 * Neither the name of the University of California or the San Diego Supercomputer Center nor the
 * names of its contributors may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractWizardFormController;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.admin.PersonService;

/**
 * AdminMergingPersonsController.java
 * 
 * Created on Aug 28, 2008
 * @author Jin Ruan
 *
 */
public class AdminMergingPersonsController extends AbstractWizardFormController {

	private static final Logger LOGGER = Logger.getLogger(AdminMergingPersonsController.class);

	private PersonService mPersonService;
	private String mSourcePersonId;
	private String mTargetPersonId;

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

		if (TreebaseUtil.isEmpty(mSourcePersonId)) {
			userproblem = true;
			problemList.add("Please provide Source person record id!");
		}
		if (TreebaseUtil.isEmpty(mTargetPersonId)) {
			userproblem = true;
			problemList.add("Please provide Target person record id!");
		}

		if (userproblem) {
			return setAttributeAndShowForm(request, response, bindExp, "errors", problemList);
		}

		Person srcPerson = getPersonService().findByID(Long.valueOf(mSourcePersonId));
		Person targetPerson = getPersonService().findByID(Long.valueOf(mTargetPersonId));
		
		if (srcPerson == null) {
			problemList.add("Source person id: '" + mSourcePersonId + "' does not exist.");
			userproblem = true;
		}
		if (targetPerson == null) {
			problemList.add("Target person id: '" + mTargetPersonId + "' does not exist.");
			userproblem = true;
		}

		if (userproblem) {
			return setAttributeAndShowForm(request, response, bindExp, "errors", problemList);
		}

		if (mSourcePersonId.equals(mTargetPersonId)) {
			problemList.add("Two person records cannot be identical.");
			userproblem = true;
		}

		if (userproblem) {
			return setAttributeAndShowForm(request, response, bindExp, "errors", problemList);
		}

		String srcPersonName = srcPerson.getFirstName() + " " + srcPerson.getLastName() + " (id:" + mSourcePersonId + ")";
		String targetPersonName = targetPerson.getFirstName() + " " + targetPerson.getLastName() + " (id:" + mTargetPersonId + ")";
		
		int movedCount = getPersonService().mergePerson(Long.valueOf(mSourcePersonId), Long.valueOf(mTargetPersonId));

		if (movedCount < 0) {
			return setAttributeAndShowForm(request, response, bindExp, "errors", problemList);
		}

		StringBuilder pMessage = new StringBuilder("" + movedCount);
		pMessage.append(" Submissions/author/editor records have been moved from ").append(srcPersonName);
		pMessage.append(" to ").append(targetPersonName).append(".");

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
			mSourcePersonId = 
			ServletRequestUtils.getStringParameter(
				request,
				"sourcepersonid",
				null);
			
			mTargetPersonId = request.getParameter("targetpersonid");
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
				"Are you sure you want to merge person record '");
			aMessage
				.append(mSourcePersonId).append("' to person record '").append(mTargetPersonId).append(
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
	 * @return the PersonService
	 */
	public PersonService getPersonService() {
		return mPersonService;
	}

	/**
	 * @param pPersonService the PersonService to set
	 */
	public void setPersonService(PersonService pPersonService) {
		mPersonService = pPersonService;
	}

}
