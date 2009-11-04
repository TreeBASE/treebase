package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyStatus;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.model.AGenericList;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * @author madhu
 * 
 */
public class ChangeStudyStatusController extends BaseFormController {

	private static final Logger LOGGER = Logger.getLogger(ChangeStudyStatusController.class);

	private UserService mUserService;
	private SubmissionService mSubmissionService;

	/**
	 * Return the UserService field.
	 * 
	 * @return UserService mUserService
	 */
	public UserService getUserService() {
		return mUserService;
	}

	/**
	 * Set the UserService field.
	 */
	public void setUserService(UserService pNewUserService) {
		mUserService = pNewUserService;
	}

	/**
	 * @return SubmissionService
	 */
	public SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	/**
	 * Set the SubmissionService
	 */
	public void setSubmissionService(SubmissionService pSubmissionService) {
		mSubmissionService = pSubmissionService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException myerrors) throws Exception {

		AGenericList aSubmissionList = (AGenericList) command;
		List<Submission> asbmcollection = (List<Submission>) aSubmissionList.getMyList();

		List<String> errorsList = new ArrayList<String>();

		if (request.getParameter(ACTION_UPDATE) != null) {

			for (Submission submission : asbmcollection) {

				Study study = submission.getStudy();
				StudyStatus studyStatus = study.getStudyStatus();

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("++ " + submission.getId() + "  "
						+ study.getTransientDescription());
				}

				if (study.getTransientDescription().equals(StudyStatus.READY)) {
					if (!submission.isReady()) {
						getSubmissionService().updateStudyStatusReady(submission.getId());
					}
				} else if (study.getTransientDescription().equals(StudyStatus.PUBLISHED)) {
					// check the status is ready before change to publish:
					if (!submission.isReady() && !submission.isPublished()) {
						// Undo the change:
						study.resetTransientDescription();

						errorsList.add("Submission with ID: " + submission.getId()
							+ " could not be updated.");

					} else {
						if (!submission.isPublished()) {
							getSubmissionService().updateStudyStatusPublish(submission.getId());
						}
					}
				} else if (study.getTransientDescription().equals(StudyStatus.INPROGRESS)) {
					if (!submission.isInProgress()) {
						getSubmissionService().updateStudyStatusInProgress(submission.getId());
					}
				}

			}
		}

		if (errorsList.size() > 0) {
			request.setAttribute("errors", errorsList);
			return showForm(request, response, myerrors);
		}

