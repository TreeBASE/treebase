<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="person.merge.confirm"/></title>
<content tag="heading"><fmt:message key="person.merge.confirm"/></content>
<body id="admin"/>


<form method="post">
<input type="hidden" name="_page" value="1"/>

<h3><strong>${ADMIN_TEST_CONDITION}<br/>
 If yes, then press the 'Finish' button.</strong></h3> 
 
 <p>
 Please note, pressing the 'Finish' button would delete the source person record and move all 
 author/editor references and user created submissions to the target person record.</p>

<table>
<tr>
 <td colspan="3" align="center"> 
 	<input type="submit" class="button" name="_target0" value="<fmt:message key="button.previous"/>" />
 	<input type="submit" class="button" name="_finish" value="<fmt:message key="button.finish"/>" />
 	<input type="submit" class="button" name="_cancel" value="<fmt:message key="button.cancel"/>" />
 </td>
</tr>
</table>

</form>