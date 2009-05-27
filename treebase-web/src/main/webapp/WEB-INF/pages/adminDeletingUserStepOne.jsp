<%@ include file="/common/taglibs.jsp"%>
<title><fmt:message key="user.delete"/></title>
<content tag="heading"><fmt:message key="user.delete"/></content>
<body id="admin"/>

<p>Please provide a user name.</p>

<form method="post">

<fieldset>
<legend>Deleting a user</legend>

<table border="0" cellpadding="3" cellspacing="3">
	<tr>
        <th><fmt:message key="user.username"/>:</th>
        <td>  	   	
        	<input type="text" name="username" size="20" />       
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