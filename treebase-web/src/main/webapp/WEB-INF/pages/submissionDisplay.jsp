<%@ include file="/common/taglibs.jsp" %>

<title><fmt:message key="submission.display.title"/></title>
<body id="submissions"/>

<div class="gutter">
	<h2><fmt:message key="submission.display.title"/></h2>
	<p>Below is the submission you've selected</p>
</div>

<div class="separator"></div>

<spring:bind path="submission.*"></spring:bind>


<div id="displayTable">
<form method="post">
    
<table width="100%">    
	<tr>
		<th>ID</th>
		<td><c:out value="${submission.id}"/></td>
	</tr>
	<tr>
		<th>Title</th>
		<td><c:out value="${submission.title}"/></td>
	</tr>
	<tr>
		<th>Authors</th>
		<td><c:out value="${submission.authors}"/></td>
	</tr>
	<tr>
		<th>Status</th>
		<td><c:out value="${submission.status}"/></td>
	</tr>

	<tr> <th></th>
	<c:choose>
		<c:when test="${submission.status == 'progress' }">
			<th></th>
			<td>
				<input type="submit" class="button" name="update" value="<fmt:message key="button.update"/>" />
				<input type="submit" class="button" name="delete" value="<fmt:message key="button.delete"/>" />
			</td>
		</c:when>
		<c:when test="${submission.status == 'submitted' }">
			<th></th>
			<td>
				<input type="submit" class="button" name="update" value="<fmt:message key="button.update"/>" />
			</td>
		</c:when>
	</c:choose>
	</tr>
</table>

</form>

</div>

