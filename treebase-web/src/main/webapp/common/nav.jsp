<%@ include file="/common/taglibs.jsp"%>



<nav class="navbar navbar-expand-lg navbar-light bg-light">
	<div class="container-fluid">
		<ul class="navbar-nav me-auto mb-2 mb-lg-0">
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
				<a class="nav-link" href="<c:url value="/user/updateProfile.html"/>"><fmt:message key="nav.personalinfo"/></a>
			</li>
		</ul>

		<c:if test="${pageContext['request'].remoteUser != null}">
			<ul class="navbar-nav ms-auto mb-2 mb-lg-0">
				<li class="nav-item dropdown">
					<a class="nav-link dropdown-toggle" href="#" id="userMenu" role="button" data-bs-toggle="dropdown" aria-expanded="false">
						<c:out value="${pageContext.request.remoteUser}"/>
					</a>
					<ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userMenu">
						<li>
							<a class="dropdown-item" href="<c:url value="/logout.jsp"/>"><fmt:message key="nav.logout"/></a>
						</li>
					</ul>
				</li>
			</ul>
		</c:if>
	</div>
</nav>
