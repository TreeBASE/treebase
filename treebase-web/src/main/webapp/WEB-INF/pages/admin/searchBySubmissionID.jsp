<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.by.submission.id"/></title>
<content tag="heading"><fmt:message key="search.by.submission.id"/></content>
<body id="admin"/>

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle me-3 mt-1"></i>
		<div>
			<p class="mb-0">Search for submissions using different identifier types.</p>
		</div>
	</div>

	<form method="post" id="dataForm">
		<div class="card shadow-lg mb-4">
			<div class="card-header d-flex justify-content-between align-items-center">
				<span class="fw-semibold"><i class="fa fa-search"></i> Search for submissions by identifiers</span>

                <tb:helpButton topic="searchBySubmissionID"/>
			</div>
			<div class="card-body">
				<div class="mb-4">
					<label class="form-label fw-semibold"><fmt:message key="submission.accession"/></label>
					<div class="d-flex flex-column flex-md-row gap-3 mb-3">
						<div class="form-check">
							<input class="form-check-input" type="radio" name="identifierType" value="TB2" id="idTypeTB2" checked>
							<label class="form-check-label" for="idTypeTB2">TreeBASE2 Submission ID</label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio" name="identifierType" value="TB1" id="idTypeTB1">
							<label class="form-check-label" for="idTypeTB1">TreeBASE1 Legacy Study ID</label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio" name="identifierType" value="TB0" id="idTypeTB0">
							<label class="form-check-label" for="idTypeTB0">TreeBASE2 Study ID</label>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<input type="text" name="submissionaccession" class="form-control" maxlength="25" placeholder="Enter ID..."/>
						</div>
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