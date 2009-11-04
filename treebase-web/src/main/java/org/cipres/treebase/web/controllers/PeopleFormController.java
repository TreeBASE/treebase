
package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.admin.Person;
import org.cipres.treebase.domain.admin.PersonService;
import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.CitationService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * PeopleController.java Created on June 7, 2006
 * 
 * @author lcchan
 * @author Madhu, Modified on February, 2008, for Validating case for author name. In addition,
 *         server side validation is not turned on by pressing 'up' and 'down' buttons. Author list
 *         is updated on the page and the change is persistent when 'up' and 'down' buttons are
 *         clicked.
 */
public abstract class PeopleFormController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(AuthorFormController.class);

	private StudyService mStudyService;
	private CitationService mCitationService;
	private PersonService mPersonService;

	public PeopleFormController() {
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
		// return disallowDuplicateFormSubmission(request, response);
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

		if (request.getParameter(ACTION_SUBMIT) == null
			&& request.getParameter("Submit and Continue") == null) {
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
	 * Returns the appropriate ModelAndView, depending on runtime
	 * instance (e.g. EditorFormController, AuthorFormController)
	 * 
	 * @return ModelAndView, depending on runtime instance
	 */
	private ModelAndView returnView () {
		if ( this instanceof EditorFormController ) {
			return new ModelAndView("redirect:/user/editorForm.html");
		}
		else {
			return new ModelAndView("redirect:/user/authorForm.html");
		}		
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

		// modified ids from user.
		String idsList = request.getParameter("authorIds");

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Value of the hidden field: " + idsList);
		}

		if (request.getParameter(ACTION_CANCEL) != null) {

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("++++++++++++++++ CANCEL is called");
			}
			return new ModelAndView(getCancelView());
		}
		/*
		 * --IMPORTANT NOTE-- showNewForm method is like clicking on the link a fresh. It presents
		 * the new form and in the process formBackingObject method is called. showForm method does
		 * not call formBackingObject.
		 */

		if (!TreebaseUtil.isEmpty(idsList)) {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Here is the idsList: " + idsList);
			}
			rearrangeList(idsList, request);
			return returnView();
		}

		Person author_editor = (Person) command;

		if (request.getParameter("method") == null) {

			List<String> errorsEncountered = verifyEachParameter(author_editor);
			if (errorsEncountered.size() > 0) {
				errorsEncountered.add(
					0,
					"Sorry, form could not be submissitted for reasons see below.");
				request.getSession().setAttribute("errors", errorsEncountered);
				return returnView();
			}

			author_editor.setFirstName(author_editor.getFirstName().trim());
			author_editor.setLastName(author_editor.getLastName().trim());

			if (author_editor.getMiddleName().trim().length() > 0) {
				author_editor.setMiddleName(author_editor.getMiddleName().trim());
			}
		}

		// Get the existing list of authors for the citation
		Study study = ControllerUtil.findStudy(request, mStudyService);
		Citation citation = study.getCitation();

		if (citation == null) {
			request.getSession().setAttribute(
				"errors",
				"Please provide the citataion for this study first.");
			return showForm(request, response, bindExp);
		}

		// list of original author/editors associated with the citation
		List<Person> currentPersons = getPeople(citation);

		// Check permission that the user has the edit right:
		// Currently it is not necessary since the only delete link can be hacked by direct editing
		// the url.
		// but the delete can only work with this particular study.
		TBPermission perm = getStudyService().getPermission(request.getRemoteUser(), study.getId());
		if (perm != TBPermission.WRITE) {
			// Give error message and return
			// return;
		}

		if (request.getParameter(ACTION_CANCEL) == null) {
			// update the author list based the user selection
			if (request.getParameter("method") != null
				&& request.getParameter("method").equals(ACTION_DELETE)) {
				String id = request.getParameter("id");
				for (Person person : currentPersons) {
					if (person.getId().equals(Long.parseLong((id)))) {
						currentPersons.remove(person);
						break;
					}
				}
			} 
			else if (request.getParameter(ACTION_SUBMIT) != null
				|| request.getParameter("Submit and Continue") != null) {
				 // look for match in "Person" table
				Person p = mPersonService.findByExactMatch(author_editor);
				// no match
				if (p == null) { 
					// add "author" to Person table
					mPersonService.createPerson(author_editor);
					// add "author" to citation_author table
					currentPersons.add(author_editor); 
				} 
				// match found
				else { 
					// just add the found "author" to citation_author table
					currentPersons.add(p); 
				}

			}

			setPeople(citation, currentPersons);
			mCitationService.update(citation); // update the author list in database
			
			/* populate feedback messages for delete resp. add */
			if (request.getParameter("method") != null) {
				request.getSession().setAttribute(
					"messages",
					getMessageSourceAccessor().getMessage(provideDeleteMessageParameter()));
			} 
			else if (request.getParameter(ACTION_SUBMIT) != null) {
				request.getSession().setAttribute(
					"messages",
					getMessageSourceAccessor().getMessage(provideAddMessageParameter()));
			}
			
			/* determine which view to return */
			return returnView();
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
		/*
		 * Retrieve the list of authors to show
		 */
		// Long submission_id = ControllerUtil.getSubmissionId(request);
		// Study study = mStudyService.findBySubmissionID(submission_id);
		Study study = ControllerUtil.findStudy(request, mStudyService);
		List<Person> people = null;
		if (study != null) {
			Citation citation = study.getCitation();

			if (citation != null) people = getPeople(citation);// study.getAuthors();
		}

		request.getSession().setAttribute(Constants.PEOPLE_LIST, people);
		setSessionVariable(request);

		/*
		 * Always provide the user a new form
		 */

		return new Person();
	}

	/**
	 * @param aPersonIdsList
	 * @param request
	 * 
	 * This method rearranges the authors/editors list based on the new ids arrangement received
	 * from the action of 'up' & 'down' buttons on the form.
	 */
	private void rearrangeList(String aPersonIdsList, HttpServletRequest request) {

		Study study = ControllerUtil.findStudy(request, mStudyService);

		Citation citation = study.getCitation();
		List<Person> oldList = (List<Person>) getPeople(citation);

		if (LOGGER.isDebugEnabled()) {
			LOGGER
				.debug("rearrangeList(String, HttpServletRequest) - Total Elements in the Old List:" + oldList.size()); //$NON-NLS-1$
		}

		List<Person> newList = new ArrayList<Person>();
		String[] idValues = aPersonIdsList.split(",");

		for (String aId : idValues) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("rearrangeList(String, HttpServletRequest) - A ID: " + aId); //$NON-NLS-1$
			}
			for (Person person : oldList) {
				if (String.valueOf(person.getId()).equals(aId.trim())) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("rearrangeList(String, HttpServletRequest) - Found Match"); //$NON-NLS-1$
					}
					newList.add(person);
				}
			}
		}

		if (LOGGER.isDebugEnabled()) {
			for (Person person : newList) {
				LOGGER.debug(" New ids: " + person.getId()); //$NON-NLS-1$
			}
		}

		setPeople(citation, newList);
		getCitationService().update(citation);
	}

	/**
	 * @param pPerson
	 * @return List of complains encountered
	 */
	private List<String> verifyEachParameter(Person pPerson) {

		List<String> errorsList = new ArrayList<String>();

		String firstName = pPerson.getFirstName().trim();
		String lastName = pPerson.getLastName().trim();
		String middleName = pPerson.getMiddleName().trim();

		checkParameterCase("First Name", firstName, errorsList);
		checkParameterCase("Middle Name", middleName, errorsList);
		checkParameterCase("Last Name", lastName, errorsList);

		return errorsList;
	}

	/**
	 * @param pNameType
	 * @param pName
	 * @param aErrorsList
	 */
	private void checkParameterCase(String pNameType, String pName, List<String> aErrorsList) {
		if (pName.length() > 1) {
			if (pName.equals(pName.toLowerCase()) || pName.equals(pName.toUpperCase())) {
				aErrorsList.add("All characters in " + pNameType + " '" + pName
					+ "' cannot be of same case.");
			}
		}
	}

	abstract protected List<Person> getPeople(Citation pCitation);

	abstract protected void setPeople(Citation pCitation, List<Person> people);

	abstract protected void setSessionVariable(HttpServletRequest request);

	abstract protected String provideDeleteMessageParameter();

	abstract protected String provideAddMessageParameter();
}
