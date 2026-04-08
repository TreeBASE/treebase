<%@ include file="/common/taglibs.jsp"%>



<nav class="navbar navbar-expand-lg navbar-light bg-light">
	<ul class="navbar-nav me-auto mb-2 mb-lg-0">
		<li class="nav-item">
			<a class="nav-link" href="<c:url value="/user/submissionList.html"/>"><fmt:message key="nav.submissions"/></a>
		</li>
		<li class="nav-item">
			<a class="nav-link" href="<c:url value="/search/studySearch.html"/>"><fmt:message key="nav.search.treebase"/></a>
		</li>

		<li class="nav-item nav-divider"></li>
		<%@ include file="nav-docs.jsp"%>
		<%@ include file="nav-about.jsp"%>
		<li class="nav-item nav-divider"></li>

		<%@ include file="/common/nav-admin.jsp"%>


		<li  class="nav-item"><a class="nav-link" href="<c:url value="/dataMan.html"/>"><fmt:message key="nav.dataman"/></a></li>
		<li class="nav-item"><a class="nav-link" href="<c:url value="/journal.html"/>"><fmt:message key="nav.journals"/></a></li>
		<li class="nav-item"><a class="nav-link" href="<c:url value="/contact.html"/>"><fmt:message key="nav.contact"/></a></li>


	</ul>

	<c:choose>
		<c:when test="${pageContext['request'].remoteUser != null}">
			<ul class="navbar-nav ms-auto mb-2 mb-lg-0">
				<li class="nav-item dropdown">
					<a class="nav-link dropdown-toggle" href="#" id="userMenu" role="button" data-bs-toggle="dropdown" aria-expanded="false">
						<c:out value="${pageContext.request.remoteUser}"/>
					</a>
					<ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userMenu">
						<li>
							<a class="dropdown-item" href="<c:url value="/user/updateProfile.html"/>"><fmt:message key="nav.personalinfo"/></a>
						</li>

						<li>
							<a class="dropdown-item" href="<c:url value="/logout.jsp"/>"><fmt:message key="nav.logout"/></a>
						</li>
					</ul>
				</li>
			</ul>
		</c:when>
		<c:otherwise>
			<!-- If user is not logged in, show login link -->
			<ul class="navbar-nav ms-auto mb-2 mb-lg-0">
				<li class="nav-item">
					<a class="nav-link" href="<c:url value="/login.jsp"/>"><fmt:message key="nav.login"/></a>
				</li>
			</ul>
		</c:otherwise>
	</c:choose>
</nav>
