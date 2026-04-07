<%@include file="/common/taglibs.jsp" %>

<div class="row g-3">
	<div class="col-12">
		<label class="form-label fw-semibold"><fmt:message key="citation.article.title"/></label>
		<spring:bind path="citation.title">
			<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
			<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</div>
	
	<div class="col-12">
		<label class="form-label fw-semibold"><fmt:message key="citation.keywords"/></label>
		<spring:bind path="citation.keywords">
			<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
			<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</div>
	
	<div class="col-12">
		<label class="form-label fw-semibold"><fmt:message key="citation.abstract"/></label>
		<spring:bind path="citation.abstract">
			<textarea rows="5" class="form-control" name="<c:out value="${status.expression}"/>">${status.value}</textarea>
			<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</div>
	
	<div class="col-md-6">
		<label class="form-label fw-semibold" title="PubMed ID"><fmt:message key="citation.pmid"/></label>
		<spring:bind path="citation.PMID">
			<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" title="PubMed ID" maxlength="10"/>
			<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</div>
	
	<div class="col-md-6">
		<label class="form-label fw-semibold" title="Digital Object Identifier"><fmt:message key="citation.doi"/></label>
		<spring:bind path="citation.doi">
			<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" title="Digital Object Identifier"/>
			<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</div>
	
	<div class="col-12">
		<label class="form-label fw-semibold"><fmt:message key="citation.url"/></label>
		<spring:bind path="citation.URL">
			<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
			<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</div>
	
	<div class="col-12">
		<label class="form-label fw-semibold"><fmt:message key="citation.article.journal"/></label>
		<spring:bind path="citation.journal">
			<div id="ac">
				<input type="text" class="form-control" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
				<div id="journalList" class="auto_complete"></div>
				<script type="text/javascript">
					new Autocompleter.DWR('<c:out value="${status.expression}"/>', 'journalList', updateJournalNameList, { valueSelector: nameValueSelector, partialChars: 0 });
				</script>
			</div>
			<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</div>
	
	<div class="col-md-6">
		<label class="form-label fw-semibold"><fmt:message key="citation.article.volume"/></label>
		<spring:bind path="citation.volume">
			<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
			<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</div>
	
	<div class="col-md-6">
		<label class="form-label fw-semibold"><fmt:message key="citation.article.issue"/></label>
		<spring:bind path="citation.issue">
			<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
			<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</div>
	
	<div class="col-12">
		<label class="form-label fw-semibold" title="Please provide page numbers in Microsoft Word format, e.g. 11-28"><fmt:message key="citation.article.pages"/></label>
		<spring:bind path="citation.pages">
			<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" title="Please provide page numbers in Microsoft Word format, e.g. 11-28"/>
			<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</div>
</div>
