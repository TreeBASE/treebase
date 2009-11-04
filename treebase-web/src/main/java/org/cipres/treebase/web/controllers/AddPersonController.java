package org.cipres.treebase.web.controllers;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.admin.PersonHome;
import org.cipres.treebase.domain.admin.PersonService;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.CitationService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * @author Madhu
 * 
 */
public abstract class AddPersonController extends BaseFormController {
	private static final Logger LOGGER = Logger.getLogger(AddPersonController.class);

	private StudyService mStudyService;
	private CitationService mCitationService;
	private PersonService mPersonService;
	private PersonHome mPersonHome;

	public AddPersonController() {
	// need a session to hold the formBackingObject
	// setSessionForm(true);
	// setSynchronizeOnSession(true);

	}

	/**
	 * Return the PersonService field.
	 * 
	 * @return PersonService mPersonService
	 */
	public PersonService getPersonService() {
		return mPersonService;
	}

	/**
	 * Set the PersonService field.
	 */
	public void setPersonService(PersonService pNewPersonService) {
		mPersonService = pNewPersonService;
	}

	/**
	 * Return the CitationService field.
	 * 
	 * @return CitationService mCitationService
	 */
	public CitationService getCitationService() {
		return mCitationService;
	}

	/**
	 * Set the CitationService field.
	 */
	public void setCitationService(CitationService pNewCitationService) {
		mCitationService = pNewCitationService;
	}

	/**
	 * Return the StudyService field.
	 * 
	 * @return StudyService mStudyService
	 */
	public StudyService getStudyService() {
		return mStudyService;
	}

	/**
	 * Set the StudyService field.
	 */
	public void setStudyService(StudyService pNewStudyService) {
		mStudyService = pNewStudyService;
	}

	/**
	 * Treat all the "POST" as submission, but also the "GET" if the query string is not null (for
	 * the "Delete" link)
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#isFormSubmission(javax.servlet.http.HttpServletRequest)
	 */

	protected boolean isFormSubmission(HttpServletRequest request) {
		return ((request.getMethod().equals("GET") && (request.getQueryString() != null)) || (request
			.getMethod().equals("POST")));
	}

	protected ModelAndView handleInvalidSubmit(
		HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Inside Handle Invalid Submit");
		}
		BindException errors = new BindException(formBackingObject(request), getCommandName());
		errors.reject("duplicateFormSubmission", "Duplicate form submission");
		return showForm(request, response, errors);

	}

	/*
	 * processFormSubmissionMethod is the first one to be called upon the form submission by
	 * default. Here, it is being overridden because I want the the validator to intercept only when
	 * "Submit" or "Submit and Continue" buttons are pressed. That is achieved by calling
	 * super.processFormSubmission else validation is bypassed. processCancel is called by
	 * super.processFormSubmission when "Cancel" button is pressed. (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.CancellableFormController#processFormSubmission(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView processFormSubmission(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) throws Exception {

		if (request.getParameter(ACTION_SUBMIT) == null) {
			return onSubmit(request, response, command, errors);
		} else {

			LOGGER.info("FORM ATTRIBUTE: "
				+ request.getSession().getAttribute(getFormSessionAttributeName()));

			return super.processFormSubmission(request, response, command, errors);
		}
	}

	/**
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return cancelView
	 */
	protected ModelAndView processCancel(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("++++++++++++++++ processCancel is called.");
		}
		return new ModelAndView(getCancelView());
	}

	/**
	 * 
	 * The API right now supports only a list of authors/editors, so we have to update the list
	 * instead of individual authors/editors.
	 * 
	 * Delete: remove associate of person from citation, but the person is NOT deleted from the
	 * database.
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @return ModemAndView
	 */
	protected ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException bindExp) throws Exception {

		Person author_editor = (Person) command;

		/* Get the existing list of authors for the citation */
		Study study = ControllerUtil.findStudy(request, mStudyService);
		Citation citation = study.getCitation();

		if (citation == null) {
			request.getSession().setAttribute(
				"errors",
				"Please provide the citation for this study first.");
			return showForm(request, response, bindExp);
		}

		// list of original author/editors associated with the citation
		List<Person> currentPersons = getPeople(citation);// citation.getAuthors();

		if (request.getParameter(ACTION_CANCEL) == null) {
			/*
			 * update the author list based the user selection
			 */
			if (request.getParameter(ACTION_SUBMIT) != null
				|| request.getParameter("Submit and Continue") != null) {
				/*
				 * look for match in "Person" table
				 */
				Person p = mPersonService.findByExactMatch(author_editor);
				if (p == null) { // no match
					mPersonService.createPerson(author_editor); // add "author" to Person table
					currentPersons.add(author_editor); // add "author" to citation_author table
				} else { // match found
					currentPersons.add(p); // just add the found "author" to citation_author table
				}

			}
			// Update list in the database

			setPeople(citation, currentPersons);// citation.setAuthors(people);
			mCitationService.update(citation); // update the author list in database

		}

		return new ModelAndView(getSuccessView());
	}

	/**
	 * Retrieve the list of existing authors/editors for the citation and provide a brand new form
	 * each time.
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) {

		Collection<Person> people = null;

		String lastName = (String) request.getSession().getAttribute("PERSONLASTNAME");
		people = getPersonHome().findByLastName(lastName);

		request.getSession().setAttribute("peopleMatchList", people);
		setSessionVariable(request);

		/*
		 * Always provide the user a new form
		 */

		return new Person();
	}

	abstract protected List<Person> getPeople(Citation pCitation);

	abstract protected void setPeople(Citation pCitation, List<Person> people);

	abstract protected void setSessionVariable(HttpServletRequest request);

	abstract protected String provideDeleteMessageParameter();

	abstract protected String provideAddMessageParameter();

	/**
	 * @return the personHome
	 */
	public PersonHome getPersonHome() {
		return mPersonHome;
	}

	/**
	 * @param pPersonHome the personHome to set
	 */
	public void setPersonHome(PersonHome pPersonHome) {
		mPersonHome = pPersonHome;
	}
}
