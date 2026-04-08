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
				<i class="fa fa-diagram-3 me-2"></i>
				<span>Analysis Step - Algorithm Information</span>
			</div>
			<div class="card-body">
				<p class="text-muted mb-4">Please complete the following algorithm information used in the analysis step.</p>
				<input type="hidden" name="_page" value="2"/>
				
				<c:choose>
					<c:when test="${empty step.algorithmType }">
						<c:set var="algorithmType" value="Likelihood"/>
					</c:when>                             
					<c:otherwise>
						<c:set var="algorithmType" value="${step.algorithmType}"/>
					</c:otherwise>
				</c:choose>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label"><fmt:message key="analysis.step.algorithm.type"/>:</label>
					<div class="col-sm-9">
						<spring:bind path="step.algorithmType">
							<select name="${status.expression}" class="form-select" style="max-width:200px" onchange="this.form.submit()">
								<option value="">--- Please Select ---</option>
								<c:forEach var="type" items="${algorithmtypes}">
									<option value="${type}" <c:if test="${type == step.algorithmType}">selected="true"</c:if>>
										<c:out value="${type}"/>
									</option>
								</c:forEach>
							</select>
						</spring:bind>
					</div>
				</div>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label"><fmt:message key="analysis.step.algorithm.propertyName"/>:</label>
					<div class="col-sm-9">
						<spring:bind path="step.algorithmMap[${algorithmType}].propertyName">
							<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
							<c:if test="${not empty status.errorMessage}">
								<div class="invalid-feedback d-block"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label"><fmt:message key="analysis.step.algorithm.propertyValue"/>:</label>
					<div class="col-sm-9">
						<spring:bind path="step.algorithmMap[${algorithmType}].propertyValue">
							<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
							<c:if test="${not empty status.errorMessage}">
								<div class="invalid-feedback d-block"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>
				
				<%-- Algorithm-specific fields --%>
				<c:choose>
					<c:when test="${step.algorithmType == 'Likelihood'}">
						<jsp:include page="analysisStepForm-algorithm-likelihood.jsp"/>
					</c:when>
					<c:when test="${step.algorithmType == 'Parsimony'}">
						<jsp:include page="analysisStepForm-algorithm-parsimony.jsp"/>
					</c:when>
					<c:when test="${step.algorithmType == 'Distance'}">
						<jsp:include page="analysisStepForm-algorithm-distance.jsp"/>
					</c:when>
					<c:when test="${step.algorithmType == 'Other Algorithm'}">
						<jsp:include page="analysisStepForm-algorithm-other.jsp"/>
					</c:when>
				</c:choose>
			</div>
			<div class="card-footer">
				<div class="btn-group">
					<button type="submit" name="_target1" class="btn btn-outline-secondary">
						<i class="fa fa-arrow-left me-1"></i> <fmt:message key="button.previous"/>
					</button>
					<button type="submit" name="_finish" class="btn btn-success">
						<i class="fa fa-check-circle me-1"></i> <fmt:message key="button.finish"/>
					</button>
					<button type="submit" name="_cancel" class="btn btn-outline-secondary">
						<i class="fa fa-x-circle me-1"></i> <fmt:message key="button.cancel"/>
					</button>
				</div>
			</div>
		</div>
	</form>
</div>

