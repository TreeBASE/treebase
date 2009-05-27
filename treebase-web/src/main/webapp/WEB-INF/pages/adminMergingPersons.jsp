<%@ include file="/common/taglibs.jsp"%>
<title><fmt:message key="person.merge"/></title>
<content tag="heading"><fmt:message key="person.merge"/></content>
<body id="admin"/>

<p>The person records are referenced by user accounts and citation author/editor records.
Merge the person records would affect the following:
</p>
<br>* If there are user accounts associated with the source and the target person record, 
then merge the source user account to the target user account. 
<br>* For all citation author/editor records, replace the source person with the target person record.
<br>* Delete the source person record.

<p>Please provide the source &amp; target person record ids.
</p>

<form method="post">
<input type="hidden" name="_page" value="0"/>

<fieldset>
<legend>Merging two person records</legend>

<table border="0" cellpadding="3" cellspacing="3">
	<tr>
        <th>Source <fmt:message key="person.id"/> :</th>
        <td>  	   	
        	<input type="text" name="sourcepersonid" size="20" />       
        </td>
    </tr>
    <tr>
        <th>Target <fmt:message key="person.id"/> :</th>
        <td>  	   	
        	<input type="text" name="targetpersonid" size="20" />       
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