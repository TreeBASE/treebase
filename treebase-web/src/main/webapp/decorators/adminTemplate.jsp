<!DOCTYPE html>
<%@include file="/common/taglibs.jsp" %>

<html lang="en" xml:lang="en">
<head>
<%@ include file="/common/meta.jsp" %>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>TreeBASE-<decorator:title/></title>
<!-- Bootstrap 5 -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<!-- TreeBASE styles -->
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/treebase-bootstrap.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/treebase.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/displaytag.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/submissionSummary.css'/>" />
<!-- jQuery - required by DWR and custom JS -->
<script type="text/javascript" src="<c:url value='/scripts/jquery-3.7.1.min.js'/>"></script>
<!-- Bootstrap 5 JS bundle (includes Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc4s9bIOgUxi8T/jzmhkqOlEqJF4hSUZVA63r/sVMxKl" crossorigin="anonymous"></script>
<!-- Phylotree.js stack - MUST load before Prototype.js to avoid Array.prototype pollution -->
<script type="text/javascript" src="<c:url value='/scripts/d3.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/lodash.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/underscore.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/phylotree.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/phylotree.css'/>" />
<!-- End Phylotree.js stack -->
<script type="text/javascript" src="<c:url value='/scripts/common.js'/>"></script>
<decorator:head/>
</head>

<body <decorator:getProperty property="body.id" writeEntireProperty="true"/>>
<% if( isOldMSIE ){ %>
<c:import url="/common/updateBrowser.jsp"/>
<% } %>

<!-- Bootstrap Navbar -->
<nav class="navbar navbar-expand-lg treebase-navbar">
	<div class="container-fluid">
		<jsp:include page="/common/header.jsp"/>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#adminNavbar" aria-controls="adminNavbar" aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="adminNavbar">
			<c:if test="${pageContext['request'].remoteUser != null}">
				<jsp:include page="/common/nav.jsp"/>
			</c:if>
			<div class="ms-auto d-flex align-items-center navbar-login-text">
				<c:if test="${pageContext['request'].remoteUser != null}">
					<span class="text-white me-2">logged in as: <strong><c:out value="${pageContext.request.remoteUser}"/></strong></span>
					<a class="btn btn-outline-light btn-sm" href="<c:url value="/logout.jsp"/>"><fmt:message key="nav.logout"/></a>
				</c:if>
			</div>
		</div>
	</div>
</nav>

<!-- Main content -->
<div class="container-fluid">
	<div class="row">
		<main class="col-12 col-md-9" id="content">
			<div class="content-gutter">
				<h2 class="page-heading"><decorator:getProperty property="page.heading"/></h2>
				<%@ include file="/common/messages.jsp" %>
				<decorator:body/>
			</div>
		</main>

		<!-- Admin sidebar -->
		<%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
			<aside class="col-12 col-md-3" id="sidebar">
				<c:import url="/common/adminMenu.jsp"/>
			</aside>
		<% } %>
	</div>
</div>

<footer class="treebase-footer">
	<c:import url="/common/footer.jsp"/>
</footer>

<jsp:include page="/common/googleAnalytics.jsp"/>
</body>
</html>