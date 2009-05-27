<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="user.management"/></title>
<content tag="heading"><fmt:message key="user.management"/></content>
<body id="admin"/>

<p>This is a simple person audit page. Search for potential duplicate person records based on selected criteria.</p>

<form method="post"  id="dataForm">

<fieldset>
<legend>Search for potential duplicate person records</legend>

<table border="0" cellpadding="3" cellspacing="3">

	<tr>
		<th align ="left">
			<input type="radio" name = "<fmt:message key="user.management.userchoice"/>" value = "firstAndLast" checked> With Same First and Last Name<br/>
			<input type="radio" name = "<fmt:message key="user.management.userchoice"/>" value = "lastOnly"> With Same Last Name<br/>
        </th>
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