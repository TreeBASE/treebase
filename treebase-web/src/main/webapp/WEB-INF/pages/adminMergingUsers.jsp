<%@ include file="/common/taglibs.jsp"%>
<title><fmt:message key="user.merge"/></title>
<content tag="heading"><fmt:message key="user.merge"/></content>
<body id="admin"/>

<p>Please provide source &amp; target user names.</p>

<form method="post">
<input type="hidden" name="_page" value="0"/>

<fieldset>
<legend>Merging two users</legend>

<table border="0" cellpadding="3" cellspacing="3">
	<tr>
        <th>Source <fmt:message key="user.username"/> :</th>
        <td>  	   	
        	<input type="text" name="sourceusername" size="20"/>       
        </td>
    </tr>
    <tr>
        <th>Target <fmt:message key="user.username"/> :</th>
        <td>  	   	
        	<input type="text" name="targetusername" size="20"/>       
        </td>
    </tr>               
  	<tr>
    	<th></th>
    	<td>
	         <input type="submit" name="_target1" value="<fmt:message key="button.next"/>" />
	        <input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>"/>
        </td>
    </tr>  	
</table>
</fieldset>
</form>