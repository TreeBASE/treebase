package org.cipres.treebase.web.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
 * Unit tests for RssFeedController.
 */
public class RssFeedControllerTest {

	private RssFeedController controller;
	
	@Mock
	private SubmissionService submissionService;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpServletResponse response;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		controller = new RssFeedController();
		controller.setSubmissionService(submissionService);
	}
	
	@Test
	public void testHandleRequest_EmptyDatabase() throws Exception {
		// Arrange
		when(request.getParameter("limit")).thenReturn(null);
		when(submissionService.findRecentPublishedSubmissions(20)).thenReturn(new ArrayList<>());
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);
		
		// Act
		ModelAndView result = controller.handleRequest(request, response);
		
		// Assert
		assertNull("Should return null ModelAndView for direct response writing", result);
		verify(response).setContentType("application/rss+xml; charset=UTF-8");
		
		String rssContent = stringWriter.toString();
		assertTrue("Should contain RSS version", rssContent.contains("rss version=\"2.0\""));
		assertTrue("Should contain channel element", rssContent.contains("<channel>"));
		assertTrue("Should contain TreeBASE title", rssContent.contains("TreeBASE"));
	}
	
	@Test
	public void testHandleRequest_WithStudies() throws Exception {
		// Arrange
		Collection<Submission> submissions = createTestSubmissions();
		when(request.getParameter("limit")).thenReturn("5");
		when(submissionService.findRecentPublishedSubmissions(5)).thenReturn(submissions);
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);
		
		// Act
		controller.handleRequest(request, response);
		
		// Assert
		String rssContent = stringWriter.toString();
		assertTrue("Should contain item elements", rssContent.contains("<item>"));
		assertTrue("Should contain study title", rssContent.contains("Test Study Title"));
		assertTrue("Should contain link", rssContent.contains("<link>"));
	}
	
	@Test
	public void testHandleRequest_LimitParameter() throws Exception {
		// Arrange
		when(request.getParameter("limit")).thenReturn("10");
		when(submissionService.findRecentPublishedSubmissions(10)).thenReturn(new ArrayList<>());
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);
		
		// Act
		controller.handleRequest(request, response);
		
		// Assert
		verify(submissionService).findRecentPublishedSubmissions(10);
	}
	
	@Test
	public void testHandleRequest_InvalidLimitParameter() throws Exception {
		// Arrange
		when(request.getParameter("limit")).thenReturn("invalid");
		when(submissionService.findRecentPublishedSubmissions(20)).thenReturn(new ArrayList<>());
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);
		
		// Act
		controller.handleRequest(request, response);
		
		// Assert - should use default limit of 20
		verify(submissionService).findRecentPublishedSubmissions(20);
	}
	
	@Test
	public void testHandleRequest_LimitCapped() throws Exception {
		// Arrange
		when(request.getParameter("limit")).thenReturn("500");
		when(submissionService.findRecentPublishedSubmissions(100)).thenReturn(new ArrayList<>());
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		when(response.getWriter()).thenReturn(printWriter);
		
		// Act
		controller.handleRequest(request, response);
		
		// Assert - should cap at 100
		verify(submissionService).findRecentPublishedSubmissions(100);
	}
	
	private Collection<Submission> createTestSubmissions() {
		Collection<Submission> submissions = new ArrayList<>();
		
		Submission submission = mock(Submission.class);
		Study study = mock(Study.class);
		Citation citation = mock(Citation.class);
		StudyStatus status = mock(StudyStatus.class);
		
		when(submission.getStudy()).thenReturn(study);
		when(study.getId()).thenReturn(1L);
		when(study.getCitation()).thenReturn(citation);
		when(study.getLastModifiedDate()).thenReturn(new Date());
		when(study.getStudyStatus()).thenReturn(status);
		when(status.isPublished()).thenReturn(true);
		when(citation.getTitle()).thenReturn("Test Study Title");
		when(citation.getAuthorsCitationStyleWithoutHtml()).thenReturn("Author A, Author B. 2024. Test Study.");
		
		submissions.add(submission);
		return submissions;
	}
}
