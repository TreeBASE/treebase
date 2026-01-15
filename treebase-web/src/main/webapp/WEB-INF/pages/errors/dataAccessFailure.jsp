<%@ include file="/common/taglibs.jsp" %>
<%@ page import="org.slf4j.Logger, org.slf4j.LoggerFactory" %>

<title>Data Access Error</title>
<content tag="heading">Data Access Failure</content>

<p>
    <c:out value="${requestScope.exception.message}"/>
</p>

<p>
    <strong>Note:</strong> The full error details are shown below and have been logged to the server for review.
    If this error persists, please contact the system administrator.
</p>

<details>
    <summary>Show full error details</summary>
    <pre style="white-space: pre-wrap; word-wrap: break-word; background-color: #f5f5f5; padding: 10px; border: 1px solid #ddd; overflow-x: auto;"><%
Logger logger = LoggerFactory.getLogger("org.cipres.treebase.web.errors");
Exception ex = (Exception) request.getAttribute("exception");
if (ex != null) {
    // Log the exception to server logs for administrator review
    logger.error("Data access failure encountered", ex);
    
    // Print the stack trace to the page using try-with-resources
    try (java.io.StringWriter sw = new java.io.StringWriter();
         java.io.PrintWriter pw = new java.io.PrintWriter(sw)) {
        ex.printStackTrace(pw);
        pw.flush();
        out.print(org.springframework.web.util.HtmlUtils.htmlEscape(sw.toString()));
    } catch (java.io.IOException ioEx) {
        logger.error("Error writing exception details", ioEx);
        out.print("Error displaying exception details.");
    }
} else {
    out.print("No exception details available.");
}
%></pre>
</details>

<a href="/user/submissionList.html" onclick="history.back();return false">&#171; Back</a>
