<!DOCTYPE html>
<%@include file="/common/taglibs.jsp" %>

<html lang="en" xml:lang="en">
<head>
<%@ include file="/common/meta.jsp" %>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>TreeBASE Search-<decorator:title/></title>
<!-- Bootstrap 5 -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<!-- TreeBASE styles -->
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/treebase-bootstrap.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/treebase.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/displaytag.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/autocomplete.css'/>" />
<!-- jQuery - required by DWR and custom JS -->
<script type="text/javascript" src="<c:url value='/scripts/jquery-3.7.1.min.js'/>"></script>
<!-- Bootstrap 5 JS bundle (includes Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc4s9bIOgUxi8T/jzmhkqOlEqJF4hSUZVA63r/sVMxKl" crossorigin="anonymous"></script>
<!-- Phylotree.js stack -->
<script type="text/javascript" src="<c:url value='/scripts/d3.min.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/lodash.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/underscore.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/phylotree.js'/>"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/styles/phylotree.css'/>" />
<!-- End Phylotree.js stack -->
<script type="text/javascript" src="<c:url value='/scripts/common.js'/>"></script>

<!-- DWR for autocomplete and AJAX progress -->
<script type="text/javascript" src="<c:url value='/dwr/engine.js'/>"></script>
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
		function nameValueSelector(tag){ return tag; }

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

<decorator:head/>
</head>

<body <decorator:getProperty property="body.id" writeEntireProperty="true"/> onload="TreeBASE.initialize()">
<% if( isOldMSIE ){ %>
<c:import url="/common/updateBrowser.jsp"/>
<% } %>

<!-- Bootstrap Navbar -->
<nav class="navbar navbar-expand-lg treebase-navbar">
	<div class="container-fluid">
		<jsp:include page="/common/header.jsp"/>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#searchNavbar" aria-controls="searchNavbar" aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="searchNavbar">
			<jsp:include page="/common/search-nav.jsp"/>
			<div class="ms-auto d-flex align-items-center navbar-login-text">
				<c:choose>
					<c:when test="${pageContext['request'].remoteUser != null}">
						<span class="text-white me-2">logged in as: <strong><c:out value="${pageContext.request.remoteUser}"/></strong></span>
						<a class="btn btn-outline-light btn-sm" href="<c:url value="/logout.jsp"/>"><fmt:message key="nav.logout"/></a>
					</c:when>
					<c:otherwise>
						<a class="btn btn-outline-light btn-sm" href="<c:url value="/login.jsp"/>"><fmt:message key="nav.login"/></a>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</nav>

<!-- Main content (full width for search) -->
<div class="container-fluid">
	<div class="row">
		<main class="col-12" id="content">
			<div class="content-gutter">
				<c:if test="${not empty page.heading}">
					<h2 class="page-heading"><decorator:getProperty property="page.heading"/></h2>
				</c:if>
				<%@ include file="/common/messages.jsp" %>
				<decorator:body/>
			</div>
		</main>
	</div>
</div>

<footer class="treebase-footer">
	<c:import url="/common/footer.jsp"/>
</footer>

<jsp:include page="/common/googleAnalytics.jsp"/>
</body>
</html>