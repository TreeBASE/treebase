<%@ include file="/common/taglibs.jsp"%>

<c:if test ="${PEOPLE == 'Author'}">
	<title><fmt:message key="author.form.title"/></title>
	<content tag="heading"><fmt:message key="author.list.title"/></content>
</c:if>
<c:if test ="${PEOPLE == 'Editor'}">
	<title><fmt:message key="editor.form.title"/></title>
	<content tag="heading"><fmt:message key="editor.list.title"/></content>
</c:if>

<body id="submissions" onload="test();"/>

<spring:bind path="person.*">
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


<form  name="peopleForm" onsubmit="return validateAuthor(this)">

<fieldset>
<legend>New ${PEOPLE} Information
<a href="#" class="openHelp" onclick="openHelp('addPersonForm')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>
<p>Add ${PEOPLE} information for citation</p>
<table border="0" cellpadding="3">
	<tr>
        <th><fmt:message key="user.firstname"/>:</th>
        <td>
            <spring:bind path="person.firstName">
            <input class="textCell" style="width:100%" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    
        <th><fmt:message key="user.middlename"/>:</th>
        <td>
            <spring:bind path="person.middleName">
            <input class="textCell" style="width:100%" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
   
        <th><fmt:message key="user.lastname"/>:</th>
        <td>
            <spring:bind path="person.lastName">            
            <input class="textCell" style="width:100%" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th><fmt:message key="user.emailaddressstring"/>:</th>
        <td colspan="5">          
            <spring:bind path="person.emailAddressString">
              <div id="ac">
            	<input class="textCell" style="width:100%" type="text" id="<c:out value="${status.expression}"/>"  name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
            	<div id="aemailList" class="auto_complete"></div>
            	<script type="text/javascript">
				 		new Autocompleter.DWR('emailAddressString', 'aemailList', updateList,{ valueSelector: nameValueSelector, partialChars: 0 });
		
				</script>
              </div>
              <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
          
        </td>
    </tr>
    
    <tr>
       <td><input type="hidden" name="authorIds" id="authorIdsList"/> </td>
    </tr> 
    
    
    <tr>
      <%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
		<% request.setAttribute("isEditable","yes");%>
	<% } %>
  	  
  	  <c:if test="${publicationState eq 'NotReady'||isEditable eq 'yes'}">
    	<td></td>
    	<td colspan="5">
		    <input type="submit" name="Submit" value="<fmt:message key="button.submit"/>" />
    			&nbsp;&nbsp;&nbsp;
    		<input type="submit" name="Submit and Continue" value="<fmt:message key="button.submit.and.continue"/>" />
 	        <input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>" onClick ="bCancel=true"/>
    			
        </td>
       </c:if>       
    </tr>
</table>
</fieldset>


<jsp:include page="peopleMatchList.jsp"/>

</form>


<script type="text/javascript">
	function test(){
    }
</script>



<v:javascript formName="person" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
