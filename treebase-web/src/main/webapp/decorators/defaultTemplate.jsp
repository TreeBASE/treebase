<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<!-- This is modified from Ashton Treebase_Forms/form_example.xml  -->

<%@include file="/common/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head><%@ include file="/common/meta.jsp" %>
		<meta name="template" content="defaultTemplate"/>
		<title>TreeBASE-<decorator:title/></title>
		<!-- Bootstrap 5 CSS -->
		<link href="https://cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/styles.css'/>" />
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/displaytag.css'/>" />
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/treebase.css'/>" />
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/menuExpandable.css'/>" />
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/messages.css'/>" />
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/ajaxProgress.css'/>" />
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/submissionSummary.css'/>" />
		<!-- jQuery - replaces Prototype.js -->
		<script type="text/javascript" src="<c:url value='/scripts/jquery-3.7.1.min.js'/>"></script>
		<!-- Phylotree.js stack -->
		<script type="text/javascript" src="<c:url value='/scripts/d3.min.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/scripts/lodash.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/scripts/underscore.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/scripts/phylotree.js'/>"></script>
		<link rel="stylesheet" type="text/css" href="<c:url value='/styles/phylotree.css'/>" />
		<!-- End Phylotree.js stack -->
		<script type="text/javascript" src="<c:url value='/scripts/menuExpandable.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/scripts/common.js'/>"></script>

		<!-- Following script lines have been added for DWR and they are used for now only on author page -->

		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/autocomplete.css'/>" />

		<script type="text/javascript" src="<c:url value='/dwr/engine.js'/>"> </script>
		<script type="text/javascript" src="<c:url value='/dwr/util.js'/>"></script>

		<script type="text/javascript" src="<c:url value='/dwr/interface/RemotePersonService.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/dwr/interface/RemoteSoftwareNameService.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/dwr/interface/RemoteJournalNameService.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/dwr/interface/RemoteAjaxProgressListener.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/dwr/interface/RemoteUniqueOtherAlgorithmService.js'/>"></script>

		<script type="text/javascript" src="<c:url value='/scripts/autocomplete.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/scripts/ajaxProgress.js'/>"></script>
		<script language="Javascript" type="text/javascript">
			// Configure DWR to suppress default error alerts for autocomplete
			if (typeof dwr !== 'undefined' && dwr.engine) {
				dwr.engine.setErrorHandler(function(message, ex) {
					// Silently handle DWR errors for autocomplete (e.g., empty database)
					if (typeof console !== 'undefined' && console.log) {
						console.log('DWR error (suppressed): ' + message);
					}
				});
			}
			
			function updateList(autocompleter, token) {
				RemotePersonService.findCompleteEmailAddress(token, {
					callback: function(data) { autocompleter.setChoices(data || []); },
					errorHandler: function(message, ex) { autocompleter.setChoices([]); }
				});
			}
			function nameValueSelector(tag){
				return tag;
			}
			// nameValueSelctor(tag) method is used by all the four methods related to Auto Suggestion Box
			
			function updateSoftwareNameList(autocompleter, token) {
				RemoteSoftwareNameService.findCompleteSoftwareName(token, {
					callback: function(data) { autocompleter.setChoices(data || []); },
					errorHandler: function(message, ex) { autocompleter.setChoices([]); }
				});
			}
			
			function updateJournalNameList(autocompleter, token) {
				RemoteJournalNameService.findCompleteJournalName(token, {
					callback: function(data) { autocompleter.setChoices(data || []); },
					errorHandler: function(message, ex) { autocompleter.setChoices([]); }
				});
			}
			
			function updateUniqueOtherAlgorithmList(autocompleter, token) {
				RemoteUniqueOtherAlgorithmService.findAllUniqueOtherAlgorithmDescriptions(token, {
					callback: function(data) { autocompleter.setChoices(data || []); },
					errorHandler: function(message, ex) { autocompleter.setChoices([]); }
				});
			}
			
		</script>

		<!-- DWR STUFF END -->
		<decorator:head/>
	</head>

	<body <decorator:getProperty property="body.id" writeEntireProperty="true"/> onload="TreeBASE.initialize()">
		<%-- Sticky Bootstrap header --%>
		<jsp:include page="/common/header.jsp"/>
		<% if( isOldMSIE ){ %>
		<c:import url="/common/updateBrowser.jsp"/>
		<% } %>
		<!--  BEGIN HEADER -->
		<!-- <div id="header"><jsp:include page="/common/header.jsp"/></div> -->

		<!--  show top navigation menu for a logged in user -->

		<jsp:include page="/common/nav.jsp"/>

		<!-- show submission menu bar for a logged in user after a submission is selected -->
		<% if (request.getSession().getAttribute("studyMap") != null  &&
			request.getRequestURL().indexOf("/user/submissionList.html") == -1 ) {
			%>
			<c:if test="${search != 'y' && pageContext['request'].remoteUser != null }">
				<c:import url="/common/submissionMenu.jsp"/>
			</c:if>
		<% } %>

		<div class="container">
			<h2><decorator:getProperty property="page.heading"/></h2>
			<%@ include file="/common/messages.jsp" %>
			<decorator:body/>
		</div>

			<%-- Help Panel Offcanvas --%>
			<jsp:include page="/common/helpPanel.jsp"/>

			<!-- Bootstrap 5 JS Bundle -->
			<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
			<jsp:include page="/common/googleAnalytics.jsp"/>
		</body>
	</html>