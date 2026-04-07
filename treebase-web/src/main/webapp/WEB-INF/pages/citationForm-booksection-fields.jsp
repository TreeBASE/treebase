<%@include file="/common/taglibs.jsp" %>
<c:url var="editorURL" value="/user/editorForm.html" />

<div class="row g-3">
	<div class="col-12">
		<label class="form-label fw-semibold"><fmt:message key="citation.booksection.title"/></label>
		<spring:bind path="citation.sectionTitle">
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
		<label class="form-label fw-semibold" title="Please provide page numbers in Microsoft Word format, e.g. 11-28"><fmt:message key="citation.booksection.page"/></label>
		<spring:bind path="citation.sectionPages">
			<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" title="Please provide page numbers in Microsoft Word format, e.g. 11-28"/>
			<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</div>
	
	<div class="col-12">
		<label class="form-label fw-semibold"><fmt:message key="citation.book.title"/></label>
		<spring:bind path="citation.bookTitle">
			<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
			<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</div>
	
	<div class="col-md-6">
		<label class="form-label fw-semibold"><fmt:message key="citation.book.publisher"/></label>
		<spring:bind path="citation.publisher">
			<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
			<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</div>
	
	<div class="col-md-6">
		<label class="form-label fw-semibold"><fmt:message key="citation.book.city"/></label>
		<spring:bind path="citation.city">
			<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
			<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</div>
	
	<div class="col-12">
		<label class="form-label fw-semibold"><fmt:message key="citation.book.isbn"/></label>
		<spring:bind path="citation.ISBN">
			<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
			<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</div>
	
	<c:if test="${not empty citation.id}">
		<div class="col-12 text-center">
			<a href="<c:out value="${editorURL}"/>" class="btn btn-outline-secondary">
				<i class="fa fa-edit"></i> Editor(s)
			</a>
		</div>
	</c:if>
</div>
