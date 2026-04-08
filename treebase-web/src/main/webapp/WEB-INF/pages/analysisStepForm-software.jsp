<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="analysis.step.title"/></title>
<content tag="heading"><fmt:message key="analysis.step.title"/></content>
<body id="submissions"/>

<div class="container py-5">
	<spring:bind path="step.*">
		<c:if test="${not empty status.errorMessages}">
			<div class="alert alert-danger d-flex align-items-start mb-4" role="alert">
				<i class="fa fa-exclamation-triangle me-2 fs-5"></i>
				<div>
					<c:forEach var="error" items="${status.errorMessages}">
						<c:out value="${error}" escapeXml="false"/><br/>
					</c:forEach>
				</div>
			</div>
		</c:if>
	</spring:bind>

	<form method="post" onsubmit="return validateCitation(this)">
		<div class="card shadow-lg">
			<div class="card-header bg-primary text-white">
				<i class="fa fa-cpu me-2"></i>
				<span>Analysis Step - Software Information</span>
			</div>
			<div class="card-body">
				<p class="text-muted mb-4">Please complete the following software information used in the analysis step.</p>
				<input type="hidden" name="_page" value="1"/>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label"><fmt:message key="analysis.step.software.name"/>:</label>
					<div class="col-sm-9">
						<spring:bind path="step.softwareInfo.name">
							<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
							<c:if test="${not empty status.errorMessage}">
								<div class="invalid-feedback d-block"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label"><fmt:message key="analysis.step.software.version"/>:</label>
					<div class="col-sm-9">
						<spring:bind path="step.softwareInfo.version">
							<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
							<c:if test="${not empty status.errorMessage}">
								<div class="invalid-feedback d-block"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label"><fmt:message key="analysis.step.software.description"/>:</label>
					<div class="col-sm-9">
						<spring:bind path="step.softwareInfo.description">
							<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
							<c:if test="${not empty status.errorMessage}">
								<div class="invalid-feedback d-block"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>
			</div>
			<div class="card-footer">
				<div class="btn-group">
					<button type="submit" name="_target0" class="btn btn-outline-secondary">
						<i class="fa fa-arrow-left me-1"></i> <fmt:message key="button.previous"/>
					</button>
					<button type="submit" name="_target2" class="btn btn-primary">
						<fmt:message key="button.next"/> <i class="fa fa-arrow-right ms-1"></i>
					</button>
					<button type="submit" name="_cancel" class="btn btn-outline-secondary">
						<i class="fa fa-x-circle me-1"></i> <fmt:message key="button.cancel"/>
					</button>
				</div>
			</div>
		</div>
	</form>
</div>

