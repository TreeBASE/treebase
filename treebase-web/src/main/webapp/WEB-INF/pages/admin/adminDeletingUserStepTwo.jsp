<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="user.delete"/></title>
<content tag="heading"><fmt:message key="user.delete"/></content>
<body id="admin"/>

<div class="container py-5">
	<form method="post">
		<div class="card shadow-lg mb-4 border-danger">
			<div class="card-header bg-danger text-white">
				<span class="fw-semibold"><i class="fa fa-exclamation-triangle"></i> Confirm User Deletion</span>
			</div>
			<div class="card-body">
				<div class="alert alert-danger mb-4" role="alert">
					<h5 class="alert-heading"><i class="fa fa-warning me-2"></i>${ADMIN_TEST_CONDITION}</h5>
					<p class="mb-0">If yes, then press the delete button.</p>
				</div>

				<div class="alert alert-warning mb-4" role="alert">
					<i class="fa fa-info-circle me-2"></i>
					<strong>Warning:</strong> Pressing the 'Delete User' button will permanently remove all Submissions, Phylotrees, Matrices, and everything else related to this particular user. This action cannot be undone.
				</div>

				<div class="d-flex gap-2">
					<button type="submit" name="Delete" class="btn btn-danger">
						<i class="fa fa-trash"></i> Delete User
					</button>
					<button type="submit" name="_cancel" class="btn btn-outline-secondary">
						<i class="fa fa-times"></i> Cancel
					</button>
				</div>
			</div>
		</div>
	</form>
</div>