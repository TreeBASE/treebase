<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="user.management"/></title>
<content tag="heading"><fmt:message key="user.management"/></content>
<body id="admin"/>

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle me-3 mt-1"></i>
		<div>
			<p class="mb-0">This is a simple user management page. Search for users based on selected criteria.</p>
		</div>
	</div>

	<form method="post" id="dataForm">
		<div class="card shadow-lg mb-4">
			<div class="card-header">
				<span class="fw-semibold"><i class="fa fa-search"></i> Search for users</span>
			</div>
			<div class="card-body">
				<div class="row mb-4">
					<div class="col-md-6">
						<label class="form-label fw-semibold">Search by:</label>
						<div class="d-flex flex-column gap-2 mb-3">
							<div class="form-check">
								<input class="form-check-input" type="radio" name="<fmt:message key="user.management.userchoice"/>" value="Email" id="searchEmail">
								<label class="form-check-label" for="searchEmail">Email Address</label>
							</div>
							<div class="form-check">
								<input class="form-check-input" type="radio" name="<fmt:message key="user.management.userchoice"/>" value="<fmt:message key="user.username"/>" id="searchUsername" checked>
								<label class="form-check-label" for="searchUsername"><fmt:message key="user.username"/></label>
							</div>
							<div class="form-check">
								<input class="form-check-input" type="radio" name="<fmt:message key="user.management.userchoice"/>" value="<fmt:message key="user.management.userlastname"/>" id="searchLastname">
								<label class="form-check-label" for="searchLastname"><fmt:message key="user.management.userlastname"/></label>
							</div>
						</div>
						<input type="text" name="<fmt:message key="user.management.userinfo"/>" class="form-control" placeholder="Enter search term..."/>
					</div>
				</div>

				<div class="row mb-4">
					<div class="col-md-6">
						<div class="form-check mb-2">
							<input class="form-check-input" type="radio" name="<fmt:message key="user.management.userchoice"/>" value="User Role" id="searchRole">
							<label class="form-check-label" for="searchRole">User Role:</label>
						</div>
						<select name="<fmt:message key="user.role"/>" class="form-select">
							<c:forEach var="role" items="${userRoles}">
								<option value="${role}" <c:if test="${type eq 'Associate Editor'}">selected</c:if>>
									<c:out value="${role}"/>
								</option>
							</c:forEach>
						</select>
					</div>
				</div>

				<div class="d-flex gap-2">
					<button type="submit" name="Submit" class="btn btn-primary">
						<i class="fa fa-search"></i> <fmt:message key="button.submit"/>
					</button>
					<button type="submit" name="_cancel" class="btn btn-outline-secondary">
						<fmt:message key="button.cancel"/>
					</button>
				</div>
			</div>
		</div>
	</form>
</div>