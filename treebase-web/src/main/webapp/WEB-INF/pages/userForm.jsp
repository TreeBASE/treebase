<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function checkPasswords() { 

  var x = document.getElementById("password").value;
  var y = document.getElementById("retypedpassword").value; 
 
  if(trim(x).length == 0 || trim(y).length == 0)
  {
     alert("One or both Passwords fields might be empty.");
     return false;
  }
  else if(x != y)
  {
    alert("Two passwords are not identical.");
    return false;
   } 
   return true;
} 

</script>

<title><fmt:message key="userform.title"/></title>
<body id="info" onLoad="test();"/>



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
<div class="container d-flex justify-content-center min-vh-100 ">

<form method="post" name="userForm" id="userForm" class="container mt-5" style="max-width: 600px;" onsubmit="if (document.userForm.pressedButton.value != '_cancel') return validateUser(this)">

    <input type="hidden" name="id" value="<c:out value="${user.id}"/>"/>

    <div class="card shadow-lg">
        <div class="card-header">
        <c:choose>
	<c:when test="${user.id == null}">
		<fmt:message key="create.profile"/>
	</c:when>
	<c:otherwise>
		<fmt:message key="update.profile"/>
	</c:otherwise>
</c:choose>
</div>
        <div class="card-body p-4">
            <h2 class="card-title mb-4 text-center">User profile

                <tb:helpButton topic="userForm"/>
            </h2>

            <div class="mb-3">
                <label for="user.username" class="form-label"><fmt:message key="user.username"/>:</label>
                <c:choose>
                    <c:when test="${empty user.id}">
                        <spring:bind path="user.username">
                            <input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                        </spring:bind>
                    </c:when>
                    <c:otherwise>
                        <div class="form-control-plaintext"><c:out value="${user.username}"/></div>
                    </c:otherwise>
                </c:choose>
            </div>
    
    

            <div class="mb-3">
                <label for="user.password" class="form-label"><fmt:message key="user.password"/>:</label>
                <spring:bind path="user.password">
                    <input type="password" class="form-control" name="<c:out value="${status.expression}"/>" id = "<c:out value="${status.expression}"/>" value=""/>
                    <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                </spring:bind>
                <c:if test="${user.id != null}">
                    <small class="text-muted">(Leave blank to keep current password)</small>
                </c:if>
            </div>

            <div class="mb-3">
                <label for="retypedpassword" class="form-label">Re-type Password:</label>
                <input type="password" class="form-control" name="retypedpassword" id ="retypedpassword" value=""/>
            </div>

            <div class="mb-3">
                <label for="user.firstName" class="form-label"><fmt:message key="user.firstname"/>:</label>
                <spring:bind path="user.firstName">
                    <input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                    <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                </spring:bind>
            </div>
            <div class="mb-3">
                <label for="user.middleName" class="form-label"><fmt:message key="user.middlename"/>:</label>
                <spring:bind path="user.middleName">
                    <input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                    <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                </spring:bind>
            </div>
            <div class="mb-3">
                <label for="user.lastName" class="form-label"><fmt:message key="user.lastname"/>:</label>
                <spring:bind path="user.lastName">
                    <input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                    <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                </spring:bind>
            </div>
            <div class="mb-3">
                <label for="user.phone.number" class="form-label"><fmt:message key="user.phone.number"/>:</label>
                <spring:bind path="user.phoneNumber">
                    <input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                    <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                </spring:bind>
            </div>
    
            <div class="mb-3">
                <label for="user.emailAddressString" class="form-label"><fmt:message key="user.emailaddressstring"/>:</label>
                <spring:bind path="user.emailAddressString">
                    <input type="email" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                    <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
                </spring:bind>
            </div>
    

            <div class="d-flex justify-content-end gap-2 mt-4">
                <c:choose>
                    <c:when test="${user.id == null}">
                        <input type="submit" name="Submit" value="<fmt:message key="button.register"/>" class="btn btn-primary" onClick="return checkPasswords();" />
                    </c:when>
                    <c:otherwise>
                        <input type="submit" name="Update" value="<fmt:message key="button.update"/>" class="btn btn-primary" />
                    </c:otherwise>
                </c:choose>

                <input type="hidden" name="pressedButton" value="">
                <input type="reset" name="Reset" value="<fmt:message key="button.reset"/>" class="btn btn-secondary" />
                <input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>" class="btn btn-outline-secondary" onClick="document.userForm.pressedButton.value = '_cancel';" />
            </div>

        </div>
    </div>
</form>
</div>

<v:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
