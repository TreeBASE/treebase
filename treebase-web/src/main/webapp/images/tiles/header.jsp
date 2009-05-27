<%@ include file="/WEB-INF/jsp/include.jsp" %>

<tiles:importAttribute/>

<table bgcolor="lightblue" cellspacing="2" cellpadding="2" border="0" width="100%">
	<tr>
		<c:if test="${not empty user.userName}">
			<td align="right">Welcome! You are logged in as <c:out value="${user.userName}"/> </td>
		</c:if>
	</tr>
</table>
