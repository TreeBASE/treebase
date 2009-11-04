<%--
This threw an error in the new eclipse, but the file never
existed in the first place. Perhaps we didn't validate as
stringently before? Let's see how this works if we just 
leave it out.
<%@ include file="/WEB-INF/jsp/include.jsp" %>
 --%>
 
<tiles:importAttribute/>

<table bgcolor="lightblue" cellspacing="2" cellpadding="2" border="0" width="100%">
	<tr>
		<c:if test="${not empty user.userName}">
			<td align="right">Welcome! You are logged in as <c:out value="${user.userName}"/> </td>
		</c:if>
	</tr>
</table>
