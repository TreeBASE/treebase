<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="study.title"/></title>
<content tag="heading"><fmt:message key="study.update.title"/></content>
<body id="submissions"/>

<p>You have selected the following submission to update</p>

<spring:bind path="study.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">	
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>

<form method="post">
<input type="hidden" name="id" value="${status.value}"/>

<fieldset>
<legend>Submission to Update</legend>

<table border="0" cellpadding="5" cellspacing="5">

	<tr>
		<th><fmt:message key="study.id"/></th>
		<td><c:out value="${study.id}"/></td>
	</tr>
	<tr>
		<th><fmt:message key="study.status"/></th>
		<td><c:out value="${study.studyStatus.description}"/></td>
	</tr>
	
	<tr>
        <th><fmt:message key="study.name"/>:</th>
        <td>
            <spring:bind path="study.name">
            <input size="50" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/> 
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
   	<tr>
        <th><fmt:message key="study.notes"/>:</th>
        <td>
            <spring:bind path="study.notes">
            <textarea rows="4" cols="50" name="<c:out value="${status.expression}"/>">${status.value}</textarea>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
    	<th>&nbsp;</th>
    	<td>
    		<input type="submit" name="Update" value="<fmt:message key="button.update"/>" />
    		<input type="submit" name="Delete" value="<fmt:message key="button.delete"/>" />
    	</td>
    </tr>
</table>
</fieldset>
</form>
