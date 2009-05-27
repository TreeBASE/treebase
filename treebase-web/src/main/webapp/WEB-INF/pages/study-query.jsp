<%@include file="/common/taglibs.jsp" %>

<title><fmt:message key="search.title"/></title>
<content tag="heading"><fmt:message key="search.title"/></content>
<body id="search"/>

<p>You have created the following search query</p>

<form method="post">
<fieldset>
	<legend>Find all studies such that</legend>
	<table border="0" cellpadding="0" cellspacing="0" align="center">
			<c:forEach var="criteria" items="${searchCommand.criterias}" varStatus="var">
			<tr>
				<td>
				(<c:out value="${criteria.attribute}"/> &nbsp;
				<c:out value="${criteria.match}"/> &nbsp;
				'<c:out value="${criteria.value}"/>' &nbsp;)
				<b><c:out value="${criteria.operator}"/></b>
				</td>
			</tr>
		</c:forEach>
	</table>
</fieldset>

<table border="0" align="center">
	<tr>
	    <td align="center">
	 		<input type="submit" name="Modify Query" value="Modify Query" />
	 		<input type="submit" name="Submit Query" value="Submit Query" />
		</td>
	</tr>
</table>

</form>
