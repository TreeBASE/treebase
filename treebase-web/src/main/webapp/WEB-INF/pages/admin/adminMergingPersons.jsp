<%@ include file="/common/taglibs.jsp"%>
<title><fmt:message key="person.merge"/></title>
<content tag="heading"><fmt:message key="person.merge"/></content>
<body id="admin"/>

<div class="container py-5">
	<div class="alert alert-info mb-4" role="alert">
		<h5 class="alert-heading"><i class="fa fa-info-circle me-2"></i>About Person Record Merging</h5>
		<p>The person records are referenced by user accounts and citation author/editor records. Merging person records will affect the following:</p>
		<ul class="mb-0">
			<li>If there are user accounts associated with the source and the target person record, then merge the source user account to the target user account.</li>
			<li>For all citation author/editor records, replace the source person with the target person record.</li>
			<li>Delete the source person record.</li>
		</ul>
	</div>

	<form method="post">
		<input type="hidden" name="_page" value="0"/>
		
		<div class="card shadow-lg mb-4">
			<div class="card-header">
				<span class="fw-semibold"><i class="fa fa-compress"></i> Merging two person records</span>
			</div>
			<div class="card-body">
				<p class="text-muted mb-4">Please provide the source &amp; target person record IDs.</p>

				<div class="row mb-3">
					<div class="col-md-6">
						<label class="form-label fw-semibold">Source <fmt:message key="person.id"/>:</label>
						<input type="text" name="sourcepersonid" class="form-control" maxlength="20" placeholder="Enter source person ID..."/>
						<small class="text-muted">This record will be deleted after merge</small>
					</div>
				</div>

				<div class="row mb-4">
					<div class="col-md-6">
						<label class="form-label fw-semibold">Target <fmt:message key="person.id"/>:</label>
						<input type="text" name="targetpersonid" class="form-control" maxlength="20" placeholder="Enter target person ID..."/>
						<small class="text-muted">This record will receive merged data</small>
					</div>
				</div>

				<div class="d-flex gap-2">
					<button type="submit" name="_target1" class="btn btn-primary">
						<i class="fa fa-arrow-right"></i> <fmt:message key="button.next"/>
					</button>
					<button type="submit" name="_cancel" class="btn btn-outline-secondary">
						<fmt:message key="button.cancel"/>
					</button>
				</div>
			</div>
		</div>
	</form>
</div>