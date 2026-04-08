<%@ include file="/common/taglibs.jsp"%>
<title><fmt:message key="user.merge"/></title>
<content tag="heading"><fmt:message key="user.merge"/></content>
<body id="admin"/>

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle me-3 mt-1"></i>
		<div>
			<p class="mb-0">Please provide source &amp; target user names to merge.</p>
		</div>
	</div>

	<form method="post">
		<input type="hidden" name="_page" value="0"/>
		
		<div class="card shadow-lg mb-4">
			<div class="card-header">
				<span class="fw-semibold"><i class="fa fa-compress"></i> Merging two users</span>
			</div>
			<div class="card-body">
				<div class="row mb-3">
					<div class="col-md-6">
						<label class="form-label fw-semibold">Source <fmt:message key="user.username"/>:</label>
						<input type="text" name="sourceusername" class="form-control" maxlength="50" placeholder="Enter source username..."/>
						<small class="text-muted">This user will be deleted after merge</small>
					</div>
				</div>

				<div class="row mb-4">
					<div class="col-md-6">
						<label class="form-label fw-semibold">Target <fmt:message key="user.username"/>:</label>
						<input type="text" name="targetusername" class="form-control" maxlength="50" placeholder="Enter target username..."/>
						<small class="text-muted">This user will receive merged data</small>
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