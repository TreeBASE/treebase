<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="delete.delete"/></title>
<content tag="heading">${deleteelementtype}</content>

<body id="submissions"/>

<form method="post">
	<p style="font-weight:bold;color:red">${generalmessage}</p><br/>
	Are you sure that you want to delete?<br/>
	${deleteid} &amp; ${objectname}<br/><br/>
	If yes, then press the delete button. <br/><br/>
 	<c:if test="${empty noidavailable}">
		<input type="submit" name="Delete" value="Delete">&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="submit" name="_cancel" value = "Cancel">
	</c:if>
</form>