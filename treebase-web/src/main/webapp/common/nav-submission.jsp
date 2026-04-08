<%@ include file="/common/taglibs.jsp"%>


<!-- show submission menu bar for a logged in user after a submission is selected -->
<% if (request.getSession().getAttribute("studyMap") != null  &&
	request.getRequestURL().indexOf("/submission/submissionList.html") == -1 ) {
	%>
	<c:if test="${search != 'y' && pageContext['request'].remoteUser != null }">
		<!-- Submission Sub-Navigation Bar -->
		<nav class="navbar navbar-expand-lg navbar-light bg-light mb-3">
			<div class="container-fluid">
				<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#submissionNavbar" aria-controls="submissionNavbar" aria-expanded="false" aria-label="Toggle submission navigation">
					<span class="navbar-toggler-icon"></span>
				</button>

				<div class="collapse navbar-collapse" id="submissionNavbar">
					<ul class="navbar-nav me-auto">
						<li class="nav-item">
							<a class="nav-link" href="<c:url value='/user/summary.html'/>"><i class="fa fa-home fa-icon"></i> <fmt:message key="nav.submission.home"/></a>
						</li>

						<!-- Study Info Dropdown -->
						<li class="nav-item dropdown">
							<a class="nav-link dropdown-toggle" href="#" id="studyInfoDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
								<i class="fa fa-info-circle fa-icon"></i> Study Info
							</a>
							<ul class="dropdown-menu" aria-labelledby="studyInfoDropdown">
								<li><a class="dropdown-item" href="<c:url value='/user/studyForm.html'/>"><i class="fa fa-sticky-note fa-icon"></i> <fmt:message key="nav.submission.notes"/></a></li>
								<li><a class="dropdown-item" href="<c:url value='/user/citationForm.html'/>"><i class="fa fa-quote-left fa-icon"></i> <fmt:message key="nav.submission.citation"/></a></li>
								<li><a class="dropdown-item" href="<c:url value='/user/authorSearchForm.html'/>"><i class="fa fa-users fa-icon"></i> <fmt:message key="nav.submission.authors"/></a></li>
							</ul>
						</li>

						<!-- Data Files Dropdown -->
						<li class="nav-item dropdown">
							<a class="nav-link dropdown-toggle" href="#" id="dataFilesDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
								<i class="fa fa-database fa-icon"></i> Data Files
							</a>
							<ul class="dropdown-menu" aria-labelledby="dataFilesDropdown">
								<li><a class="dropdown-item" href="<c:url value='/user/uploadFile.html'/>"><i class="fa fa-upload fa-icon"></i> <fmt:message key="nav.submission.upload"/></a></li>
								<li><a class="dropdown-item" href="<c:url value='/user/nexusFiles.html'/>"><i class="fa fa-files-o fa-icon"></i> <fmt:message key="nav.submission.files"/></a></li>
							</ul>
						</li>

						<!-- Phylo Data Dropdown -->
						<li class="nav-item dropdown">
							<a class="nav-link dropdown-toggle" href="#" id="phyloDataDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
								<i class="fa fa-sitemap fa-icon"></i> Phylo Data
							</a>
							<ul class="dropdown-menu" aria-labelledby="phyloDataDropdown">
								<li><a class="dropdown-item" href="<c:url value='/user/taxaList.html'/>"><i class="fa fa-leaf fa-icon"></i> <fmt:message key="nav.submission.taxa"/></a></li>
								<li><a class="dropdown-item" href="<c:url value='/user/matrixList.html'/>"><i class="fa fa-th fa-icon"></i> <fmt:message key="nav.submission.matrices"/></a></li>
								<li><a class="dropdown-item" href="<c:url value='/user/treeBlockList.html'/>"><i class="fa fa-code-fork fa-icon"></i> <fmt:message key="nav.submission.trees"/></a></li>
							</ul>
						</li>

						<li class="nav-item">
							<a class="nav-link" href="<c:url value='/user/analyses.html'/>"><i class="fa fa-bar-chart fa-icon"></i> <fmt:message key="nav.submission.analyses"/></a>
						</li>
					</ul>

					<ul class="navbar-nav ms-auto">
						<li class="nav-item">
							<a class="nav-link" href="<c:url value='/submission/submissionList.html'/>"><i class="fa fa-list fa-icon"></i> <fmt:message key="nav.submission.submissions"/></a>
						</li>
					</ul>
				</div>
			</div>
		</nav>
	</c:if>
	<script type="text/javascript" src="/treebase-web/scripts/user/submissionSummary.js"></script>
	<% } %>
