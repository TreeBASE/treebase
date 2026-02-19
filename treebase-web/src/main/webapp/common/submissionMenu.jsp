<%@ include file="/common/taglibs.jsp"%>
 
<div id="sidebar">
<div id="mainMenu">
<div id="gutter">
<div id="menu">
<div id="menuDiv">
<menu:useMenuDisplayer name="ListMenu">
	<menu:displayMenu name="SubmissionHome"/>
	<menu:displayMenu name="SubmissionNotes"/>
	<menu:displayMenu name="SubmissionCitation"/>
	<menu:displayMenu name="SubmissionAuthors"/>
    <menu:displayMenu name="SubmissionUploadFile"/>
    <menu:displayMenu name="SubmissionNexusFiles"/>
    <menu:displayMenu name="SubmissionTaxa"/>
    <menu:displayMenu name="SubmissionMatrices"/>
    <menu:displayMenu name="SubmissionTrees"/>
    <menu:displayMenu name="SubmissionAnalyses"/>     
    <menu:displayMenu name="SubmissionSubmissions"/>    
 </menu:useMenuDisplayer>
</div>
 <div id="debug"></div>
</div>
</div>
</div>
</div>
<script type="text/javascript" src="/treebase-web/scripts/user/submissionSummary.js"></script>