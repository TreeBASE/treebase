package org.cipres.treebase.web.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Madhu, created on December, 2007
 * 
 * Modified on March, 2008, to incorporate extra options.
 */
public class SelectStudiesController extends BaseFormController {

	private static final Logger LOGGER = LogManager.getLogger(SelectStudiesController.class);

	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		String studytype = getMessageSourceAccessor()
			.getMessage("user.management.studytype").trim();
		String STUDYTYPE = request.getParameter(studytype).trim();

		if (LOGGER.isDebugEnabled()) {

			LOGGER.debug("STUDYTYPE: " + STUDYTYPE);
		}

		request.getSession().setAttribute(studytype.toUpperCase(), STUDYTYPE);
		request.getSession().setAttribute("ADMIN_COMING_FROM", "SelectStudies");

		return new ModelAndView(getSuccessView());

	}

	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		return new Object();
	}

}
