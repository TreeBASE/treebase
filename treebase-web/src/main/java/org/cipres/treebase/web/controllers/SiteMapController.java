


package org.cipres.treebase.web.controllers;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;



/**
 * @author mjd 20080701
 */
public class SiteMapController implements Controller{
	/**
	 * Logger for this class
	 */
	//private static final Logger LOGGER = Logger.getLogger(SearchController.class);
	private SubmissionService mSubmissionService;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		List<String> phyloURL = new ArrayList<String>();
		
		Collection<Submission> submissions = mSubmissionService.findPublishedSubmissions();
		
		for ( Submission submission : submissions ) {
			phyloURL.add(submission.getStudy().getPhyloWSPath().getPurl().toString());
		}

		return new ModelAndView("sitemap", "phyloURL", phyloURL);
	}
	
	public SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	public void setSubmissionService(SubmissionService pNewSubmissionService) {
		mSubmissionService = pNewSubmissionService;
	}
}


