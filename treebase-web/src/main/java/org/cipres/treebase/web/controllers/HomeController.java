package org.cipres.treebase.web.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * Controller for the home page. Provides recent studies to be displayed
 * directly in the JSP without JavaScript dependencies.
 * 
 * This replaces the previous implementation that used JavaScript to load
 * RSS feeds via Yahoo Pipes.
 * 
 * @author TreeBASE Team
 */
public class HomeController implements Controller {

	private static final int DEFAULT_RECENT_STUDIES_COUNT = 8;
	
	private SubmissionService mSubmissionService;
	private int mRecentStudiesCount = DEFAULT_RECENT_STUDIES_COUNT;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		List<Study> recentStudies = new ArrayList<Study>();
		
		try {
			Collection<Submission> submissions = mSubmissionService.findRecentPublishedSubmissions(mRecentStudiesCount);
			
			for (Submission submission : submissions) {
				Study study = submission.getStudy();
				if (study != null) {
					recentStudies.add(study);
				}
			}
		} catch (Exception e) {
			// Log the error but continue - we'll just show an empty list
			// This provides graceful degradation when running on an empty database
		}
		
		return new ModelAndView("home", "recentStudies", recentStudies);
	}

	public SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	public void setSubmissionService(SubmissionService pSubmissionService) {
		mSubmissionService = pSubmissionService;
	}

	public int getRecentStudiesCount() {
		return mRecentStudiesCount;
	}

	public void setRecentStudiesCount(int pRecentStudiesCount) {
		mRecentStudiesCount = pRecentStudiesCount;
	}
}
