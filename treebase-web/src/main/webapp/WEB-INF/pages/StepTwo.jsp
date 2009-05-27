<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="user.delete"/></title>
<content tag="heading"><fmt:message key="user.delete"/></content>
<body id="submissions"/>


<form method="post">
<input type="hidden" name="_page" value="1"/>

<h3><strong>${ADMIN_TEST_CONDITION}<br/>
 If yes, then press the 'Finish' button.</strong></h3> 
 
 <p>
 Please note, pressing the 'Finish' button will move all Submissions, Phylotrees, Matrices, and everything<br/>
 else related to Source-User to Target-User.</p>
<tr>
 <td colspan=3 align="center"> 
 	<input type="submit" class="button" name="_target0" value="<fmt:message key="button.previous"/>" />
 	<input type="submit" class="button" name="_finish" value="<fmt:message key="button.finish"/>" />
 	<input type="submit" class="button" name="_cancel" value="<fmt:message key="button.cancel"/>" />
 </td>
</tr>

</form>