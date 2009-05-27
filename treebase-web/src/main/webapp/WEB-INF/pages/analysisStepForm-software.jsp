<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="analysis.step.title"/></title>
<content tag="heading"><fmt:message key="analysis.step.title"/></content>
<body id="submissions"/>

<p>Please complete the following software information used in the analysis step</p>

<spring:bind path="step.*">
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

<form method="post" onsubmit="return validateCitation(this)">
<fieldset>
<legend>Analysis Step - Software Information</legend>
<input type="hidden" name="_page" value="1"/>

<table>
    <tr>
        <th><fmt:message key="analysis.step.software.name"/>:</th>
        <td>
            <spring:bind path="step.softwareInfo.name">
            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th><fmt:message key="analysis.step.software.version"/>:</th>
        <td>
            <spring:bind path="step.softwareInfo.version">
            <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th><fmt:message key="analysis.step.software.description"/>:</th>
        <td>
            <spring:bind path="step.softwareInfo.description">
            <input size="60" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>    
  	<tr>
    	<th></th>
    	<td >
    		<input type="submit" name="_target0" value="<fmt:message key="button.previous"/>" />
	        <input type="submit" name="_target2" value="<fmt:message key="button.next"/>" />
	        <input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>" />
        </td>
    </tr>
</table>
</fieldset>
</form>

<script type="text/javascript">
</script>

