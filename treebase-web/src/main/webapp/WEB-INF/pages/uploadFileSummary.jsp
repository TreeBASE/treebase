<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("carriageReturn","\n"); %>

<title><fmt:message key="upload.file.summary.title"/></title>
<content tag="heading"><fmt:message key="upload.file.summary.title"/></content>

<body id="submissions"/>

<form method="post">

<p/>
<br/>
File upload summary:<br/>
<c:choose>
  <c:when test="${uploadMatrixCount == 1}">
  One matrix uploaded<br/>
  </c:when>
  <c:when test="${uploadMatrixCount > 1}">
    ${uploadMatrixCount} matrices uploaded<br/>
  </c:when>
</c:choose>
<c:choose>
  <c:when test="${uploadTreeCount == 1}">
  One tree uploaded<br/>
  </c:when>
  <c:when test="${uploadTreeCount > 1}">
    ${uploadTreeCount} trees uploaded<br/>
  </c:when>
</c:choose>
<c:if test="${uploadMatrixCount + uploadTreeCount == 0}">
  No matrices or trees uploaded.<br/>
</c:if>


<br/><br/>
 
 	<c:if test="${not empty uploadParserLog}">
 		<c:out value="The Parser Log:"/>
 		<p/>
		<c:out value="${fn:replace(uploadParserLog, carriageReturn,'<br/>')}" escapeXml="false"/>
		<br/><br/><p/>
	</c:if>
 
 	<c:if test="${empty uploadParserLog}">
If you suspect that one or more blocks in your nexus file failed to parse properly, 
click "Show Parser Log" button to examine the parser's log file. 
<br/><br/>
		<input type="submit" name="showLog" value="<fmt:message key="button.parser.log"/>" />&nbsp;&nbsp;&nbsp;&nbsp;
	</c:if>
	<input type="submit" name="_cancel" value = "Cancel">
	
	
</form>