<%
response.setHeader("Cache-Control","no-store"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<%@ include file="/common/taglibs.jsp"%>
<%@ page contentType="text/javascript" %>
{
study:<%@ include file="json/studyToJSON.jsp" %>,
<c:set var="submission" value="${study.submission}"/>
submission:<%@ include file="json/submissionToJSON.jsp" %>
}
