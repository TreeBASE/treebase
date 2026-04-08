<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="data.type.title"/></title>
<content tag="heading"><fmt:message key="data.type.title"/></content>
<body id="submissions"/>

<div class="container py-5">
	<spring:bind path="data.*">
		<c:if test="${not empty status.errorMessages}">
		<div class="alert alert-danger d-flex align-items-start mb-4" role="alert">
			<i class="fa fa-exclamation-triangle me-3 mt-1"></i>
			<div>
				<c:forEach var="error" items="${status.errorMessages}">
					<c:out value="${error}" escapeXml="false"/><br />
				</c:forEach>
			</div>
		</div>
		</c:if>
	</spring:bind>

	<form method="post" id="dataForm">
		<input type="hidden" name="_page" value="0"/>
		
		<div class="card shadow-lg mb-4">
			<div class="card-header d-flex justify-content-between align-items-center">
				<span class="fw-semibold"><i class="fa fa-database me-2"></i> Data Type Selection</span>
				<a href="#" class="openHelp" onclick="openHelp('analyzedDataTypeSelection')">
					<i class="fa fa-question-circle"></i> Help
				</a>
			</div>
			<div class="card-body">
				<p class="text-muted mb-4">Please select the data you are entering for an analysis step</p>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label fw-semibold"><fmt:message key="data.analysis.step"/>:</label>
					<div class="col-sm-9">
						<spring:bind path="data.step">
							<p class="form-control-plaintext"><c:out value="${steps[0].label}"/></p>
						</spring:bind>
					</div>
				</div>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label fw-semibold"><fmt:message key="data.inputoutput.type"/>:</label>
					<div class="col-sm-9">
						<spring:bind path="data.inputOutputType">
							<select name="${status.expression}" class="form-select">
								<option value="">--- Select ---</option>
								<c:forEach var="type" items="${inputOutputTypes}">
									<option value="${type.value}" <c:if test="${type.value == data.inputOutputType}">selected="selected"</c:if>>
										<c:out value="${type.label}"/>
									</option>
								</c:forEach>
							</select>
						</spring:bind>
					</div>
				</div>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label fw-semibold"><fmt:message key="data.type"/>:</label>
					<div class="col-sm-9">
						<spring:bind path="data.dataType">
							<select name="${status.expression}" class="form-select">
								<option value="">--- Select ---</option>
								<c:forEach var="type" items="${dataTypes}">
									<option value="${type.value}" <c:if test="${type.value == data.dataType}">selected="selected"</c:if>>
										<c:out value="${type.label}"/>
									</option>
								</c:forEach>
							</select>
						</spring:bind>
					</div>
				</div>
			</div>
			<div class="card-footer">
				<div class="d-flex gap-2">
					<button type="submit" name="_target1" class="btn btn-primary">
						<i class="fa fa-arrow-right"></i> <fmt:message key="button.next"/>
					</button>
					<button type="reset" name="Reset" class="btn btn-outline-secondary">
						<i class="fa fa-undo"></i> <fmt:message key="button.reset"/>
					</button>
					<button type="submit" name="_cancel" class="btn btn-outline-secondary">
						<fmt:message key="button.cancel"/>
					</button>
				</div>
			</div>
		</div>
	</form>
</div>

<script type="text/javascript">
</script>

<v:javascript formName="data" staticJavascript="false" cdata="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
