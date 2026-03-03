<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
   
<%@include file="/common/taglibs.jsp" %>

<!-- This is modified from Ashton's 2column_left_noNav.html  -->

<html xmlns="http://www.w3.org/1999/xhtml">
<head><%@ include file="/common/meta.jsp" %>
<meta name="template" content="mainTemplate"/>

<title>TreeBASE Web<decorator:title/></title>
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/styles.css'/>" />
<!-- Bootstrap 5 CSS -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<!-- jQuery - replaces Prototype.js -->
<script type="text/javascript" src="<c:url value='/scripts/jquery-3.7.1.min.js'/>"></script>
<!-- Phylotree.js stack -->
<script type="text/javascript" src="<c:url value='/scripts/d3.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/lodash.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/underscore.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/phylotree.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/phylotree.css'/>" />
<!-- End Phylotree.js stack -->
<script type="text/javascript" src="<c:url value='/scripts/common.js'/>"></script>
<decorator:head/>
</head>
	
<body id="info" onload="TreeBASE.initialize()">
<% if( isOldMSIE ){ %>
<c:import url="/common/updateBrowser.jsp"/>
<% } %>
<c:import url="/common/header.jsp"/>
	<div class="container-fluid">
	<!-- BEGIN LEFT SIDEBAR -->
	<div id="sidebarLeft"><c:import url="/common/sidebarLeft.jsp"/></div>
	<!-- BEGIN RIGHT COLUMN -->
	<div id="contentRight"><decorator:body/></div>
	</div>
	<!-- BEING FOOTER -->

	<div id="footer"><jsp:include page="/common/footer.jsp"/></div>
<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<jsp:include page="/common/googleAnalytics.jsp"/>
</body>
</html>