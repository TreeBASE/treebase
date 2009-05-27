<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="user.management"/></title>
<content tag="heading"><fmt:message key="user.management"/></content>
<body id="admin"/>

<p>This is a simple user management page. Search for users based on selected criteria.</p>

<form method="post"  id="dataForm">

<fieldset>
<legend>Search for users</legend>

<table border="0" cellpadding="3" cellspacing="3">

	<tr>
		<th align ="left">
			<input type="radio" name = "<fmt:message key="user.management.userchoice"/>" value = "Email"> Email Address:<br/>
			<input type="radio" name = "<fmt:message key="user.management.userchoice"/>" value = "<fmt:message key="user.username"/>" checked> <fmt:message key="user.username"/>:<br/>
        	<input type="radio" name = "<fmt:message key="user.management.userchoice"/>" value = "<fmt:message key="user.management.userlastname"/>"> <fmt:message key="user.management.userlastname"/>:
        </th>
        <td>  	   	
        	<input type="text" name= "<fmt:message key="user.management.userinfo"/>"  size = 20/>       
        </td>
    </tr>
    
    <tr>
        <th align ="left">
        <input type="radio" name = "<fmt:message key="user.management.userchoice"/>" value = "User Role"> User Role:
        </th>
        <td>
         	<select name ="<fmt:message key="user.role"/>" style="width:150px"> 
        		<c:forEach var="role" items="${userRoles}">
        			<option value="${role}" <c:if test="${type eq 'Associate Editor'}">selected</c:if> > 
        				<c:out value="${role}"/>
        			</option>
        		</c:forEach>
        	</select>
        	
        </td>
    </tr>
       
  	<tr>
    	<th></th>
    	<td>
	        <input type="submit" name="Submit" value="<fmt:message key="button.submit"/>" />
	        <!--input type="reset" name="Reset" value="<fmt:message key="button.reset"/>" /-->
	        <input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>"/>
        </td>
    </tr>
  	
</table>
</fieldset>
</form>