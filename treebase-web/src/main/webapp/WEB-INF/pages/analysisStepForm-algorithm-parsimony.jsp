<%@include file="/common/taglibs.jsp" %>

<div class="row mb-3">
	<label class="col-sm-3 col-form-label"><fmt:message key="analysis.step.algorithm.parsimony.gapMode" />:</label>
	<div class="col-sm-9">
		<spring:bind path="step.gapMode.description">
			<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}" />"/>
			<c:if test="${not empty status.errorMessage}">
				<div class="invalid-feedback d-block"><c:out value="${status.errorMessage}" /></div>
			</c:if>
		</spring:bind>
	</div>
</div>
<div class="row mb-3">
	<label class="col-sm-3 col-form-label"><fmt:message key="analysis.step.algorithm.parsimony.polyTCount" />:</label>
	<div class="col-sm-9">
		<spring:bind path="step.polyTCount.description">
			<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}" />"/>
			<c:if test="${not empty status.errorMessage}">
				<div class="invalid-feedback d-block"><c:out value="${status.errorMessage}" /></div>
			</c:if>
		</spring:bind>
	</div>
</div>