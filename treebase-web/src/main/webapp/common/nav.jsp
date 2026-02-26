<%@ include file="/common/taglibs.jsp"%>


<nav class="navbar navbar-expand-lg navbar-light bg-light">
	<div class="container-fluid">
		<ul class="navbar-nav me-auto mb-2 mb-lg-0">
			<li class="nav-item">
				<a class="nav-link" href="<c:url value="/user/updateProfile.html"/>"><fmt:message key="nav.personalinfo"/></a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="<c:url value="/user/submissionList.html"/>"><fmt:message key="nav.submissions"/></a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="<c:url value="/search/studySearch.html"/>"><fmt:message key="nav.search.treebase"/></a>
			</li>
			<% if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){ %>
			<li class="nav-item">
				<a class="nav-link" href="<c:url value="/admin/administrationPage.html"/>"><fmt:message key="nav.admin"/></a>
			</li>
			<% } %>
			<li class="nav-item">
				<a class="nav-link" href="<c:url value="/home.html"/>"><fmt:message key="nav.home"/></a>
			</li>
		</ul>
	</div>
</nav>
