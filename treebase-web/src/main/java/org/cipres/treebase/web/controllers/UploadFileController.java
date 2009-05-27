/*
 * Copyright 2006 CIPRES project. http://www.phylo.org/ All Rights Reserved.
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.admin.UserService;
import org.cipres.treebase.domain.nexus.NexusService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.domain.study.UploadFileResult;
import org.cipres.treebase.web.model.FileBean;
import org.cipres.treebase.web.model.MyProgressionListener;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * UploadFileController.java
 * 
 * Created on May 18, 2006
 * 
 * @author lcchan
 * 
 */
public class UploadFileController extends BaseFormController {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = Logger.getLogger(UploadFileController.class);

	private UserService mUserService;
	private SubmissionService mSubmissionService;
	private StudyService mStudyService;
	private NexusService mNexusService;

	/**
	 * Return the NexusService field.
	 * 
	 * @return NexusService mNexusService
	 */
	public NexusService getNexusService() {
		return mNexusService;
	}

	/**
	 * Set the NexusService field.
	 */
	public void setNexusService(NexusService pNewNexusService) {
		mNexusService = pNewNexusService;
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
	 * Return the SubmissionService field.
	 * 
	 * @return SubmissionService mSubmissionService
	 */
	public SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	/**
	 * Set the SubmissionService field.
	 */
	public void setSubmissionService(SubmissionService pNewSubmissionService) {
		mSubmissionService = pNewSubmissionService;
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
	 * Set the UserService field.
	 */
	public void setUserService(UserService pNewUserService) {
		mUserService = pNewUserService;
	}

	/**
	 * Method to retrieve the list of files entered by the user
	 * 
	 * Creation date: May 19, 2006 10:57:01 AM
	 */
	public List<FileBean> getFiles(HttpServletRequest request) throws Exception {
		if (request instanceof MultipartHttpServletRequest) {

			MultipartHttpServletRequest multiReq = (MultipartHttpServletRequest) request;
			MultipartFile file = null;
			FileBean bean = null;
			List<FileBean> files = new ArrayList<FileBean>();
			for (Object o : multiReq.getFileMap().values()) {
				if (o != null) {
					file = (MultipartFile) o;
					if (!file.isEmpty()) {
						bean = new FileBean();
						bean.setName(file.getOriginalFilename());
						bean.setData(file.getBytes());
						files.add(bean);
					}
				}
			}
			return files;
		}
		return null;

	}

	/**
	 * 
	 * Creation date: May 18, 2006 6:33:54 PM
	 */
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException myerrors) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering onSubmit()..."); //$NON-NLS-1$
		}

		String userName = request.getRemoteUser();
		// String sep = System.getProperty("file.separator");
		String uploadDir = getServletContext()
			.getRealPath(TreebaseUtil.FILESEP + "NexusFileUpload")
			+ TreebaseUtil.FILESEP + userName;

		/* make sure the directory exists */
		File dirPath = new File(uploadDir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		/* store each file uploaded by the user */
		List<File> files = new ArrayList<File>();

		String firstFile = null;
		for (FileBean file : getFiles(request)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER
					.debug("Uploading file to =>" + uploadDir + TreebaseUtil.FILESEP + file.getName()); //$NON-NLS-1$
			}

			File uploadedFile = new File(uploadDir + TreebaseUtil.FILESEP + file.getName());
			FileCopyUtils.copy(file.getData(), uploadedFile);
			files.add(uploadedFile);

			if (firstFile == null) {
				firstFile = file.getName();
			}
		}

		if ((files.size() == 0)) {
			request.setAttribute("errors", "Please make file(s) selection");
			return showForm(request, response, myerrors);
		}

		/* pass List<File> to service layer */
		// get user's Submission Object
		Study study = ControllerUtil.findStudy(request, mStudyService);
		Submission submission = study.getSubmission();
		MyProgressionListener listener = new MyProgressionListener();

		UploadFileResult uploadResult = getSubmissionService().addNexusFilesJDBC(
			submission,
			files,
			listener);

		request.getSession().setAttribute("uploadMatrixCount", "" + uploadResult.getMatrixCount());
		request.getSession().setAttribute("uploadTreeCount", "" + uploadResult.getTreeCount());
		request.getSession().setAttribute("uploadFileName", firstFile);

		return new ModelAndView(getSuccessView());
	}
}
