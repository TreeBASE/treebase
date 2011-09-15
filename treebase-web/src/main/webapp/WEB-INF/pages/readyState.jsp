<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="study.toready.state"/></title>
<content tag="heading"><fmt:message key="study.toready.state"/></content>
<body id="submissions"/>


<form method="post">
<%--
<c:if test="${AnalysesStatus eq false}"> 
	<p style="color:red;font-weight:bold">
		We notice that some of your taxon labels have failed to be validated against an external taxonomy. 
		Unless it is impossible to validate these labels, TreeBASE may refuse or delay the acceptance of your data.
	</p>
</c:if>
--%>
<p class="readyStateError" style="color:red; font-weight:bold; display:none;">
	Sorry! Errors in your submission prevent you from changing the status to 'Ready State' at this time. 
</p> 

<p class="readyStateError" style="display:none;">
	The items in the Tool Box on the right that are highlighted in yellow indicate that the item either has not been visited or there is an error. Hovering your mouse over the item will indicate the nature of the problem. Your submission cannot be changed to "Ready" state until all highlighted items are addressed. Please return to your submission to complete it. If you think this request is in error, please contact <a href='mailto:help@treebase.org?subject=TreeBASE Submission S<c:out value="${submissionNumber}"/>'>help</a>. 
	<br /><br />
	The most common problems include:
	<br /><br />
	(1) The submitter has not entered a citation that at a minimum must include the authors, year, title, and journal name.<br />
	(2) The submitter has "orphaned" matrices or trees: i.e., there are matrices that are not listed as inputs to at least one analysis; or trees that are not listed as outputs from at least one analysis. If your paper does not include a tree, please submit a generic neighbor joining tree to satisfy this requirement.<br />
	(3) The output tree of an analysis has taxon labels that do not match with the input matrix (i.e. there are taxon labels in the tree that are missing from the matrix or matrices that produced the tree). Preparing your submission using <a href='http://mesquiteproject.org/'>Mesquite</a>, in which both matrices and trees are part of the same Mesquite project and share a common taxon block, will avoid the problem of mismatched taxon labels.<br />
	(4) The submitter has not attempted to validate the taxon labels by clicking the "Validate Taxon Labels" button in the Taxa section. 
</p>

<p style="font-weight:bold">
	Are you sure that you want to change the status of this study to 'Ready State'?
</p> 

<p>
	Changing the status of your study to 'Ready State' means that you are ready for the review process. If yes, please press Submit,
	otherwise choose the Cancel button. Note that after you press the Submit button you will no longer be able to make any further 
	changes to your study, <b>except</b> for updating your citation information. We strongly encourage you to return to your 
	submission to update citation metadata, such as volume, issue, page numbers, or DOI, as needed.
</p>

<p>
	<input type="submit" name="Submit" value="Submit" id="submitReadyState"/>
	<input type="submit" name="_cancel" value = "Cancel"/>
</p>
</form>