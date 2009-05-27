/*
 * CIPRES Copyright (c) 2005- 20078, The Regents of the University of California All rights reserved.
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
