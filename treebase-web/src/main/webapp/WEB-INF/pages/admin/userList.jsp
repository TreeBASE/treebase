<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="user.list.title"/></title>
<content tag="heading"><fmt:message key="user.list.title"/></content>
<body id="admin"/>

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle me-3 mt-1"></i>
		<div>
			<p class="mb-0">The table below shows a list of TreeBASE users based on your search.</p>
		</div>
	</div>

	<form method="post" action="<c:url value='/admin/userList.html'/>">
		<c:url var="deletePageURL" value="/admin/adminDeletingUserStepTwo.html?"/>

		<div class="card shadow-lg mb-4">
			<div class="card-header">
				<span class="fw-semibold"><i class="fa fa-users"></i> <fmt:message key="user.list.title"/></span>
			</div>
			<div class="card-body p-0">
				<div class="table-responsive">
					<display:table name="${userList}" 
								   requestURI=""
								   defaultsort="1"
								   class="table table-striped table-hover mb-0"
								   id="itemList"
								   export="true">
						
						<display:column property="id" title="User ID" 
									sortable="true"
									url="/admin/overrideUserProfile.html" paramId="id" paramProperty="id"/>
									
						<display:column property="username" title="Username" sortable="true"/>
									
						<display:column property="lastName" title="Last Name" sortable="true"/>
									
						<display:column property="firstName" title="First Name" sortable="true"/>

						<display:column property="emailAddressString" title="Email" sortable="true"/>

						<display:column property="roleDescription" title="Role" sortable="true"/>
						
						<display:column titleKey="link.delete">
							<a href="<c:out value="${deletePageURL}"/><c:out value="userid="/><c:out value="${itemList.username}"/>" class="btn btn-outline-danger btn-sm">
								<i class="fa fa-trash"></i> Delete
							</a>
						</display:column>
							
						<display:setProperty name="export.pdf" value="true"/>	
						<display:setProperty name="basic.empty.showtable" value="true"/>
						
					</display:table>
				</div>
			</div>
		</div>
	</form>
</div>