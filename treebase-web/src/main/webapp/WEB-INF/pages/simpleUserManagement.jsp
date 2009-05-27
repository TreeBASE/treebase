<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="user.management"/></title>
<content tag="heading"><fmt:message key="study.management"/></content>
<body id="admin"/>

<p>List all the specified studies submitted by a particular user</p>

<form method="post"  id="dataForm">

<fieldset>
<legend>List studies by submitter</legend>

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
        <th>Study type:</th>
        <td>
        	
        	<select name ="<fmt:message key="user.management.studytype"/>" style="width:150px"> 
        		<c:forEach var="type" items="${studyStatusTypes}">
        			<option value="${type}" <c:if test="${type eq 'Ready'}">selected</c:if> > 
        				<c:out value="${type}"/>
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