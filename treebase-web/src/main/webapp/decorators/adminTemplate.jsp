<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<!-- This is modified from Ashton Treebase_Forms/form_example.xml  -->
  
<%@include file="/common/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head><%@ include file="/common/meta.jsp" %>
<meta name="template" content="adminTemplate"/>

<title>TreeBASE-<decorator:title/></title>
<!-- Bootstrap 5 CSS -->
<link href="https://cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/styles.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/displaytag.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/treebase.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/menuExpandable.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/messages.css'/>" />
<!-- Phylotree.js stack - MUST load before Prototype.js to avoid Array.prototype pollution -->
<script type="text/javascript" src="<c:url value='/scripts/d3.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/lodash.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/underscore.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/phylotree.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/phylotree.css'/>" />
<!-- End Phylotree.js stack -->
<!-- jQuery -->
<script type="text/javascript" src="<c:url value='/scripts/jquery-3.7.1.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/menuExpandable.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/common.js'/>"></script>


<decorator:head/>
</head>
	
<body <decorator:getProperty property="body.id" writeEntireProperty="true"/>>
<% if( isOldMSIE ){ %>
<c:import url="/common/updateBrowser.jsp"/>
<% } %>
	<!--  BEGIN HEADER -->
	<div id="header"><jsp:include page="/common/header.jsp"/></div>
	
	<%-- <!--  show top navigation menu for a logged in user -->
	<c:if test="${pageContext['request'].remoteUser != null}">	
		<ul id="login">	
			<li><strong>logged in as: <c:out value="${pageContext.request.remoteUser}"/></strong></li>
			<li><strong><a href="<c:url value="/logout.jsp"/>"><fmt:message key="nav.logout"/></a></strong></li>
		</ul>
		<jsp:include page="/common/nav.jsp"/>
		
	</c:if>
	 --%>

	
	<!-- BEGIN RIGHT COLUMN -->
	<div id="content">
		<div class="gutter">
		<h2><decorator:getProperty property="page.heading"/></h2>
		<%@ include file="/common/messages.jsp" %>
		<decorator:body/>
		</div>
	</div>
	
	<!-- show RHS menu for a logged in user after a submission is selected but not when display the all submissions -->
	<%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
		<c:import url="/common/adminMenu.jsp"/>
	<% } %>
	
	<!-- BEING FOOTER -->
	<div id="footer"><c:import url="/common/footer.jsp" /></div>

<%-- Help Panel Offcanvas --%>
<jsp:include page="/common/helpPanel.jsp"/>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<jsp:include page="/common/googleAnalytics.jsp"/>
</body>
</html>