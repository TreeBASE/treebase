<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="user.management"/></title>
<content tag="heading"><fmt:message key="study.management"/></content>
<body id="admin"/>

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle me-3 mt-1"></i>
		<div>
			<p class="mb-0">List all the specified studies submitted by a particular user</p>
		</div>
	</div>

	<form method="post" id="dataForm">
		<div class="card shadow-lg mb-4">
			<div class="card-header">
				<span class="fw-semibold"><i class="fa fa-list"></i> List studies by submitter</span>
			</div>
			<div class="card-body">
				<div class="mb-4">
					<label class="form-label fw-semibold">Search by:</label>
					<div class="d-flex flex-column flex-md-row gap-3 align-items-md-center">
						<div class="form-check">
							<input class="form-check-input" type="radio" name="<fmt:message key="user.management.userchoice"/>" value="Email" id="searchByEmail">
							<label class="form-check-label" for="searchByEmail">Email Address</label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio" name="<fmt:message key="user.management.userchoice"/>" value="<fmt:message key="user.username"/>" id="searchByUsername" checked>
							<label class="form-check-label" for="searchByUsername"><fmt:message key="user.username"/></label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio" name="<fmt:message key="user.management.userchoice"/>" value="<fmt:message key="user.management.userlastname"/>" id="searchByLastname">
							<label class="form-check-label" for="searchByLastname"><fmt:message key="user.management.userlastname"/></label>
						</div>
					</div>
				</div>

				<div class="row mb-4">
					<div class="col-md-6">
						<label class="form-label fw-semibold">User Info</label>
						<input type="text" name="<fmt:message key="user.management.userinfo"/>" class="form-control" placeholder="Enter search value..."/>
					</div>
					<div class="col-md-6">
						<label class="form-label fw-semibold">Study type</label>
						<select name="<fmt:message key="user.management.studytype"/>" class="form-select">
							<c:forEach var="type" items="${studyStatusTypes}">
								<option value="${type}" <c:if test="${type eq 'Ready'}">selected</c:if>>
									<c:out value="${type}"/>
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