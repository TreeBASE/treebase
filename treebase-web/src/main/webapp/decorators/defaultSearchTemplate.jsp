<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<!-- This is modified from Ashton Treebase_Forms/form_example.xml  -->

<%@include file="/common/taglibs.jsp" %>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
	<head><%@ include file="/common/meta.jsp" %>
		<meta name="template" content="defaultSearchTemplate"/>
		<title>TreeBASE Search-<decorator:title/></title>
		<link href="https://cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css" rel="stylesheet">
		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/styles.css'/>" />
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/displaytag.css'/>" />
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/treebase.css'/>" />
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/menuExpandable.css'/>" />
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/messages.css'/>" />
		<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/ajaxProgress.css'/>" />
		<!-- Phylotree.js stack - MUST load before Prototype.js to avoid Array.prototype pollution -->
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
		<script type="text/javascript">
			//<![CDATA[
			TreeBASE.register(
			function() {
				if ( document.cookie.length > 0 )	{
					var c_name = 'citeuser';
					var c_start = document.cookie.indexOf(c_name + '=');
					if ( c_start != -1 ) {
						c_start = c_start + c_name.length + 1;
						var c_end = document.cookie.indexOf(';', c_start);
						if ( c_end == -1 ) {
							c_end=document.cookie.length;
						}
						var citeuser = document.cookie.substring(c_start,c_end);
						$('citeuser').value = citeuser;
					}
				}
			}
			);
			function makeLink(points) {
				var address = '';
				for ( var i = 0; i < points.length; i++ ) {
					if ( points[i] != null ) {
						address += "&#" + points[i] + ";";
					}
				}
				var link = '<a href="mailto:' + address + '?subject=From Treebase-2 Community">';
				link += '<img class="iconButton" src="<fmt:message key="icons.email"/>" alt="Email"/> ';
				link += address + '</a>';
				return link;
			}
			function citeulike() {
				var citeuser = $('citeuser').value;
				var citeform = $('citeulike');
				if ( citeuser != null && citeuser != 'enter your citeulike user name' ) {
					document.cookie='citeuser='+citeuser;
					var bibtex = $('bibtex').textContent;
					var pasted = $('pasted');
					pasted.value = bibtex;
					citeform.action = 'http://www.citeulike.org/profile/' + citeuser + '/import_do';
					citeform.submit();
				}
				else {
					alert('Please enter your citeulike user name first!');
					$('citeuser').style.display = 'inline';
				}
			}
			
			function connotea() {
				$('connotea').submit();
			}
			
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
			
			function toggle_visibility(id) {
				var e = document.getElementById(id);
				if(e.style.display == 'block')
				e.style.display = 'none';
				else
				e.style.display = 'block';
			}
			
			//]]>
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
		<jsp:include page="/common/nav.jsp"/>
		<jsp:include page="/common/nav-search.jsp"/>

		<!-- <jsp:include page="/common/search-topnav.jsp"/> -->

		<!-- BEGIN RIGHT COLUMN -->
		<div class="container">
			<c:if test="${not empty page.heading}">
				<h2><decorator:getProperty property="page.heading"/></h2>
			</c:if>	
			<%@ include file="/common/messages.jsp" %>
			<decorator:body/>
		</div>
		<%--
		<c:import url="/common/searchMenuRight.jsp"/>
		<c:import url="/common/searchSummaryMenuRight.jsp"/>
		--%>


		<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
		<jsp:include page="/common/googleAnalytics.jsp"/>
	</body>
</html>