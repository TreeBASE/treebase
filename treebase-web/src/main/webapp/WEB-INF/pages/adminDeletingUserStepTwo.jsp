<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="user.delete"/></title>
<content tag="heading"><fmt:message key="user.delete"/></content>
<body id="submissions"/>


<form method="post">

<h3><strong>${ADMIN_TEST_CONDITION}<br/>
 If yes, then press the delete button.</strong></h3> 
 
 <p>
 Please note, pressing the 'Delete User' button will remove all Submissions, Phylotrees, Matrices, and everything<br/>
 else related to this particular user.</p><br/>
 
 <input type="submit" name="Delete" value="Delete User">&nbsp;&nbsp;&nbsp;&nbsp;
 <input type="submit" name="_cancel" value = "Cancel">



</form>