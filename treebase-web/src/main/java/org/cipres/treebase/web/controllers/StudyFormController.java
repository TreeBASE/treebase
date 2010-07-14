
package org.cipres.treebase.web.controllers;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.TreebaseUtil;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserService;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.web.Constants;
import org.cipres.treebase.web.model.MyProgressionListener;
import org.cipres.treebase.web.util.ControllerUtil;
import org.cipres.treebase.web.util.DryadUtil;

/**
 * StudyFormController.java
 * 
 * Created on June 5, 2006
 * 
 * @author lcchan
 * 
 */
public class StudyFormController extends BaseFormController {
	private static final Logger LOGGER = Logger.getLogger(StudyFormController.class);

	private StudyService mStudyService;
	private SubmissionService mSubmissionService;
	private UserService mUserService;

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
	 * 
	 * Creation date: June 5, 2006 5:28:42 PM
	 */
	public ModelAndView onSubmit(
		HttpServletRequest request,
		HttpServletResponse response,
		Object command,
		BindException errors) throws Exception {

		Study study = (Study) command;
		User user = ControllerUtil.getUser(request, mUserService);
		String importKey = (String)request.getSession().getAttribute("importKey");
		request.getSession().removeAttribute("importKey");
		
		if (request.getParameter(ACTION_SUBMIT) != null) {
			// Study must be submitted with citation together
			// here we are just saving the data to the session
			// request.getSession().setAttribute(Constants.STUDY_KEY, study);

			// BeanUtils.copyProperties(citationCommand.getCitationMap(citationType),
			// citationCommand);

			// FIXME citation
			// Citation c = new ArticleCitation();
			// c.setStudy(study);
			// study.setCitation(c);

			// retrieve Study object from session to be submitted with citation
			// Study study = (Study) request.getSession().getAttribute(Constants.STUDY_KEY);
			// citationCommand.getCitationMap(citationType).setStudy(study);
			Submission submission = mSubmissionService.createSubmission(user, study);

			// save Study object to session and remove
			ControllerUtil.saveStudy(request, submission.getStudy());

		} else if(importKey != null && importKey.length()>0){
			String uploadpath = getServletContext()
        	.getRealPath(TreebaseUtil.FILESEP + "DryadFileUpload")
        	+ TreebaseUtil.FILESEP +  importKey;			
			String importStatus="";
			
			File uploadDir=new File(uploadpath);
			if(!uploadDir.exists()){
				importStatus = "NOT FOUND";
				return new ModelAndView(new RedirectView("submissionList.html"));
			}
			
			File[] uploadFiles = uploadDir.listFiles(new FileFilter(){public boolean accept(File file){return file.isDirectory();}}); 			
			if(uploadDir.length()==0){
				importStatus = "NOT FOUND";
				return new ModelAndView(new RedirectView("submissionList.html"));	
			}
			File bagitPath = uploadFiles[0];
			File dataPath = new File(bagitPath, "data");
			
			try{
					Submission submission = mSubmissionService.createSubmission(user, new Study());
					Citation citation = DryadUtil.createCitation(dataPath);
					submission.getStudy().setCitation(citation);
					citation.setStudy(submission.getStudy());
			
					Collection<File> files = DryadUtil.getDataFiles(dataPath);
					MyProgressionListener listener = new MyProgressionListener();
					getSubmissionService().addNexusFilesJDBC(submission, files, listener);
					// save Study object to session
					ControllerUtil.saveStudy(request, submission.getStudy());
					importStatus = "OK";
			}catch (Exception e) {
					importStatus = "FAILED";
			}
			
			request.setAttribute("importStatus", importStatus);
			//request.getSession().removeAttribute("importKey");
			return new ModelAndView(new RedirectView("submissionList.html"));
			
		}else if (request.getParameter(ACTION_UPDATE) != null) {
			mStudyService.update(study);
		} else if (request.getParameter(ACTION_DELETE) != null) {

			study = getStudyService().update(study);
			getSubmissionService().deleteSubmission(study.getSubmission());
			return new ModelAndView(new RedirectView("submissionList.html"));
		} else if (request.getParameter(ACTION_CANCEL) != null) {
			return new ModelAndView(getCancelView());
		}
		return new ModelAndView(getSuccessView());
	}

	/**
	 * There are 3 different ways a StudyForm can be accessed
	 * 
	 * i) from the list of submission (submission_id is indicated) ii) from the right RHS menu (no
	 * submission_id) iii)new submission
	 * 
	 * Creation date: June 5, 2006 5:19:18 PM
	 */
	protected Object formBackingObject(HttpServletRequest request) throws ServletException {
		Study study = new Study();
		String submission_id = ServletRequestUtils.getStringParameter(request, "id", null);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("In studyFormController--");
		}

		if (!TreebaseUtil.isEmpty(submission_id)) {
			study = mStudyService.findBySubmissionID(Long.parseLong(submission_id));
			ControllerUtil.saveStudy(request, study); // user has made selection
			return study;
		}
		// if we start a new form (indicated by URL)
		String form = request.getParameter("form");
		if (form != null && form.equals("new")) {
			// start a new form, remove what's in the session
			request.getSession().removeAttribute(Constants.STUDY_KEY);
			request.getSession().removeAttribute(Constants.STUDY_MAP);
			return new Study();
		}
		// if we are updating a data that's already in the db (access from RHS menu)
		study = ControllerUtil.findStudy(request, mStudyService);
		return study;
	}
}
