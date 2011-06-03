<?xml version="1.0" encoding="UTF-8"?>
<%@ include file="/common/taglibs.jsp"%>
<% response.setContentType("application/xml; charset=UTF-8"); %>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
	<c:forEach var="url" items="${phyloURL}" varStatus="status">
	<url>
		<loc><c:out value="${url}"/></loc>
	</url>
	</c:forEach>
</urlset>