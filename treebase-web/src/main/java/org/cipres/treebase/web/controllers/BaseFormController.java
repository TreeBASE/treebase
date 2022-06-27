
package org.cipres.treebase.web.controllers;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cipres.treebase.NamespacedGUID;
import org.cipres.treebase.TreebaseIDString;
import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.ControllerUtil;
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
	private static final Logger LOGGER = LogManager.getLogger(BaseFormController.class);

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
		request.setAttribute("messages", messages);
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
		LOGGER.info("Checking whether explicit access is granted, value="+mAuthorizationChecked);
		return mAuthorizationChecked;
	}

	/**
	 * 
	 * @param pAuthorizationChecked
	 */
	protected void setAuthorizationChecked(boolean pAuthorizationChecked) {
		mAuthorizationChecked = pAuthorizationChecked;
	}
	
	/**
	 * 
	 * @param pRequest
	 * @param pModelAndView
	 * @return
	 */
	protected ModelAndView getConditionalModelAndView(
		HttpServletRequest pRequest,
		ModelAndView pModelAndView) {
		if ( ControllerUtil.isReviewerAccessGranted(pRequest) || isAuthorizationChecked() ) {
			return pModelAndView;
		} else {
			return new ModelAndView(AUTHORIZATION_VIOLATION_VIEW);
		}		
	}

	@Override
	protected ModelAndView showForm(
		HttpServletRequest pRequest,
		HttpServletResponse pResponse,
		BindException pBindException,
		Map pMap) throws Exception {
		if ( isAuthorizationChecked() ) {
			LOGGER.info("returning supplied ModelAndView because access was granted explicitly");
			return super.showForm(pRequest, pResponse, pBindException, pMap);
		}
		if (ControllerUtil.isReviewerAccessGranted(pRequest)) {
			LOGGER.info("returning supplied ModelAndView because reviewer access was granted");
			return super.showForm(pRequest, pResponse, pBindException, pMap);
		} else {
			LOGGER.info("returning AUTHORIZATION_VIOLATION_VIEW");
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
