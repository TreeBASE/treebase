package org.cipres.treebase.web.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.StudyStatus;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

/**
 * Unit tests for HomeController.
 */
public class HomeControllerTest {

	private HomeController controller;
	
	@Mock
	private SubmissionService submissionService;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpServletResponse response;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		controller = new HomeController();
		controller.setSubmissionService(submissionService);
		controller.setRecentStudiesCount(8);
	}
	
	@Test
	public void testHandleRequest_WithStudies() throws Exception {
		// Arrange
		Collection<Submission> submissions = createTestSubmissions(3);
		when(submissionService.findRecentPublishedSubmissions(8)).thenReturn(submissions);
		
		// Act
		ModelAndView result = controller.handleRequest(request, response);
		
		// Assert
		assertNotNull("Should return ModelAndView", result);
		assertEquals("Should use 'home' view", "home", result.getViewName());
		
		@SuppressWarnings("unchecked")
		List<Study> recentStudies = (List<Study>) result.getModel().get("recentStudies");
		assertNotNull("Should include recentStudies in model", recentStudies);
		assertEquals("Should have correct number of studies", 3, recentStudies.size());
	}
	
	@Test
	public void testHandleRequest_EmptyDatabase() throws Exception {
		// Arrange
		when(submissionService.findRecentPublishedSubmissions(8)).thenReturn(new ArrayList<>());
		
		// Act
		ModelAndView result = controller.handleRequest(request, response);
		
		// Assert
		assertNotNull("Should return ModelAndView", result);
		assertEquals("Should use 'home' view", "home", result.getViewName());
		
		@SuppressWarnings("unchecked")
		List<Study> recentStudies = (List<Study>) result.getModel().get("recentStudies");
		assertNotNull("Should include recentStudies in model", recentStudies);
		assertTrue("Should have empty list", recentStudies.isEmpty());
	}
	
	@Test
	public void testHandleRequest_ServiceThrowsException() throws Exception {
		// Arrange - simulate database error
		when(submissionService.findRecentPublishedSubmissions(8)).thenThrow(new RuntimeException("Database error"));
		
		// Act
		ModelAndView result = controller.handleRequest(request, response);
		
		// Assert - should gracefully degrade
		assertNotNull("Should return ModelAndView even on error", result);
		assertEquals("Should use 'home' view", "home", result.getViewName());
		
		@SuppressWarnings("unchecked")
		List<Study> recentStudies = (List<Study>) result.getModel().get("recentStudies");
		assertNotNull("Should include recentStudies in model", recentStudies);
		assertTrue("Should have empty list on error", recentStudies.isEmpty());
	}
	
	@Test
	public void testHandleRequest_NullStudyInSubmission() throws Exception {
		// Arrange - submission with null study
		Collection<Submission> submissions = new ArrayList<>();
		Submission submission = mock(Submission.class);
		when(submission.getStudy()).thenReturn(null);
		submissions.add(submission);
		
		when(submissionService.findRecentPublishedSubmissions(8)).thenReturn(submissions);
		
		// Act
		ModelAndView result = controller.handleRequest(request, response);
		
		// Assert
		assertNotNull("Should return ModelAndView", result);
		
		@SuppressWarnings("unchecked")
		List<Study> recentStudies = (List<Study>) result.getModel().get("recentStudies");
		assertTrue("Should exclude null studies", recentStudies.isEmpty());
	}
	
	@Test
	public void testCustomRecentStudiesCount() {
		// Arrange
		controller.setRecentStudiesCount(15);
		
		// Act
		int count = controller.getRecentStudiesCount();
		
		// Assert
		assertEquals("Should use custom count", 15, count);
	}
	
	private Collection<Submission> createTestSubmissions(int count) {
		Collection<Submission> submissions = new ArrayList<>();
		
		for (int i = 0; i < count; i++) {
			Submission submission = mock(Submission.class);
			Study study = mock(Study.class);
			Citation citation = mock(Citation.class);
			StudyStatus status = mock(StudyStatus.class);
			
			when(submission.getStudy()).thenReturn(study);
			when(study.getId()).thenReturn((long) (i + 1));
			when(study.getName()).thenReturn("Test Study " + (i + 1));
			when(study.getCitation()).thenReturn(citation);
			when(study.getStudyStatus()).thenReturn(status);
			when(status.isPublished()).thenReturn(true);
			when(citation.getTitle()).thenReturn("Test Study Title " + (i + 1));
			
			submissions.add(submission);
		}
		
		return submissions;
	}
}
