<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="citation.page.title"/></title>

<div class="container py-5">
	<div class="card shadow-lg mb-4">
		<div class="card-header d-flex justify-content-between align-items-center">
			<span class="fw-semibold"><fmt:message key="citation.page.title"/></span>
			<a href="#" class="openHelp" onclick="openHelp('citationForm')">
				<i class="fa fa-question-circle"></i> Help
			</a>
		</div>
		<div class="card-body">
			<spring:bind path="citation.*">
				<c:if test="${not empty status.errorMessages}">
					<div class="alert alert-danger d-flex align-items-center mb-3" role="alert">
						<i class="fa fa-exclamation-triangle me-2"></i>
						<div>
							<c:forEach var="error" items="${status.errorMessages}">
								<c:out value="${error}" escapeXml="false"/><br />
							</c:forEach>
						</div>
					</div>
				</c:if>
			</spring:bind>

			<p class="text-muted mb-4">Please complete the following citation publication information for <strong>submission ${studyMap['id']} - ${studyMap['name']}</strong></p>

			<form method="post" id="citationForm">
				<input type="hidden" name="id" value="${citation.id}"/>
				<input type="hidden" name="citation.citationType" value="${status.value}"/>

				<div class="row g-3 mb-4">
					<div class="col-md-4">
						<label class="form-label fw-semibold"><fmt:message key="citation.type"/></label>
						<spring:bind path="citation.citationType">
							<select name="${status.expression}" class="form-select" onchange="formSubmit(this.form)">
								<option value="">--- Please Select ---</option>
								<c:forEach var="type" items="${citationtypes}">
									<option value="${type}" <c:if test="${type == citation.citationType}">selected="selected"</c:if>>
										<c:out value="${type}"/>
									</option>
								</c:forEach>
							</select>
						</spring:bind>
					</div>
					<div class="col-md-4">
						<label class="form-label fw-semibold"><fmt:message key="citation.publishyear"/></label>
						<spring:bind path="citation.publishYear">
							<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" onchange="checkYear(this.form)"/>
							<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
						</spring:bind>
					</div>
					<div class="col-md-4">
						<label class="form-label fw-semibold"><fmt:message key="citation.publicationstatus"/></label>
						<spring:bind path="citation.citationStatusDescription">
							<select name="${status.expression}" class="form-select">
								<c:forEach var="pubStatus" items="${statuses}">
									<option value="${pubStatus}" <c:if test="${pubStatus == citation.citationStatusDescription}">selected="selected"</c:if>>
										<c:out value="${pubStatus}"/>
									</option>
								</c:forEach>
							</select>
							<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
						</spring:bind>
					</div>
				</div>

				<c:choose>
					<c:when test="${citation.citationType == 'Book' }">
						<div class="card mb-4">
							<div class="card-header d-flex justify-content-between align-items-center">
								<span class="fw-semibold">Book / Thesis Information</span>
								<a href="#" class="openHelp" onclick="openHelp('citationForm-book')">
									<i class="fa fa-question-circle"></i> Help
								</a>
							</div>
							<div class="card-body">
								<jsp:include page="citationForm-book-fields.jsp"/>
							</div>
						</div>
					</c:when>
					<c:when test="${citation.citationType == 'Book Section' }">
						<div class="card mb-4">
							<div class="card-header d-flex justify-content-between align-items-center">
								<span class="fw-semibold">Book Section / Conference Proceedings Information</span>
								<a href="#" class="openHelp" onclick="openHelp('citationForm-booksection')">
									<i class="fa fa-question-circle"></i> Help
								</a>
							</div>
							<div class="card-body">
								<jsp:include page="citationForm-booksection-fields.jsp"/>
							</div>
						</div>
					</c:when>
					<c:when test="${citation.citationType == 'Article' }">
						<div class="card mb-4">
							<div class="card-header d-flex justify-content-between align-items-center">
								<span class="fw-semibold">Article Information</span>
								<a href="#" class="openHelp" onclick="openHelp('citationForm-article')">
									<i class="fa fa-question-circle"></i> Help
								</a>
							</div>
							<div class="card-body">
								<jsp:include page="citationForm-article-fields.jsp"/>
							</div>
						</div>
					</c:when>
				</c:choose>

				<%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
					<% request.setAttribute("isEditable","yes");%>
				<% } %>

				<c:if test="${publicationState eq 'NotReady' || publicationState eq 'Published' || isEditable eq 'yes'}">
					<div class="d-flex justify-content-end gap-2 mt-4">
						<c:choose>
							<c:when test="${empty citation.id}">
								<button type="submit" name="Submit" class="btn btn-primary btn-lg"><fmt:message key="button.submit"/></button>
							</c:when>
							<c:otherwise>
								<button type="submit" name="Update" class="btn btn-primary btn-lg"><fmt:message key="button.update"/></button>
							</c:otherwise>
						</c:choose>
						<button type="reset" name="Reset" class="btn btn-secondary btn-lg"><fmt:message key="button.reset"/></button>
						<button type="submit" name="_cancel" class="btn btn-outline-secondary btn-lg"><fmt:message key="button.cancel"/></button>
					</div>
				</c:if>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">
//<![CDATA[
function formSubmit(form) {
	form.submit();
}
function checkYear(form) {
	var year = form.publishYear.value;
	var currentDate = new Date();
	var minYear = 1900;
	var maxYear = currentDate.getFullYear();
	var currentMonth = currentDate.getMonth();
	var currentYear = currentDate.getFullYear();
	var currentDay = currentDate.getDate();
	
	if (year.length != 4 || year == 0 || 
	    year < minYear || year > (maxYear + 1)) {
	    if (currentMonth >= 9 || currentMonth == 1) {
	    	currentYear = currentYear + 1;
	    }
	    alert("Please enter a valid year");
	    form.publishYear.value = currentYear;
	}
}
//]]>
</script>

<v:javascript formName="citation" staticJavascript="false" cdata="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
