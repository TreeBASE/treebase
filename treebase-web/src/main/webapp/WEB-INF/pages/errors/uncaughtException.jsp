<%@ include file="/common/taglibs.jsp" %>
<%@ page import="org.slf4j.Logger, org.slf4j.LoggerFactory" %>

<title>Java Uncaught Exception</title>
<content tag="heading">Uncaught Exception Encountered</content>

<p>
    An unexpected error occurred while processing your request.
</p>

<p>
    <strong>Note:</strong> The full error details are shown below and have been logged to the server for review.
    If this error persists, please contact the system administrator.
</p>

<details>
    <summary>Show full error details</summary>
    <pre style="white-space: pre-wrap; word-wrap: break-word; background-color: #f5f5f5; padding: 10px; border: 1px solid #ddd; overflow-x: auto;"><%
try {
    Logger logger = LoggerFactory.getLogger("org.cipres.treebase.web.errors");
    java.io.StringWriter sw = new java.io.StringWriter();
    java.io.PrintWriter pw = new java.io.PrintWriter(sw);
    
    // The Servlet spec guarantees this attribute will be available
    Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception"); 

    if (exception != null) {
        Throwable rootCause = exception;
        if (exception instanceof ServletException) {
            // It's a ServletException: we should extract the root cause
            ServletException sex = (ServletException) exception;
            if (sex.getRootCause() != null) {
                rootCause = sex.getRootCause();
            }
        }
        
        // Log the exception to server logs
        logger.error("Uncaught exception encountered", rootCause);
        
        pw.println("** Exception: " + rootCause.getClass().getName());
        pw.println("** Message: " + rootCause.getMessage());
        pw.println();
        pw.println("** Stack trace:");
        rootCause.printStackTrace(pw);
    } else {
        pw.println("No error information available");
    }
    
    out.print(org.springframework.web.util.HtmlUtils.htmlEscape(sw.toString()));
} catch (Exception ex) {
    Logger logger = LoggerFactory.getLogger("org.cipres.treebase.web.errors");
    logger.error("Error while displaying exception page", ex);
    out.print("Error displaying exception details.");
}
%></pre>
</details>

<p>
    <a href="/" onclick="history.back();return false">&#171; Back</a>
</p>

