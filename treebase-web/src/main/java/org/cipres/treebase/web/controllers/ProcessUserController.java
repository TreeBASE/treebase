package org.cipres.treebase.web.controllers;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.admin.User;
import org.cipres.treebase.domain.admin.UserRole;
import org.cipres.treebase.domain.admin.UserService;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.web.model.MyProgressionListener;
import org.cipres.treebase.web.util.CitationParser;
import org.cipres.treebase.web.util.ControllerUtil;
import org.cipres.treebase.web.util.DryadUtil;

/**
 * @author madhu
 * 
 * created on November 15th, 2007
 * 
 */
public class ProcessUserController implements Controller {
	private SubmissionService mSubmissionService;
	private UserService mUserService;
	
	public UserService getUserService() {
		return mUserService;
	}

	public void setUserService(UserService mUserService) {
		this.mUserService = mUserService;
	}

	public SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	public void setSubmissionService(SubmissionService mSubmissionService) {
		this.mSubmissionService = mSubmissionService;
	}

	private static final Logger LOGGER = Logger.getLogger(ProcessUserController.class);

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String importKey = (String)request.getSession().getAttribute("importKey");
		User user = ControllerUtil.getUser(request, mUserService);
		
		if(importKey != null && importKey.length()>0){
									
			String uploadpath = request.getSession().getServletContext()
        	.getRealPath(TreebaseUtil.FILESEP + "DryadFileUpload")
        	+ TreebaseUtil.FILESEP +  importKey;			
			String importStatus="";
			
			File uploadDir=new File(uploadpath);
			if(!uploadDir.exists()){
				importStatus = "NOT FOUND";
				request.getSession().setAttribute("importStatus", importStatus);
				return new ModelAndView("redirect:/user/submissionList.html");
			}
			
			File[] uploadFiles = uploadDir.listFiles(new FileFilter(){public boolean accept(File file){return file.isDirectory();}}); 			
			if(uploadFiles.length==0){
				importStatus = "NOT FOUND";
				request.getSession().setAttribute("importStatus", importStatus);
				return new ModelAndView("redirect:/user/submissionList.html");
			}
			File bagitPath = uploadFiles[0];
			File dataPath = new File(bagitPath, "data");
			
			try{
			        CitationParser cparser= new CitationParser(dataPath);
			        Citation citation = cparser.getCitation();
					Study study = new Study();
					study.setName(bagitPath.getName());
					study.setCitation(citation);					
			        citation.setStudy(study);					
					Submission submission = mSubmissionService.createSubmission(user, study);
					long unixTime = System.currentTimeMillis() / 1000L;
					
					List<File> files = DryadUtil.getDataFiles(dataPath);
					HashMap<String, Integer> filenamesHash = new HashMap<String, Integer>();
			        for(int i=0; i<files.size(); i++ ) {
			        	
			        	int filecount = 1;
			        	
			        	File originalFile = new File(files.get(i).getAbsolutePath());
			        	
			        	/* This keeps a hashmap of the files so that going through it knows the count of each file
			        	 * Each file then has a prefix of the count and unix timestamp of the upload
			        	 */
			        	
			        	if (filenamesHash.containsKey(originalFile.getName())) {
			        		filecount = filenamesHash.get(originalFile.getName()) + 1;
			        		filenamesHash.put(originalFile.getName(), filecount);
			        	}
			        	else {
			        		filenamesHash.put(originalFile.getName(), filecount);
			        	}

			        	String copyDir = request.getSession().getServletContext()
			        	.getRealPath(TreebaseUtil.FILESEP + "NexusFileUpload")
						+ TreebaseUtil.FILESEP + request.getRemoteUser();

			        	
			        	File copyFile = new File(copyDir + 
			        							TreebaseUtil.FILESEP +
			        							filenamesHash.get(originalFile.getName()) +
			        							"_" + 
			        							unixTime + 
			        							"_" + 
			        							files.get(i).getName());
			        	
			        	FileUtils.copyFile(originalFile, copyFile);
			        
			        	files.remove(i);
			        	files.add(i,copyFile);

			        	submission.getStudy().addNexusFile(files.get(i).getName(), TreebaseUtil.readFileToString(files.get(i)));
			        }
					MyProgressionListener listener = new MyProgressionListener();
					getSubmissionService().addNexusFilesJDBC(submission, files, listener);
					
					// save Study object to session					
					//mSubmissionService.save(submission);
					mSubmissionService.save(study);	
					//mSubmissionService.save(citation);
					
					importStatus = "OK";
			}catch (Exception e) {
					importStatus = "FAILED";
			}
			
			request.getSession().setAttribute("importStatus", importStatus);
			request.getSession().removeAttribute("importKey");			
			return new ModelAndView("redirect:/user/submissionList.html");
			
		}	
		if (request.isUserInRole(UserRole.ROLE_ADMIN) || request.isUserInRole(UserRole.ROLE_ASSO_EDITOR)) {

			return new ModelAndView("redirect:/admin/administrationPage.html");
		}
		return new ModelAndView("redirect:/user/submissionList.html");
	}
}