		return new ModelAndView(getSuccessView());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {

		List<Submission> submissionList = new ArrayList<Submission>();

		String currentURL = null;
		String adminComingFrom = (String)request.getSession().getAttribute("ADMIN_COMING_FROM");
		if (request.getRequestURI().indexOf("/admin/readyStateStudies") >= 0) {
			if (getSubmissionService().findReadySubmissions() != null) {
				submissionList.addAll(getSubmissionService().findReadySubmissions());
			}
			currentURL = "/admin/readyStateStudies.html";
		} else if (adminComingFrom != null && adminComingFrom.equals("Management")) {

			Collection<Submission> submissionColl = new ArrayList<Submission>();
			String userinfo = getMessageSourceAccessor().getMessage("user.management.userinfo").trim();
			List<User> users = (List<User>) request.getSession().getAttribute(userinfo);
			
			for (User aUser : users) {
				aUser = getUserService().resurrect(aUser);
				submissionColl.addAll(aUser.getSubmissionsCopy());
			}

			//String userchoice = (String) request.getSession().getAttribute("USERCHOICE");
			//String userinfo = (String) request.getSession().getAttribute("USERINFO");
			String studytype = (String) request.getSession().getAttribute("STUDYTYPE");

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("**STUDY TYPE: " + studytype);
			}

//			if (userchoice.startsWith("User") && userinfo.length() != 0) {
//				submissionColl.addAll(getUserService()
//					.findUserByName(userinfo).getSubmissionsCopy());
//			} else if (userchoice.startsWith("Email") && userinfo.length() != 0) {
//				if (LOGGER.isInfoEnabled()) {
//					LOGGER.info("INSIDE EMAIL CHECK BLOCK");
//				}
//
//				List<User> userList = getUserService().findUserByEmail(userinfo);
//
//				LOGGER.info("TOTAL NUMBER OF USERS: " + userList.size());
//
//				for (User aUser : userList) {
//					if (aUser.getSubmissionsCopy().size() > 0)
//						submissionColl.addAll(aUser.getSubmissionsCopy());
//				}
//
//			} else if (userchoice.startsWith("Last") && userinfo.length() != 0) {
//
//				List<User> userList = getUserService().findUserByLastName(userinfo);
//
//				LOGGER.info("TOTAL NUMBER OF USERS With last name: " + userinfo + " "
//					+ userList.size());
//
//				for (User aUser : userList) {
//					if (aUser.getSubmissionsCopy().size() > 0)
//						submissionColl.addAll(aUser.getSubmissionsCopy());
//				}
//
//			}
			for (Submission submission : submissionColl) {

				if (studytype == null || studytype.equals(Constants.STUDYSTATUS_ALL)) {
					submissionList.add(submission);
				} else if (studytype.equals(StudyStatus.READY)) {
					if (submission.getStudy().isReady()) {
						submissionList.add(submission);
					}
				} else if (studytype.equals(StudyStatus.PUBLISHED)) {
					if (submission.getStudy().isPublished()) {
						submissionList.add(submission);
					}
				} else if (studytype.equals(StudyStatus.INPROGRESS)) {
					if (submission.getStudy().isInProgress()) {
						submissionList.add(submission);
					}
				}

			}

			currentURL = "/admin/userManagement.html";
		} else if (adminComingFrom != null && adminComingFrom.equals("SearchBySubmissionID")) {
			Long submissionID = (Long) request.getSession().getAttribute(
				"SUBMISSION_FROM_SEARCHBYSUBMISSIONID");

			if (submissionID != null) {
				submissionList.add(getSubmissionService().findSubmissionByID(
					submissionID));
			}

			currentURL = "/admin/searchBySubmissionID.html";
		} else if (adminComingFrom != null && adminComingFrom.equals("SelectStudies")) {

			String studyType = (String) request.getSession().getAttribute("STUDYTYPE");

			if (studyType != null) {
				if (studyType.equals(Constants.STUDYSTATUS_ALL)
					|| studyType.equals(StudyStatus.INPROGRESS)) {
					submissionList.addAll(getSubmissionService().findInProgressSubmissions());
				} else if (studyType.equals(Constants.STUDYSTATUS_ALL)
					|| studyType.equals(StudyStatus.READY)) {
					submissionList.addAll(getSubmissionService().findReadySubmissions());
				} else if (studyType.equals(Constants.STUDYSTATUS_ALL)
					|| studyType.equals(StudyStatus.PUBLISHED)) {
					submissionList.addAll(getSubmissionService().findPublishedSubmissions());
				}

			}

			currentURL = "/admin/selectStudies.html";
		}
		
		if (currentURL != null) {
			request.getSession().setAttribute(ControllerUtil.DEL_STUDY_PAGE_KEY, currentURL);
		}
		return new AGenericList(submissionList);
	}
	/*
	 * protected Map referenceData(HttpServletRequest pRequest) throws Exception { Map resultMap =
	 * new HashMap();
	 * 
	 * String username = (String) pRequest.getSession().getAttribute("USERNAME"); String userEmail =
	 * getUserService().findUserByName(username).getEmailAddressString();
	 * 
	 * StringBuilder userEmailInfo = new StringBuilder(); userEmailInfo.append("<a href='mailto:");
	 * userEmailInfo.append(userEmail); userEmailInfo.append("?subject=From Treebase-2 Editor'>");
	 * userEmailInfo.append(username); userEmailInfo.append("</a>");
	 * 
	 * resultMap.put("atreebaseuser", userEmailInfo.toString()); return resultMap; }
	 */
}
