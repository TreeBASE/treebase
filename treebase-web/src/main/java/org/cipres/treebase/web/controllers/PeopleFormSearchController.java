


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
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.CitationService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * PeopleFormSearchController.java Created on March 6, 2008
 * 
 * @author lcchan
 * @author Madhu
 * 
 * It is a new incarnation of PeopleFormController, which is being split in two components in
 * current requirement.
 */
public abstract class PeopleFormSearchController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(PeopleFormSearchController.class);
	private StudyService mStudyService;
	private CitationService mCitationService;
	private PersonService mPersonService;

	public PeopleFormSearchController() {
		// need a session to hold the formBackingObject
		setSessionForm(true);
		setSynchronizeOnSession(true);
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

	/*
	 * protected ModelAndView handleInvalidSubmit( HttpServletRequest request, HttpServletResponse
	 * response) throws Exception { if (LOGGER.isInfoEnabled()) { LOGGER.info("Inside Handle Invalid
	 * Submit"); } BindException errors = new BindException(formBackingObject(request),
	 * getCommandName()); errors.reject("duplicateFormSubmission", "Duplicate form submission");
	 * System.out.println("HI++++++++++++2"); return showForm(request, response, errors); // return
	 * disallowDuplicateFormSubmission(request, response); }
	 */

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
			System.out.println("HI++++++++++++1");
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

		// modified ids from user.
		String idsList = request.getParameter("authorIds");

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Value of the hidden field: " + idsList);
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
			return new ModelAndView("redirect:/user/authorSearchForm.html");

		}

		/* Get the existing list of authors for the citation */
		Study study = ControllerUtil.findStudy(request, mStudyService);
		Citation citation = study.getCitation();

		if (citation == null) {
			request.getSession().setAttribute(
				"errors",
				"Please provide the citataion for this study first.");
			return showForm(request, response, bindExp);
		}

		// list of original author/editors associated with the citation
		List<Person> currentPersons = getPeople(citation);// citation.getAuthors();

		Person author_editor = (Person) command;

		if (request.getParameter("method") != null) {
			String id = request.getParameter("id");
			if (request.getParameter("method").equals(ACTION_DELETE)) {

				for (Person person : currentPersons) {
					if (person.getId().equals(Long.parseLong((id)))) {
						currentPersons.remove(person);
						break;
					}
				}
				request.getSession().setAttribute(
					"messages",
					getMessageSourceAccessor().getMessage(provideDeleteMessageParameter()));

			} else if (request.getParameter("method").equals(ACTION_INSERT)) {

				currentPersons.add(mPersonService.findByID(Long.parseLong(id)));
				request.getSession().setAttribute(
					"messages",
					getMessageSourceAccessor().getMessage(provideAddMessageParameter()));

			}
			setPeople(citation, currentPersons);// citation.setAuthors(people);
			mCitationService.update(citation); // update the author list in database
			return new ModelAndView("redirect:/user/authorSearchForm.html");

		} else if (request.getParameter(ACTION_SUBMIT) != null) {
			String lastName = author_editor.getLastName().trim();
			if (lastName.length() == 0) {
				request.getSession().setAttribute("messages", "Please provide last name!");
				return new ModelAndView("redirect:/user/authorSearchForm.html");
			} else {
				// Put the last name in session scope an control should go to another page
				request.getSession().setAttribute("PERSONLASTNAME", lastName);
				return new ModelAndView("redirect:/user/addAuthor.html");
			}
		}
		setPeople(citation, currentPersons);// citation.setAuthors(people);
		mCitationService.update(citation); // update the author list in database
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
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("IN FORM BACKING OBJECT");
		}
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
			LOGGER.debug("Total Elements in the Old List:" + oldList.size());
		}

		List<Person> newList = new ArrayList<Person>();
		String[] idValues = aPersonIdsList.split(",");

		for (String aId : idValues) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("A ID: " + aId);
			}
			for (Person person : oldList) {
				if (String.valueOf(person.getId()).equals(aId.trim())) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Found Match");
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

	abstract protected List<Person> getPeople(Citation pCitation);

	abstract protected void setPeople(Citation pCitation, List<Person> people);

	abstract protected void setSessionVariable(HttpServletRequest request);

	abstract protected String provideDeleteMessageParameter();

	abstract protected String provideAddMessageParameter();
}
