
package org.cipres.treebase.web.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserService;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.CitationService;
import org.cipres.treebase.domain.study.CitationStatus;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * CitationFormController.java
 * 
 * Created on May 15, 2006
 * 
 * @author lcchan
 * @author Madhu
 * 
 * Modified on December 13, 2007; now, DOI, pubmed Id, and URL are validated to reasonable extent.
 */
public class CitationFormController extends BaseFormController {
	private static final Logger LOGGER = LogManager.getLogger(CitationFormController.class);

	private UserService mUserService;
	private SubmissionService mSubmissionService;
	private StudyService mStudyService;
	private CitationService mCitationService;

	/**
	 * retrieve citation object from study object which has been stored in the session when the user
	 * create a new submission or update a submission
	 * 
	 * Creation date: May 15, 2006 5:17:45 PM
	 */
	protected Object formBackingObject(HttpServletRequest request)
		throws ServletException,
			Exception {

		String selectedCitationType = request.getParameter("citationType");

		// Map<String, Object> studyMap = (Map)
		// request.getSession().getAttribute(Constants.STUDY_MAP);

		Citation c = null;
		Study study = ControllerUtil.findStudy(request, getStudyService());
		// if (studyMap != null) {

		// map Citation object data to CitationCommand object
		c = study.getCitation();
		// }

		if (c == null) {
			// citation has not been created yet
			// create a default citaiton:
			c = getCitationService().createCitation(selectedCitationType);

			// set default for URL
			c.setURL("http://");

			study.setCitation(c);
			c.setStudy(study);
		} else if (selectedCitationType != null
			&& !c.getCitationType().equalsIgnoreCase(selectedCitationType)) {

			// morph the citation to the selected type:
			c.setCitationType(selectedCitationType);
			c = Citation.factory(c);

			study.setCitation(c);
			c.setStudy(study);
		}

		// if (citation != null) {
		// BeanUtils.copyProperties(citationCommand, citation);
		// }
		// String citationType = ArticleCitation.CITATION_TYPE_ARTICLE;
		//
		// if (citation instanceof BookCitation) {
		// if (citation instanceof InBookCitation) {
		// citationType = InBookCitation.CITATION_TYPE_BOOKSECTION;
		// } else {
		// citationType = BookCitation.CITATION_TYPE_BOOK;
		// }
		// } else if (citation instanceof ArticleCitation) {
		// citationType = ArticleCitation.CITATION_TYPE_ARTICLE;
		// } else if (citation instanceof InBookCitation) {
		// citationType = InBookCitation.CITATION_TYPE_BOOKSECTION;
		// }
		//
		// citationCommand.setMCitationType(citationType);
		// citationCommand.setCitationMap(citationType, citation);
		return c;
	}

