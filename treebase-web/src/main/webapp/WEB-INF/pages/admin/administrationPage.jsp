<%@ include file="/common/taglibs.jsp"%>

<title>Administration Page</title>
<content tag="heading">You are viewing the administration page.</content>
<body id="admin"/>

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle me-3 mt-1"></i>
		<div>
			<p class="mb-0">Please make choices from the menus above.</p>
		</div>
	</div>

	<div class="row g-4">
		<div class="col-md-6">
			<div class="card shadow-lg h-100">
				<div class="card-header">
					<span class="fw-semibold"><i class="fa fa-book"></i> Study Management</span>
				</div>
				<div class="card-body">
					<p>The Study Management menu allows administrators to:</p>
					<ul class="list-group list-group-flush mb-3">
						<li class="list-group-item"><i class="fa fa-eye me-2 text-primary"></i>View any user's studies/submissions</li>
						<li class="list-group-item"><i class="fa fa-check-circle me-2 text-success"></i>View all 'Ready State' studies</li>
						<li class="list-group-item"><i class="fa fa-search me-2 text-info"></i>Search submissions by Submission ID</li>
						<li class="list-group-item"><i class="fa fa-exchange me-2 text-warning"></i>Change study status</li>
					</ul>
					<p class="text-muted small mb-0">
						<i class="fa fa-lightbulb-o me-1"></i>
						To view user studies, provide a valid username. The default selection shows 'Ready' state studies, but you can change it to 'All', 'Published', or 'In Progress'.
					</p>
				</div>
			</div>
		</div>

		<div class="col-md-6">
			<div class="card shadow-lg h-100">
				<div class="card-header">
					<span class="fw-semibold"><i class="fa fa-users"></i> User Management</span>
				</div>
				<div class="card-body">
					<p>The User Management menu provides tools to:</p>
					<ul class="list-group list-group-flush mb-3">
						<li class="list-group-item"><i class="fa fa-pencil me-2 text-primary"></i>Change user information</li>
						<li class="list-group-item"><i class="fa fa-user-times me-2 text-danger"></i>Delete a user</li>
						<li class="list-group-item"><i class="fa fa-compress me-2 text-info"></i>Merge two users</li>
						<li class="list-group-item"><i class="fa fa-user-plus me-2 text-success"></i>Manage person records</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>