package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.admin.PersonHome;
import org.cipres.treebase.web.Constants;

/**
 * AdminSelectPersonsController.java
 * 
 * Created on Aug 21, 2008
 * @author Jin Ruan
 */
public class AdminSelectPersonsController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(AdminSelectPersonsController.class);

	private PersonHome mPersonHome;

	
	/**
	 * Constructor.
	 */
	public AdminSelectPersonsController() {
		super();
	}

	//Set<Matrix> findMatrices(Collection<TaxonLabel> pTaxonLabels) { return null;}
	
	//Set<Matrix> findMatrices(Collection<TaxonVariant> pTaxonVariants) {return null;} 

	/**
	 * Return the PersonHome field.
	 * 
	 * @return PersonHome mPersonHome
	 */
	public PersonHome getPersonHome() {
		return mPersonHome;
	}

	/**
	 * Set the PersonHome field.
	 */
	public void setPersonHome(PersonHome pNewPersonHome) {
		mPersonHome = pNewPersonHome;
	}
	
	/**
	 * 
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		String userchoice = getMessageSourceAccessor().getMessage("user.management.userchoice").trim();
		String requestUserChoice = request.getParameter(userchoice).trim();

		List<Person> persons = new ArrayList<Person>();		
			
		if (requestUserChoice.startsWith("firstAndLast")
			&& (persons = getPersonHome().findDuplicateWithFirstAndLastNames()).size() == 0 ) {
			return setAttributeAndShowForm(
				request, 
				response, 
				bindExp, 
				"errors", 
				"No duplicates with same first and last name found");
		} else if (requestUserChoice.startsWith("lastOnly")
			&& (persons = getPersonHome().findDuplicateWithLastNames()).size() == 0 ) {
			return setAttributeAndShowForm(
				request,
				response,
				bindExp,
				"errors",
				"No duplicates with same last name found");
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("USER Choice: " + requestUserChoice);
		}

		request.getSession().setAttribute(Constants.PERSON_LIST, persons);
		//request.getSession().setAttribute(userchoice.toUpperCase(), USERCHOICE);
		//request.getSession().setAttribute(studytype.toUpperCase(), requestStudyType);
		request.getSession().setAttribute("ADMIN_COMING_FROM", "Management");

		String theView = getSuccessView();
		return new ModelAndView(theView);

	}

	/**
	 * 
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		return new Object();
	}

}
