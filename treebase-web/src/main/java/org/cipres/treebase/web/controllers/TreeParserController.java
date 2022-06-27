
package org.cipres.treebase.web.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import org.cipres.treebase.domain.admin.UserService;
import org.cipres.treebase.domain.nexus.NexusService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.web.model.FileBean;
import org.cipres.treebase.web.model.MyProgressionListener;
import org.cipres.treebase.web.util.ControllerUtil;

/**
 * TestParserController.java
 * 
 * Created on May 18, 2006
 * 
 * @author jruan
 * 
 */
public class TreeParserController extends BaseFormController {
	private static final Logger LOGGER = LogManager.getLogger(TestParserController.class);

	private static final String PARSER_STUDY_NAME = "MesquiteTestStudy";

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

	/**
	 * 
	 * Creation date: May 18, 2006 6:33:54 PM
	 */
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) throws Exception {

		String userName = request.getRemoteUser();
		String sep = System.getProperty("file.separator");
		String uploadDir = getServletContext().getRealPath("/NexusFileUpload") + sep + userName;

		/* make sure the directory exists */
		File dirPath = new File(uploadDir);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
		/* store each file uploaded by the user */
		List<File> files = new ArrayList<File>();

		for (FileBean file : getFiles(request)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Uploading file to =>" + uploadDir + sep + file.getName()); //$NON-NLS-1$
			}

			File uploadedFile = new File(uploadDir + sep + file.getName());
			FileCopyUtils.copy(file.getData(), uploadedFile);
			files.add(uploadedFile);
		}

		if (files.isEmpty()) {
			return new ModelAndView(getCancelView());
		}

		Study study = ControllerUtil.findStudy(request, getStudyService());
		Submission submission = study.getSubmission();
		MyProgressionListener listener = new MyProgressionListener();

		getSubmissionService().addNexusFiles(submission, files, listener);
		getStudyService().addNexusFiles(study, files);

		return new ModelAndView(getSuccessView());
	}

	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {

		Collection<Study> studies = getStudyService().findByName(PARSER_STUDY_NAME, true);

		Study s = null;
		if (studies.isEmpty()) {

			// TODO: these code will not work.
			// s = new Study();
			// s.setName(PARSER_STUDY_NAME);
			//
			// s = getStudyService().update(s);
		} else {
			s = studies.iterator().next();
		}

		ControllerUtil.saveStudy(request, s);
		// getSubmissionService().deleteSubmittedData(s.getSubmission());

		return super.referenceData(request);
	}
}
