<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="user.management"/></title>
<content tag="heading"><fmt:message key="select.studies"/></content>
<body id="admin"/>

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle me-3 mt-1"></i>
		<div>
			<p class="mb-0">Select the type of studies you want to view.</p>
		</div>
	</div>

	<form method="post" id="dataForm">
		<div class="card shadow-lg mb-4">
			<div class="card-header">
				<span class="fw-semibold"><i class="fa fa-filter"></i> Select Studies</span>
			</div>
			<div class="card-body">
				<div class="row mb-4">
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