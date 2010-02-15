package org.cipres.treebase.web.controllers;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.admin.UserRole.TBPermission;
import org.cipres.treebase.domain.nexus.NexusService;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyService;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;
import org.cipres.treebase.web.util.ControllerUtil;
import org.cipres.treebase.web.util.WebUtil;
import org.springframework.web.servlet.mvc.Controller;

public abstract class AbstractDownloadController implements Controller {
	protected String mFormatParameter = "format";
	protected static final int FORMAT_NEXUS = 1;
	protected static final int FORMAT_NEXML = 2;
	protected static final int FORMAT_RDF = 3;
	private NexusService mNexmlService;	
	private NexusService mRdfaService;	
	private SubmissionService mSubmissionService;
	private static String mNexmlContentType = "application/xml";
	private static String mRdfContentType = "application/rdf+xml";

	/**
	 * Return the StudyService field.
	 * 
	 * @return StudyService mStudyService
	 */
	public abstract StudyService getStudyService();

	/**
	 * Set the StudyService field.
	 */
	public abstract void setStudyService(StudyService pNewStudyService);
	
	protected Properties getDefaultProperties(HttpServletRequest request) {
		Properties properties = new Properties();
		StringBuffer baseURI = new StringBuffer("http://");
		baseURI
			.append(request.getServerName())
			.append(':')
			.append(request.getServerPort())
			.append("/treebase-web/phylows/");
		properties.setProperty("nexml.uri.base", baseURI.toString());
		return properties;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	protected int getFormat (HttpServletRequest request) {
		String requestedFormat = request.getParameter(mFormatParameter);
		if ( null != requestedFormat ) {
			if ( requestedFormat.equalsIgnoreCase("nexml") ) {
				return FORMAT_NEXML;
			}
			else if ( requestedFormat.equalsIgnoreCase("rdf") ) {
				return FORMAT_RDF;
			}
			else {
				return FORMAT_NEXUS; // default
			}
		}
		else {
			return FORMAT_NEXUS; // default
		}
	}
	
	/**
	 * 
	 * @param objectId
	 * @param request
	 * @return
	 */
	protected String getFileName(long id,HttpServletRequest request) {
		if ( getFormat(request) == FORMAT_NEXML ) {
			return getFileNamePrefix() + id + ".xml";
		}
		if ( getFormat(request) == FORMAT_RDF ) {
			return getFileNamePrefix() + id + ".rdf";
		}		
		return getFileNamePrefix() + id + ".nex";
	}	
	
	/**
	 * 
	 * @return a context-dependent prefix (e.g. "M" for a matrix)
	 */
	protected abstract String getFileNamePrefix();
	
	/**
	 * 
	 * @return a context-dependent serialization (e.g. a nexus string)
	 */
	protected abstract String getFileContent(long objectId,HttpServletRequest request);
	
	/**
	 * 
	 * @param objectId - the id of the focal object (e.g. a tree)
	 * @return the study to which the focal object belongs
	 */
	protected abstract Study getStudy(long objectId,HttpServletRequest request);
	
	protected String getDownloadDir (HttpServletRequest request) {
		String downloadDir = request.getSession().getServletContext().getRealPath(
				TreebaseUtil.FILESEP + "NexusFileDownload")
				+ TreebaseUtil.FILESEP + request.getRemoteUser();
		return downloadDir;
	}

	/**
	 * 
	 * @param request
	 * @param pFileContent
	 * @param objectId
	 * @param downloadDirName
	 */
	protected void generateAFileDynamically(HttpServletRequest request, HttpServletResponse response, long objectId) {
        if ( ! isSubmitter(objectId,request) && ! ControllerUtil.isReviewerAccessGranted(request) && ! getStudy(objectId,request).isPublished() ) {
        	response.setStatus(HttpServletResponse.SC_SEE_OTHER);        
        	response.setHeader("Location", "/treebase-web/accessviolation.html");
        }
        else {
			String downloadDirName = getDownloadDir(request);
			File dirPath = new File(downloadDirName);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}
			String fileName = getFileName(objectId,request);
			try {
				File file = new File(downloadDirName + TreebaseUtil.FILESEP + fileName);
				FileWriter out = new FileWriter(file);
				out.write(getFileContent(objectId,request));
				out.close();
				if ( getFormat(request) == FORMAT_NEXML ) {
					WebUtil.downloadFile(response, downloadDirName, fileName, mNexmlContentType);
				}
				else if ( getFormat(request) == FORMAT_RDF ) {
					WebUtil.downloadFile(response, downloadDirName, fileName, mRdfContentType);
				}			
				else {
					WebUtil.downloadFile(response, downloadDirName, fileName);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	
	private boolean isSubmitter (long objectId,HttpServletRequest request) {
		Study study = ControllerUtil.findStudy(request, getStudyService());
		//Study study = getStudy(objectId,request);
		Submission submission = study.getSubmission();
		TBPermission tbp = getSubmissionService().getPermission(request.getRemoteUser(), submission.getId());
		if (tbp == TBPermission.WRITE || tbp == TBPermission.READ_ONLY || tbp == TBPermission.SUBMITTED_WRITE) {
			return true;
		} 
		else {
			return false;
		}
	}

	/**
	 * 
	 * @return
	 */
	public NexusService getNexmlService() {
		return mNexmlService;
	}

	/**
	 * 
	 * @param nexmlService
	 */
	public void setNexmlService(NexusService nexmlService) {
		mNexmlService = nexmlService;
	}

	public NexusService getRdfaService() {
		return mRdfaService;
	}

	public void setRdfaService(NexusService rdfaService) {
		mRdfaService = rdfaService;
	}
	
	public void setSubmissionService(SubmissionService submissionService) {
		mSubmissionService = submissionService;
	}
	
	public SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

}
