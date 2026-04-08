<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="user.management"/></title>
<content tag="heading"><fmt:message key="user.management"/></content>
<body id="admin"/>

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle me-3 mt-1"></i>
		<div>
			<p class="mb-0">This is a simple person audit page. Search for potential duplicate person records based on selected criteria.</p>
		</div>
	</div>

	<form method="post" id="dataForm">
		<div class="card shadow-lg mb-4">
			<div class="card-header">
				<span class="fw-semibold"><i class="fa fa-search"></i> Search for potential duplicate person records</span>
			</div>
			<div class="card-body">
				<div class="mb-4">
					<label class="form-label fw-semibold">Search criteria:</label>
					<div class="d-flex flex-column gap-2">
						<div class="form-check">
							<input class="form-check-input" type="radio" name="<fmt:message key="user.management.userchoice"/>" value="firstAndLast" id="firstAndLast" checked>
							<label class="form-check-label" for="firstAndLast">With Same First and Last Name</label>
						</div>
						<div class="form-check">
							<input class="form-check-input" type="radio" name="<fmt:message key="user.management.userchoice"/>" value="lastOnly" id="lastOnly">
							<label class="form-check-label" for="lastOnly">With Same Last Name</label>
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