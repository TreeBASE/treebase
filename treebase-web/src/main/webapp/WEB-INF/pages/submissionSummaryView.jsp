<%@ include file="/common/taglibs.jsp"%>
<head>
<title>Summary Information</title>
<content tag="heading">Summary  for current study</content>

<body id="submissions"/>

<fieldset>
<legend>Summary
<a href="#" class="openHelp" onclick="openHelp('submissionSummaryView')"><img class="iconButton" alt="help" src="<fmt:message key="icons.help"/>" /></a>
</legend>
Submission: <c:out value="${submissionNumber}"/>, <c:out value="${studyStatus}"/>, <a href="/treebase-web/admin/changeStudyStatus.html"> Update Status</a><br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Submission initiated:&nbsp;&nbsp;<c:out value="${initiatedDate}"/><br/>
<a href='mailto:${submission.submitter.emailAddressString}?subject=TreeBASE Submission S${submission.id}'>
	<img class="iconButton" alt="mail" src="<fmt:message key="icons.email"/>" />
	Contact Submitter
</a>
<br/>
<a href="/treebase-web/phylows/<c:out value="${submission.study.phyloWSPath}"/>?x-access-code=<c:out value="${submission.study.namespacedGUID.hashedIDString}"/>&format=html">
	<img class="iconButton" alt="link" src="<fmt:message key="icons.permalink"/>" />
	Reviewer access URL: right-click and copy me
</a>

<c:if test="${not empty citationsummary.study}">
	<c:if test="${not empty citationsummary.study.name}">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Study name:&nbsp;&nbsp;<c:out value="${citationsummary.study.name}"/>
	</c:if>
</c:if>

<br/>
<br/>

<c:if test="${empty citationsummary.title}">
 Citation information not yet entered. Click the <strong>Citation</strong> menu item on the right.<br/><br/>
</c:if>
<c:if test="${not empty citationsummary.title}">
	
	<strong><u>Citation</u></strong>
	<p>&nbsp; <c:out value="${citationsummary.citationType}"/>,&nbsp;<c:out value="${citationsummary.citationStatusDescription}"/> </p>
	<p>&nbsp;&nbsp;&nbsp;${citationsummary.authorsCitationStyle}</p>
	<br/>
		
</c:if>

<c:if test="${not empty citationsummary.abstract}">
	<strong><u>Abstract</u></strong>
	<p>&nbsp; <c:out value="${citationsummary.abstract}"/></p>
	<br/>
</c:if>
 </fieldset>

	<!-- imports & variables necessary before running analysis jsps -->
	<c:set var="editable" value="${false}" scope="request"/>
	<!-- also need studyCommand from controller -->
	
	<!-- now run analysis jsps -->
	<jsp:include page="analysisList.jsp"/>


</body>
 		