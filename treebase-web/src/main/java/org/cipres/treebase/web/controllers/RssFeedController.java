package org.cipres.treebase.web.controllers;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cipres.treebase.TreebaseUtil;
import org.cipres.treebase.domain.study.Citation;
import org.cipres.treebase.domain.study.Study;
import org.cipres.treebase.domain.study.Submission;
import org.cipres.treebase.domain.study.SubmissionService;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * Controller for generating an RSS 2.0 feed of recent TreeBASE studies.
 * This replaces the previous PHP-based Feed2JS solution that relied on
 * external services (Yale httpd and Yahoo pipes).
 * 
 * @author TreeBASE Team
 */
public class RssFeedController implements Controller {

	private static final int DEFAULT_LIMIT = 20;
	private static final String RSS_CONTENT_TYPE = "application/rss+xml; charset=UTF-8";
	
	private SubmissionService mSubmissionService;

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		// Parse the limit parameter, default to 20
		int limit = DEFAULT_LIMIT;
		String limitParam = request.getParameter("limit");
		if (limitParam != null) {
			try {
				limit = Integer.parseInt(limitParam);
				if (limit <= 0) {
					limit = DEFAULT_LIMIT;
				} else if (limit > 100) {
					limit = 100; // Cap at 100 for performance
				}
			} catch (NumberFormatException e) {
				// Use default
			}
		}
		
		Collection<Submission> submissions = mSubmissionService.findRecentPublishedSubmissions(limit);
		
		response.setContentType(RSS_CONTENT_TYPE);
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		writeRssFeed(out, submissions);
		out.flush();
		
		return null;
	}
	
	/**
	 * Writes the RSS 2.0 feed to the output.
	 */
	private void writeRssFeed(PrintWriter out, Collection<Submission> submissions) {
		String siteUrl = TreebaseUtil.getSiteUrl();
		SimpleDateFormat rfc822Format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
		rfc822Format.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<rss version=\"2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\">");
		out.println("  <channel>");
		out.println("    <title>TreeBASE - Recent Studies</title>");
		out.println("    <link>" + escapeXml(siteUrl) + "</link>");
		out.println("    <description>Recent phylogenetic studies published in TreeBASE</description>");
		out.println("    <language>en-us</language>");
		out.println("    <lastBuildDate>" + rfc822Format.format(new Date()) + "</lastBuildDate>");
		out.println("    <atom:link href=\"" + escapeXml(siteUrl) + "rss.xml\" rel=\"self\" type=\"application/rss+xml\"/>");
		
		for (Submission submission : submissions) {
			Study study = submission.getStudy();
			if (study == null) {
				continue;
			}
			
			writeRssItem(out, study, siteUrl, rfc822Format);
		}
		
		out.println("  </channel>");
		out.println("</rss>");
	}
	
	/**
	 * Writes a single RSS item for a study.
	 */
	private void writeRssItem(PrintWriter out, Study study, String siteUrl, SimpleDateFormat dateFormat) {
		Citation citation = study.getCitation();
		
		String title = "";
		String description = "";
		String link = siteUrl + "search/study/summary.html?id=" + study.getId();
		
		if (citation != null) {
			title = citation.getTitle();
			if (title == null) {
				title = "";
			}
			String citationText = citation.getAuthorsCitationStyleWithoutHtml();
			if (citationText != null && !citationText.isEmpty()) {
				description = citationText;
			}
		}
		
		// Fallback to study name if no citation title
		if (title.isEmpty() && study.getName() != null) {
			title = study.getName();
		}
		
		// Use accession number as last resort
		if (title.isEmpty()) {
			title = "Study S" + study.getId();
		}
		
		out.println("    <item>");
		out.println("      <title>" + escapeXml(title) + "</title>");
		out.println("      <link>" + escapeXml(link) + "</link>");
		
		if (!description.isEmpty()) {
			out.println("      <description>" + escapeXml(description) + "</description>");
		}
		
		// Use GUID
		out.println("      <guid isPermaLink=\"true\">" + escapeXml(link) + "</guid>");
		
		// Publication date
		Date pubDate = study.getLastModifiedDate();
		if (pubDate == null) {
			pubDate = study.getReleaseDate();
		}
		if (pubDate != null) {
			out.println("      <pubDate>" + dateFormat.format(pubDate) + "</pubDate>");
		}
		
		out.println("    </item>");
	}
	
	/**
	 * Escapes special XML characters.
	 */
	private String escapeXml(String input) {
		if (input == null) {
			return "";
		}
		return input
			.replace("&", "&amp;")
			.replace("<", "&lt;")
			.replace(">", "&gt;")
			.replace("\"", "&quot;")
			.replace("'", "&apos;");
	}

	public SubmissionService getSubmissionService() {
		return mSubmissionService;
	}

	public void setSubmissionService(SubmissionService pSubmissionService) {
		mSubmissionService = pSubmissionService;
	}
}
