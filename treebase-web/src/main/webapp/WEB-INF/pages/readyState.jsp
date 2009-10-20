<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="study.toready.state"/></title>
<content tag="heading"><fmt:message key="study.toready.state"/></content>
<body id="submissions"/>


<form method="post">
<c:if test="${AnalysesStatus eq false}"> 
	<p style="color:red;font-weight:bold">
		We notice that some of your taxon labels have failed to be validated against an external taxonomy. 
		Unless it is impossible to validate these labels, TreeBASE may refuse or delay the acceptance of your data.
	</p>
</c:if>
<p style="font-weight:bold">
	Are you sure that you want to change the status of this study to 'Ready State'?
	This means that you are ready for the review process. If yes press Submit,
	else choose the Cancel button.
</p> 
<p>
	Please note: after you press the Submit button you will no longer be able to make any further changes to this study.
	The study will be available in 'READ ONLY' mode.
</p>
 
<input type="submit" name="Submit" value="Submit"/>
<input type="submit" name="_cancel" value = "Cancel"/>

</form>