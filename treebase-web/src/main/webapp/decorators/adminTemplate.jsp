<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<!-- This is modified from Ashton Treebase_Forms/form_example.xml  -->

<%@include file="/common/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head><%@ include file="/common/meta.jsp" %>
		<meta name="template" content="defaultTemplate"/>
		<title>TreeBASE-<decorator:title/></title>
		<%@ include file="/decorators/decorator-head.jsp" %>
		<decorator:head/>
	</head>

	<body <decorator:getProperty property="body.id" writeEntireProperty="true"/> onload="TreeBASE.initialize()">
		<%-- Sticky Bootstrap header --%>
		<jsp:include page="/common/header.jsp"/>
		<jsp:include page="/common/nav.jsp"/>

		<%@ include file="/decorators/decorator-body.jsp" %>
		<%@ include file="/decorators/decorator-scripts.jsp" %>
	</body>
</html>