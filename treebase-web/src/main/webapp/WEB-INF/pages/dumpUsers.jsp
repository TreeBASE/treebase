<%@ include file="/common/taglibs.jsp"%>

<h1>User table modification and dump</h1>

n=<c:out value="${n}"/><br/>

<c:if test="${not empty userTable}">
	<c:forEach var="user" items="${userTable}">
		<c:out value="${user.username}" escapeXml="false" />
		<br />
	</c:forEach>
</c:if>

	<%-- 
<h2>State: ${state}</h2>

<p>(${backing.filename}) ${backing.contents}</p>

<form method="post" id="simpleForm">

<input name="filename" value="${backing.filename }">

<input type="submit" name="Search" value="Read file" />
</form>
	
<script type="text/javascript">
function formSubmit(form) {
	form.submit();
}
</script>
--%>
