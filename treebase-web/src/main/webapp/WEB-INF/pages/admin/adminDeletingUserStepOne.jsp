<%@ include file="/common/taglibs.jsp"%>
<title><fmt:message key="user.delete"/></title>
<content tag="heading"><fmt:message key="user.delete"/></content>
<body id="admin"/>

<div class="container py-5">
	<div class="alert alert-warning d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-exclamation-triangle me-3 mt-1"></i>
		<div>
			<p class="mb-0">Please provide a user name to delete.</p>
		</div>
	</div>

	<form method="post">
		<div class="card shadow-lg mb-4">
			<div class="card-header">
				<span class="fw-semibold"><i class="fa fa-user-times"></i> Deleting a user</span>
			</div>
			<div class="card-body">
				<div class="row mb-4">
					<div class="col-md-6">
						<label class="form-label fw-semibold"><fmt:message key="user.username"/>:</label>
						<input type="text" name="username" class="form-control" maxlength="50" placeholder="Enter username..."/>
					</div>
				</div>

				<div class="d-flex gap-2">
					<button type="submit" name="Submit" class="btn btn-danger">
						<i class="fa fa-check"></i> <fmt:message key="button.submit"/>
					</button>
					<button type="submit" name="_cancel" class="btn btn-outline-secondary">
						<fmt:message key="button.cancel"/>
					</button>
				</div>
			</div>
		</div>
	</form>
</div>