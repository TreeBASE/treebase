<!DOCTYPE html>
<%@include file="/common/taglibs.jsp" %>

<html lang="en">
<head>
<%@ include file="/common/meta.jsp" %>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>TreeBASE Web<decorator:title/></title>
<!-- Bootstrap 5 -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<!-- TreeBASE styles -->
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/treebase-bootstrap.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/displaytag.css'/>" />
<!-- jQuery - required by custom JS -->
<script type="text/javascript" src="<c:url value='/scripts/jquery-3.7.1.min.js'/>"></script>
<!-- Bootstrap 5 JS bundle (includes Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc4s9bIOgUxi8T/jzmhkqOlEqJF4hSUZVA63r/sVMxKl" crossorigin="anonymous"></script>
<script type="text/javascript" src="<c:url value='/scripts/common.js'/>"></script>
<decorator:head/>
</head>

<body id="info" onload="TreeBASE.initialize()">
<% if( isOldMSIE ){ %>
<c:import url="/common/updateBrowser.jsp"/>
<% } %>

<!-- Bootstrap Navbar -->
<nav class="navbar treebase-navbar">
	<div class="container-fluid">
		<jsp:include page="/common/header.jsp"/>
	</div>
</nav>

<!-- Full-width content -->
<div class="container-fluid">
	<div class="row">
		<main class="col-12 ps-4" id="contentAll">
			<decorator:body/>
		</main>
	</div>
</div>

<footer class="treebase-footer">
	<jsp:include page="/common/footer.jsp"/>
</footer>
</body>
</html>