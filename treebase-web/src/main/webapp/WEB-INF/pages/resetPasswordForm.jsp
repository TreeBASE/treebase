<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="user.password.reset.form.title"/></title>
<content tag="heading"><fmt:message key="user.password.reset.form.title"/></content>

<c:url var="homepageURL" value="/login.jsp" />

<c:if test="${not empty errors}">
    <div class="error">
        <img src="<c:url value="/images/iconWarning.gif"/>"
            alt="<fmt:message key="icon.warning"/>" class="icon" />
        <c:out value="${errors}" escapeXml="false"/>
    </div>
</c:if>

<c:if test="${not empty messages}">
    <div class="message">
        <c:out value="${messages}" escapeXml="false"/>
    </div>
</c:if>

<c:if test="${not empty token}">
    <p>Please enter your new password below.</p>
    <p><strong>Password requirements:</strong> Minimum 8 characters.</p>

    <form method="post" id="resetPasswordForm">
        <input type="hidden" name="token" value="<c:out value="${token}"/>"/>
        <fieldset>
            <legend>Reset Password for <c:out value="${username}"/></legend>
            
            <table border="0" cellpadding="3">
            
            <tr>
                <th>New Password:</th>
                <td>
                    <input size="30" type="password" name="newPassword" id="newPassword" required minlength="8"/>
                </td>
            </tr>
            
            <tr>
                <th>Confirm Password:</th>
                <td>
                    <input size="30" type="password" name="confirmPassword" id="confirmPassword" required minlength="8"/>
                </td>
            </tr>
            
            <tr>
                <th></th>
                <td>
                    <input type="submit" name="Submit" value="Reset Password" onclick="return validatePasswords();"/>
                    <a href="<c:out value="${homepageURL}" />">Cancel</a>
                </td>
            </tr>
            
            </table>
        </fieldset>
    </form>

    <script type="text/javascript">
    function validatePasswords() {
        var pwd = document.getElementById('newPassword').value;
        var confirm = document.getElementById('confirmPassword').value;
        
        if (pwd.length < 8) {
            alert('Password must be at least 8 characters long.');
            return false;
        }
        
        if (pwd !== confirm) {
            alert('Passwords do not match.');
            return false;
        }
        
        return true;
    }
    </script>
</c:if>

<c:if test="${empty token}">
    <p>
        <a href="<c:url value="/passwordForm.html"/>">Request a new password reset link</a>
    </p>
    <p>
        <a href="<c:out value="${homepageURL}"/>">Return to login page</a>
    </p>
</c:if>
