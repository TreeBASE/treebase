<%@page import="org.cipres.treebase.TreebaseUtil"%>
<% String purlBase = TreebaseUtil.getPurlBase(); %>
<%@ include file="/common/taglibs.jsp"%>

<head>
<title>Summary Information</title>
<content tag="heading">Summary  for current study</content>

<body id="submissions"/>

<fieldset>
<legend>Summary
<a href="#" class="openHelp" onclick="openHelp('submissionSummaryView')"><img class="iconButton" alt="help" src="<fmt:message key="icons.help"/>" /></a>
</legend>
Submission: <c:out value="${submissionNumber}"/>, <c:out value="${studyStatus}"/>
<%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
, <a href="/treebase-web/admin/changeStudyStatus.html"> Update Status</a>
<%}%>
<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Submission initiated:&nbsp;&nbsp;<c:out value="${initiatedDate}"/><br/>

<br/>
<a href='mailto:${submission.submitter.emailAddressString}?subject=TreeBASE Submission S${submission.id}'>
	<img class="iconButton" alt="mail" src="<fmt:message key="icons.email"/>" />
	Contact Submitter
</a>
<br/>
<a href='mailto:help@treebase.org?subject=TreeBASE Submission S${submission.id}'>
	<img class="iconButton" alt="mail" src="<fmt:message key="icons.email"/>" />
	Contact TreeBASE Help 
</a>
<br/>
<br/>
<a href="<c:out value="${submission.study.phyloWSPath.purl}"/>">
	<img class="iconButton" alt="link" src="<fmt:message key="icons.weblink"/>" />
	Study Accession URL:<br/>
	<c:out value="${submission.study.phyloWSPath.purl}"/>
</a>
<div><strong>You can cite this URL in your manuscript. It will become the permanent and resolvable resource locator after your submission has been approved and the data are made public.</strong></div>
<br/>
<a href="<c:out value="${submission.study.phyloWSPath.purl}"/>?x-access-code=<c:out value="${submission.study.namespacedGUID.hashedIDString}"/>&format=html">
	<img class="iconButton" alt="link" src="<fmt:message key="icons.weblink"/>" />
	Reviewer access URL:<br/>
	<c:out value="${submission.study.phyloWSPath.purl}"/>?x-access-code=<c:out value="${submission.study.namespacedGUID.hashedIDString}"/>&format=html
</a>
<div><strong>You can copy and send this URL to you journal editor to provide reviewers with limited, read-only access to your data, even if your submission has not yet been approved and the data are not yet public.</strong></div>
<br/>
<c:if test="${not empty citationsummary.study}">
	<c:if test="${not empty citationsummary.study.name}">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Study name:&nbsp;&nbsp;<c:out value="${citationsummary.study.name}"/>
	</c:if>
</c:if>

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
 		