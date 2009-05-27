<%@include file="/common/taglibs.jsp" %>


<tr>
	<th><fmt:message key="analysis.step.algorithm.parsimony.gapMode" />:</th>
	<td><spring:bind path="step.gapMode.description">
		<input size="40" type="text"
			name="<c:out value="${status.expression}"/>"
			value="<c:out value="${status.value}"/>" />
		<span class="fieldError"><c:out value="${status.errorMessage}" /></span>
	</spring:bind></td>
</tr>
<tr>
	<th><fmt:message
		key="analysis.step.algorithm.parsimony.polyTCount" />:</th>
	<td><spring:bind path="step.polyTCount.description">
		<input size="40" type="text"
			name="<c:out value="${status.expression}"/>"
			value="<c:out value="${status.value}"/>" />
		<span class="fieldError"><c:out value="${status.errorMessage}" /></span>
	</spring:bind></td>
</tr>