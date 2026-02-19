<%@ include file="/common/taglibs.jsp"%>

<ul class="navbar-nav me-auto" id="s-nav">
	<li class="nav-item" id="t-info"><a class="nav-link" href="<c:url value="/user/updateProfile.html"/>"><fmt:message key="nav.personalinfo"/></a></li>
	<li class="nav-item" id="t-submissions"><a class="nav-link" href="<c:url value="/user/submissionList.html"/>"><fmt:message key="nav.submissions"/></a></li>
	<li class="nav-item" id="t-search">
		<a class="nav-link active" href="<c:url value="/search/studySearch.html"/>"><fmt:message key="nav.search.treebase"/></a>
	</li>
	
	<%
		if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){
	%>
	<li class="nav-item" id="t-admin"><a class="nav-link" href="<c:url value="/admin/administrationPage.html"/>"><fmt:message key="nav.admin"/></a></li>
	<% } %>
	
	<li class="nav-item" id="t-home"><a class="nav-link" href="<c:url value="/home.html"/>"><fmt:message key="nav.home"/></a></li>
</ul>
