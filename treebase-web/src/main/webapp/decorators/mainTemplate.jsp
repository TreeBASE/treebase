<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
   
<%@include file="/common/taglibs.jsp" %>

<!-- This is modified from Ashton's 2column_left_noNav.html  -->

<html xmlns="http://www.w3.org/1999/xhtml">
<head><%@ include file="/common/meta.jsp" %>
<title>TreeBASE Web<decorator:title/></title>
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/styles.css'/>" />
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/menuExpandable2.css'/>" />
<script type="text/javascript" src="<c:url value='/scripts/prototype/prototype-1.6.0.3.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/common.js'/>"></script>
<decorator:head/>
</head>
	
<body id="info" onload="TreeBASE.initialize()">
<% if( isOldMSIE ){ %>
<c:import url="/common/updateBrowser.jsp"/>
<% } %>
<!-- BEGIN WRAP -->
<div id="wrap">
	<!--  BEGIN HEADER -->
	<div id="header"><c:import url="/common/header.jsp"/></div>
	<!-- BEGIN LEFT SIDEBAR -->
	<div id="sidebarLeft"><c:import url="/common/sidebarLeft.jsp"/></div>
	<!-- BEGIN RIGHT COLUMN -->
	<div id="contentRight"><decorator:body/></div>
	<!-- BEING FOOTER -->
	<div id="footer"><jsp:include page="/common/footer.jsp"/></div>
</div> <!-- END WRAP -->
<jsp:include page="/common/googleAnalytics.jsp"/>
</body>
</html>