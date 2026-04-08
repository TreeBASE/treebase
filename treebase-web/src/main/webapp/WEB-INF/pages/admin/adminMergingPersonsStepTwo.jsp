<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="person.merge.confirm"/></title>
<content tag="heading"><fmt:message key="person.merge.confirm"/></content>
<body id="admin"/>

<div class="container py-5">
	<form method="post">
		<input type="hidden" name="_page" value="1"/>

		<div class="card shadow-lg mb-4 border-warning">
			<div class="card-header bg-warning">
				<span class="fw-semibold"><i class="fa fa-exclamation-triangle"></i> Confirm Person Record Merge</span>
			</div>
			<div class="card-body">
				<div class="alert alert-info mb-4" role="alert">
					<h5 class="alert-heading"><i class="fa fa-question-circle me-2"></i>${ADMIN_TEST_CONDITION}</h5>
					<p class="mb-0">If yes, then press the 'Finish' button.</p>
				</div>

				<div class="alert alert-warning mb-4" role="alert">
					<i class="fa fa-info-circle me-2"></i>
					<strong>Note:</strong> Pressing the 'Finish' button will delete the source person record and move all author/editor references and user created submissions to the target person record.
				</div>

				<div class="d-flex gap-2">
					<button type="submit" name="_target0" class="btn btn-outline-secondary">
						<i class="fa fa-arrow-left"></i> <fmt:message key="button.previous"/>
					</button>
					<button type="submit" name="_finish" class="btn btn-success">
						<i class="fa fa-check"></i> <fmt:message key="button.finish"/>
					</button>
					<button type="submit" name="_cancel" class="btn btn-outline-danger">
						<i class="fa fa-times"></i> <fmt:message key="button.cancel"/>
					</button>
				</div>
			</div>
		</div>
	</form>
</div>