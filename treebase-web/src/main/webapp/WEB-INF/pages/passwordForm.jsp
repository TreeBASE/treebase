<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="user.password.title"/></title>
<content tag="heading"><fmt:message key="user.password.title"/></content>

<p>Please fill out either the user name or the email address on file. If an account exists, we will send a password reset link to the registered email address.</p>
<p><strong>Note:</strong> For security reasons, we never send passwords via email. You will receive a link to create a new password.</p>

<c:url var="homepageURL" value="/login.html" />

<spring:bind path="user.*">
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

<form method="post" id="passwordForm">
<input type="hidden" name="id" value="${status.value}"/>
<fieldset>
	<legend>Password Reset</legend>
	
	<table border="0" cellpadding="3">
	
	<tr>
        <th><fmt:message key="user.username"/>:</th>
        <td>
            <spring:bind path="user.username">
            <input size="20" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
        <td>
        	<a href="<c:out value="${homepageURL}" />" >TreeBASE home page</a>
        </td>
    </tr>

	<tr>
        <th><fmt:message key="user.emailaddressstring"/>:</th>
        <td>
            <spring:bind path="user.tmpEmailAddress">
            <input size="40" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    
    <tr>
    	<th></th>
    	<td>
 			<input type="submit" name="Submit" value="Request Password Reset" />
	        <input type="reset" name="Reset" value="<fmt:message key="button.reset"/>" />
        </td>
    </tr>
    
    </table>
</fieldset>
</form>
