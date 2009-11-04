<%--
This threw an error in the new eclipse, but the file never
existed in the first place. Perhaps we didn't validate as
stringently before? Let's see how this works if we just 
leave it out.
<%@ include file="/WEB-INF/jsp/include.jsp" %>
 --%>

<html>

<head>
	<title><tiles:getAsString name="title"/></title>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
	<!-- <link rel="stylesheet" href="<%=request.getContextPath()%>/style/main.css" type="text/css">-->
	<link rel="stylesheet" href="<c:out value="${ctx}"/>/style/main.css" type="text/css">
</head>

<body>

<table cellspacing="0" cellpadding="0" width="100%" height="100%" align="center">
	
	<tr>
		<td colspan="2"><tiles:insert attribute="header"/></td>
	</tr>
	
	
	<!-- Add this if you need a top navigation/tool bar menu -->
	
	<tr>
		<td width="20%" height="100%" valign="top"><tiles:insert attribute="menu"/></td>
		<td width="80%" height="100%" valign="top" align="center"><tiles:insert attribute="body"/></td>
	</tr>
	
	<tr>
		<td colspan="2"><tiles:insert attribute="footer"/></td>
	</tr>
	
</table>

</body>

</html>

