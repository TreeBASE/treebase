<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="admin.message"/></title>
<content tag="heading"><fmt:message key="admin.message"/></content>
<body id="admin"/>

<div class="container py-5">
	<c:if test="${not empty MESSAGE_TO_ADMINISTRATOR}">
		<div class="alert alert-success d-flex align-items-start" role="alert">
			<i class="fa fa-check-circle me-3 mt-1 fa-2x"></i>
			<div>
				<h5 class="alert-heading mb-2">Action Completed</h5>
				<p class="mb-0">${MESSAGE_TO_ADMINISTRATOR}</p>
			</div>
		</div>
	</c:if>
</div>