	/**
	 * Return the CitationService field.
	 * 
	 * @return CitationService mCitationService
	 */
	private CitationService getCitationService() {
		return mCitationService;
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
	 * Return the SubmissionService field.
	 * 
	 * @return SubmissionService mSubmissionService
	 */
	public SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	/**
	 * Return the UserService field.
	 * 
	 * @return UserService mUserService
	 */
	public UserService getUserService() {
		return mUserService;
	}

	/**
	 * It's not a real submit unless "Submit" is entered (javascript triggered submit())
	 * 
	 * Creation date: June 5, 2006 12:25:28 PM
	 */
	public boolean isFormChangeRequest(HttpServletRequest request) {
		// TODO: is there a way to copy existing citaitonCommand object for
		// "Update"
		// if (request.getParameter("citationId") != null) {
		//	
		// }
		return ((request.getParameter(ACTION_SUBMIT) == null)
			&& (request.getParameter(ACTION_UPDATE) == null)
			&& (request.getParameter(ACTION_DELETE) == null) && request.getParameter(ACTION_CANCEL) == null);
	}

	/**
	 * Validate the pubmed ID by i) parsing the string; ii) testing whether the record exists
	 * 
	 * @param citation Citation object
	 * @param myErrors List<String> of error messages
	 * @return a boolean indicating whether or not errors occurred
	 */
	private boolean validatePMID(Citation citation,List<String> myErrors) {
		boolean check = false;
		String pubmedId = citation.getPMID().trim();
		
		/* try to parse the pubmed ID number */
		try {
			Long.parseLong(pubmedId);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			check = true;
			myErrors.add("Pubmed ID should consist only of numbers.");
		}
		
		/* validate pubmed ID against NCBI site */
		try {
			URL url = new URL(
				"http://www.ncbi.nlm.nih.gov/sites/entrez?cmd=search&db=PubMed&holding=norwelib&otool=norwelib&term="
					+ pubmedId);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String str;
			while ((str = in.readLine()) != null) {
				if (str.indexOf("Wrong UID") >= 0) {
					check = true;
					myErrors.add("Pubmed ID you provided is not valid.");
					break;
				}
			}
			in.close();
		}
		/* error connecting to NCBI site */
		catch (IOException ioe) {
			ioe.printStackTrace();
			check = true;
			myErrors.add("An internal system problem encountered while checking the validity of Pubmed ID.");
		}		
		return check;
	}
	
	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#processFinish(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) throws Exception {

		// retrieve command object and map data to Domain object's Citation
		Citation citation = (Citation) command;
		
		// track if errors have occurred
		boolean check = false;
		
		// instantiate list of error messages
		List<String> myErrors = new ArrayList<String>();
		myErrors.add("Sorry, form could not be submitted or updated.");

		/* process pubmed ID */
		if (!TreebaseUtil.isEmpty(citation.getPMID())) {
			check = validatePMID(citation,myErrors);
		}

		/* process DOI */
		if (!TreebaseUtil.isEmpty(citation.getDoi())) {
			String doiId = citation.getDoi().trim();
			if (!doiId.startsWith("10.")) {
				check = true;
				myErrors.add("Invalid DOI -- DOI must start with 10.");

			} else if (!doiId.contains("/")) {
				check = true;
				myErrors.add("DOI must contain / separating a prefix & suffix.");
			}
		}

		/* process URL */
		if (!TreebaseUtil.isEmpty(citation.getURL())) {
			String url = citation.getURL().trim();
			if (!url.startsWith("http://")) {
				check = true;
				myErrors.add("URL must start with: http://");
			}
		}

		if (check) {
			request.setAttribute("errors", myErrors);
			return showForm(request, response, errors);
		}
		
		String publicationStatusUpdate = citation.getStatusDescriptionUpdate();
		if (request.getParameter(ACTION_SUBMIT) != null) {
			User user = ControllerUtil.getUser(request, mUserService);
			Study study = ControllerUtil.findStudy(request, mStudyService);
			if (citation.isCitationTypeChanged()) {
				// need to create a new citation object for the study:
				replaceCitation(study, citation);
			} else if (citation != study.getCitation()) {
				study.setCitation(citation);
				citation.setStudy(study);
			}
			if (publicationStatusUpdate != null) {
				CitationStatus publicationStatus = getCitationService()
					.findCitationStatusByDescription(publicationStatusUpdate);
				citation.setCitationStatus(publicationStatus);
			}
			mStudyService.update(study);
			ControllerUtil.saveStudy(request, study);
			return new ModelAndView(new RedirectView("citationForm.html"));
		} else if (request.getParameter(ACTION_UPDATE) != null) {
			if (publicationStatusUpdate != null) {
				CitationStatus publicationStatus = getCitationService()
					.findCitationStatusByDescription(publicationStatusUpdate);
				citation.setCitationStatus(publicationStatus);
			}
			//if (citation.isCitationTypeChanged()) {
				Study study = ControllerUtil.findStudy(request, mStudyService);
				replaceCitation(study, citation);
				mStudyService.update(study);
			//} else {
				getCitationService().update(citation);
			//}
		}
		return new ModelAndView(getSuccessView());
	}

	/**
	 * make year a drop down list; publication status list.
	 * 
	 * @param request
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {

		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> years = new ArrayList<String>();

		Integer startYear = 1900;
		Calendar calendar = Calendar.getInstance();
		for (int i = calendar.get(Calendar.YEAR); i >= startYear; i--) {
			years.add(Integer.toString(i));
		}
		dataMap.put("years", years);

		// drop down list for publication status
		List<String> statuses = CitationStatus.getCitationStatusList();
		dataMap.put("statuses", statuses);

		return dataMap;
	}

	/**
	 * Citation type changed. Need to create a new citation object and copy the properties.
	 * 
	 * @param pStudy
	 * @param pCitation
	 */
	private void replaceCitation(Study pStudy, Citation pCitation) {
		getCitationService().replaceCitation(pStudy, pCitation);
	}

	/**
	 * Set the CitationService field.
	 */
	public void setCitationService(CitationService pNewCitationService) {
		mCitationService = pNewCitationService;
	}

	/**
	 * Set the StudyService field.
	 */
	public void setStudyService(StudyService pNewStudyService) {
		mStudyService = pNewStudyService;
	}

	/**
	 * Set the SubmissionService field.
	 */
	public void setSubmissionService(SubmissionService pNewSubmissionService) {
		mSubmissionService = pNewSubmissionService;
	}

	/**
	 * Set the UserService field.
	 */
	public void setUserService(UserService pNewUserService) {
		mUserService = pNewUserService;
	}
}
