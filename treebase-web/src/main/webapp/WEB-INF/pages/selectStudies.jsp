<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="user.management"/></title>
<content tag="heading"><fmt:message key="select.studies"/></content>
<body id="admin"/>

<p>Select the type of studies you want to view.</p>

<form method="post"  id="dataForm">

<fieldset>
<legend>Select Studies</legend>

<table border="0" cellpadding="3" cellspacing="3">

	
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
	        <input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>"/>
        </td>
    </tr>
  	
</table>
</fieldset>
</form>