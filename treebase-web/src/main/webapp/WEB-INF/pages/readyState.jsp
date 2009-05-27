<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="study.toready.state"/></title>
<content tag="heading"><fmt:message key="study.toready.state"/></content>
<body id="submissions"/>


<form method="post">
<c:if test="${AnalysesStatus eq false}"> 
	<p style="color: red;"><b>You cannot change the Status of the study because Taxon Labels for analyzed data in Analysis Step cannot be validated.</b></p>
</c:if>
<h3><strong>Are you sure that you want to change the status of this  particular study to 'Ready State'?<br/>
 It means, you are ready for the review process. If yes, then press the Submit button,<br/> 
 else, choose the Cancel button. </strong></h3> 
 <p>
 Please note, pressing the Submit button will <strong>disallow you</strong> to make any further changes to this study.<br/>
However, it will be available in 'READ ONLY' mode.</p>
 
<c:if test="${AnalysesStatus eq true}"> <input type="submit" name="Submit" value="Submit"></c:if>&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="_cancel" value = "Cancel">

</form>