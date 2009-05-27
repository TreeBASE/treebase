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


<content tag="heading"><fmt:message key="userform.title"/></content>
<body id="info" onLoad="test();"/>

<c:choose>
	<c:when test="${user.id == null}">
		<fmt:message key="create.profile"/>
	</c:when>
	<c:otherwise>
		<fmt:message key="update.profile"/>
	</c:otherwise>
</c:choose>

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

<form method="post" name="userForm" id="userForm" onsubmit="if (document.userForm.pressedButton.value != '_cancel') return validateUser(this)">

<input type="hidden" name="id" value="<c:out value="${user.id}"/>"/>

<fieldset>
<legend>User Registration
<a href="#" class="openHelp" onclick="openHelp('userForm')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>

<table border="0" cellpadding="3">
	
    <tr>
        <td><label for="user.username"><fmt:message key="user.username"/>:</label></td>
        <c:choose>
        <c:when test="${empty user.id}">
        <td>
            <spring:bind path="user.username">
            <input type="text" class="textCell" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
        </c:when>
        <c:otherwise>
        	<td><c:out value="${user.username}"/></td>
        </c:otherwise>
        </c:choose>
    </tr>
    
    
    <tr>
    	<td><label for="user.password"><fmt:message key="user.password"/>:</label></td>
        <td>
            <spring:bind path="user.password">
            <input type="password" class="textCell" name="<c:out value="${status.expression}"/>" id = "<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    
    <tr>
    	<td>Re-type Password:</td>
        <td>
         
            <input type="password" class="textCell" name="retypedpassword" id ="retypedpassword"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
          
        </td>
    </tr>

    <tr>
        <td><label for="user.firstName"><fmt:message key="user.firstname"/>:</label></td>
        <td>
            <spring:bind path="user.firstName">
            <input type="text" class="textCell" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <td><label for="user.middleName"><fmt:message key="user.middlename"/>:</label></td>
        <td>
            <spring:bind path="user.middleName">
            <input type="text" class="textCell" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <td><label for="user.lastName"><fmt:message key="user.lastname"/>:</label></td>
        <td>
            <spring:bind path="user.lastName">
            <input type="text" class="textCell" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
		<td><label for="user.phone.number"><fmt:message key="user.phone.number"/>:</label></td>
        <td>
            <spring:bind path="user.phoneNumber">
            <input size=25 type="text" class="textCell" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>     
    
    <tr>
		<td><label for="user.emailAddressString"><fmt:message key="user.emailaddressstring"/>:</label></td>
        <td>
            <spring:bind path="user.emailAddressString">
            <input size=40 type="text" class="textCell" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>  
    
    <tr>
    	<td></td>
    	<td class="buttonBar">
    		<c:choose>
    		<c:when test="${user.id == null}">
            	<input type="submit" name="Submit" value="<fmt:message key="button.register"/>" onClick="return checkPasswords();" />
            </c:when>
            <c:otherwise>
	            <input type="submit" name="Update" value="<fmt:message key="button.update"/>" />
	        </c:otherwise>
	        </c:choose>
	        <input type="hidden" name="pressedButton" value="">
	        <input type="reset" name="Reset" value="<fmt:message key="button.reset"/>" />
	        <input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>" onClick="document.userForm.pressedButton.value = '_cancel';" />
           
        </td>
    </tr>
</table>
</fieldset>
</form>

<script type="text/javascript">
	function test(){
 //   	form.focusFirstElement(document.forms["userForm"]);
 //   	highlightFormElements();
    }
</script>

<v:javascript formName="user" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
