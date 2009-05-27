<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="study.delete"/></title>
<content tag="heading"><fmt:message key="study.delete"/></content>
<body id="submissions"/>


<form method="post">
<input type="hidden" name="submissionid" value="${status.value}"/>

<h3>Are you sure that you want to delete submission ${submissionid}?</h3>
<strong>Pressing the Delete button will permanently erase all data associated with this submission.</strong>
 <br/>
 <br/>
 <br/>
 
 <input type="submit" name="Delete" value="Delete Study">&nbsp;&nbsp;&nbsp;&nbsp;
 <input type="submit" name="_cancel" value = "Cancel">



</form>