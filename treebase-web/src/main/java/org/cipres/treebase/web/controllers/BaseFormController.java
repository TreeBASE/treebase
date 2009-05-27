/*
 * Copyright 2005 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;

/**
 * BaseFormController
 * 
 * Created on November 2, 2005
 * 
 * @author lcchan
 * 
 */
public abstract class BaseFormController extends CancellableFormController {
	private static final Logger LOGGER = Logger.getLogger(BaseFormController.class);

	// Constants:
	protected static final String ACTION_DELETE = "Delete";
	protected static final String ACTION_UPDATE = "Update";
	protected static final String ACTION_SUBMIT = "Submit";
	protected static final String ACTION_INSERT = "Insert";
	protected static final String ACTION_Validate = "Validate";
	// match spring callableFormController default
	protected static final String ACTION_CANCEL = "_cancel";

	private static final String AUTHORIZATION_VIOLATION_VIEW = "redirect:/accessviolation.html";

	private String mAuthorizationViolationView;
	private boolean mAuthorizationChecked = true;

	/**
	 * Custom property editor for converting form inputs to specified objects
	 * 
	 * Creation date: May 31, 2006 3:37:41 PM
	 */
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {

		NumberFormat nf = NumberFormat.getNumberInstance();

		// convert java.lang.Integer
		binder.registerCustomEditor(Integer.class, null, new CustomNumberEditor(
			Integer.class,
			nf,
			true));

		// convert java.util.Date
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));

		// convert java.lang.Integer
		// Note: setting null in CustomNumberEditor means no format is
		// used in print an Integer (Otherwise, if NumberFormat is
		// used, a comma will be inserted in the number for Integer > 1000
		binder.registerCustomEditor(Integer.class, null, new CustomNumberEditor(
			Integer.class,
			null,
			true));

		// convert java.lang.Double
		binder.registerCustomEditor(Double.class, null, new CustomNumberEditor(
			Double.class,
			nf,
			true));

		// convert java.lang.Long
		binder.registerCustomEditor(Long.class, null, new CustomNumberEditor(Long.class, nf, true));

		// files
		binder.registerCustomEditor(byte[].class, null, new ByteArrayMultipartFileEditor());
	}

	/**
	 * Add message to the "messages" attribute in the request scope so we can use it in JSP pages
	 * 
	 * Creation date: May 8, 2006 6:51:15 PM
	 */
	public void saveMessage(HttpServletRequest request, String msg) {

		List messages = (List) request.getAttribute("messages");

		if (messages == null) {
			messages = new ArrayList();
		}
		messages.add(msg);
		request.setAttribute("messsages", messages);
	}

	/**
	 * 
	 * @return
	 */
	private String getAuthorizationViolationView() {
		return mAuthorizationViolationView;
	}

	/**
	 * Set the view when there is a access violation.
	 * 
	 * @param pAuthorizationViolationView
	 */
	public void setAuthorizationViolationView(String pAuthorizationViolationView) {
		mAuthorizationViolationView = pAuthorizationViolationView;
	}

	/**
	 * 
	 * @return
	 */
	private boolean isAuthorizationChecked() {
		return mAuthorizationChecked;
	}

	/**
	 * 
	 * @param pAuthorizationChecked
	 */
	protected void setAuthorizationChecked(boolean pAuthorizationChecked) {
		mAuthorizationChecked = pAuthorizationChecked;
	}

	@Override
	protected ModelAndView showForm(
		HttpServletRequest pArg0,
		HttpServletResponse pArg1,
		BindException pArg2,
		Map pArg3) throws Exception {
		if (isAuthorizationChecked()) {
			return super.showForm(pArg0, pArg1, pArg2, pArg3);
		} else {
			return new ModelAndView(AUTHORIZATION_VIOLATION_VIEW);
		}
	}

	protected ModelAndView setAttributeAndShowForm(
		HttpServletRequest request,
		HttpServletResponse response,
		BindException bExp,
		String attributeType,
		String infoForUser) throws Exception {

		request.setAttribute(attributeType, infoForUser);
		return showForm(request, response, bExp);
	}

}
