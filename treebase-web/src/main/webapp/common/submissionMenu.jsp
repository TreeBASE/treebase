<%@ include file="/common/taglibs.jsp"%>
 
<div id="sidebar">
<div id="mainMenu">
<div id="gutter">
<div id="menu">
<menu:useMenuDisplayer name="ListMenu" permissions="rolesAdapter">
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
 <div id="debug"></div>
</div>
</div>
</div>
</div>
<script type="text/javascript" src="/treebase-web/scripts/prototype/prototype-1.6.0.3.js"></script>
<script type="text/javascript" src="/treebase-web/scripts/user/submissionSummary.js"></script>