<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<!-- This is modified from Ashton Treebase_Forms/form_example.xml  -->
    
<%@include file="/common/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head><%@ include file="/common/meta.jsp" %>

<title>TreeBASE-<decorator:title/></title>
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/styles.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/displaytag.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/treebase.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/menuExpandable.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/messages.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/ajaxProgress.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/submissionSummary.css'/>" />
<script type="text/javascript" src="<c:url value='/scripts/menuExpandable.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/common.js'/>"></script>

<!-- Following script lines have been added for DWR and they are used for now only on author page -->

<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/autocomplete.css'/>" />
<script type="text/javascript" src="<c:url value='/dwr/interface/RemotePersonService.js'/>"></script>
<script type="text/javascript" src="<c:url value='/dwr/interface/RemoteSoftwareNameService.js'/>"></script>
<script type="text/javascript" src="<c:url value='/dwr/interface/RemoteJournalNameService.js'/>"></script>
<script type="text/javascript" src="<c:url value='/dwr/interface/RemoteAjaxProgressListener.js'/>"></script>
<script type="text/javascript" src="<c:url value='/dwr/interface/RemoteUniqueOtherAlgorithmService.js'/>"></script>

<script type="text/javascript" src="<c:url value='/dwr/engine.js'/>"> </script>
<script type="text/javascript" src="<c:url value='/dwr/util.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/prototype/prototype.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/prototype/prototype-1.6.0.3.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/script.aculo.us/effects.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/script.aculo.us/controls.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/autocomplete.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/ajaxProgress.js'/>"></script>
<script language="Javascript" type="text/javascript">

      	function updateList(autocompleter, token) {
      		RemotePersonService.findCompleteEmailAddress(token, function(data) { autocompleter.setChoices(data) });
        }
        function nameValueSelector(tag){
    	   return tag;
        }
// nameValueSelctor(tag) method is used by all the four methods related to Auto Suggestion Box
        
        function updateSoftwareNameList(autocompleter, token) {
	       RemoteSoftwareNameService.findCompleteSoftwareName(token, function(data) { autocompleter.setChoices(data) });
        }
        
        function updateJournalNameList(autocompleter, token) {
	       RemoteJournalNameService.findCompleteJournalName(token, function(data) { autocompleter.setChoices(data) });
        }
        
        function updateUniqueOtherAlgorithmList(autocompleter, token) {
	       RemoteUniqueOtherAlgorithmService.findAllUniqueOtherAlgorithmDescriptions(token, function(data) { autocompleter.setChoices(data) });
        }
	
</script>

 <!-- DWR STUFF END -->
<decorator:head/>
</head>
	
<body <decorator:getProperty property="body.id" writeEntireProperty="true"/> onload="TreeBASE.initialize()">
<% if( isOldMSIE ){ %>
<c:import url="/common/updateBrowser.jsp"/>
<% } %>
<!-- BEGIN WRAP -->
<div id="wrap">
	<!--  BEGIN HEADER -->
	<div id="header"><jsp:include page="/common/header.jsp"/></div>
	
	<!--  show top navigation menu for a logged in user -->
	<c:if test="${pageContext['request'].remoteUser != null}">	
		<ul id="login">
			<li><strong>logged in as: <c:out value="${pageContext.request.remoteUser}"/></strong></li>
			<li><strong><a href="<c:url value="/logout.jsp"/>"><fmt:message key="nav.logout"/></a></strong></li>
		</ul>
		<jsp:include page="/common/nav.jsp"/>
		
	</c:if>
	

	
	<!-- BEGIN RIGHT COLUMN -->
	<div id="content">
		<div class="gutter">
		<h2><decorator:getProperty property="page.heading"/></h2>
		<%@ include file="/common/messages.jsp" %>
		<decorator:body/>
		</div>
	</div>
	
	<!-- show RHS menu for a logged in user after a submission is selected but not when display the all submissions -->
	<% if (request.getSession().getAttribute("studyMap") != null  && 
		 request.getRequestURL().indexOf("/user/submissionList.html") == -1 ) { 
	%>
		<c:if test="${search != 'y' && pageContext['request'].remoteUser != null }">			
				<c:import url="/common/submissionMenu.jsp"/>
		</c:if>
	<% } %>
	
	<!-- BEING FOOTER -->
	<div id="footer"><c:import url="/common/footer.jsp" /></div>
</div> <!-- END WRAP -->
<jsp:include page="/common/googleAnalytics.jsp"/>
</body>
</html>