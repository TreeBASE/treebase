<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="study.title"/></title>



<div class="container py-5">
	<div class="card shadow-lg mb-4">
		<div class="card-header">
<c:choose>
	<c:when test="${empty study.submission.id}">
			Create Submission
	</c:when>
	<c:otherwise>
		Update Submission
	</c:otherwise>
</c:choose>
		</div>
		<div class="card-body">
			<form method="post">
<c:choose>
	<c:when test="${empty study.submission.id}">
		<p>Please provide a brief title for your study. Usually this is the same title as the title of your publication.</p>
		<p>The notes for your study are not for the public, but are there for your own benefit and for communicating with TreeBASE staff once your submission status is ready to be made public. </p>
		<p>If your submission is part of a sponsored research data management plan, please indicate this in the Notes so that TreeBASE staff know to provide added attention and to assist in making your submission fully compliant with the expectations of the sponsor. For more information, see the <a href="/treebase-web/dataMan.html" target="_blank">NSF Data Management Plan instructions</a>. </p>
	</c:when>
	<c:otherwise>
		<p>Please update the following submission information as needed.</p>
		<p>The notes for your study are not for the public, but are there for your own benefit and for communicating with TreeBASE staff once your submission status is ready to be made public. </p>
		<p>If your submission is part of a sponsored research data management plan, please indicate this in the Notes so that TreeBASE staff know to provide added attention and to assist in making your submission fully compliant with the expectations of the sponsor. For more information, see the <a href="/treebase-web/dataMan.html" target="_blank">NSF Data Management Plan instructions</a>. </p>
	</c:otherwise>
</c:choose>				<fieldset class="">
					<legend class="float-none w-auto px-2">Submission Information
						<a href="#" class="openHelp ms-2" onclick="openHelp('studyForm')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
					</legend>

					<spring:bind path="study.*">
						<c:if test="${not empty status.errorMessages}">
							<div class="alert alert-danger d-flex align-items-center mb-3" id="studyError" role="alert">
								<img src="<fmt:message key="icons.warn"/>" alt="<fmt:message key="icon.warning"/>" class="me-2" style="height: 1.5em;" />
								<div>
									<c:forEach var="error" items="${status.errorMessages}">
										<c:out value="${error}" escapeXml="false"/><br />
									</c:forEach>
								</div>
							</div>
						</c:if>
					</spring:bind>

					<div class="mb-3">
						<label for="studyName" class="form-label fw-semibold">
							<fmt:message key="study.name"/>
						</label>
						<spring:bind path="study.name">
							<input type="text" class="form-control form-control-lg" id="studyName" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
							<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
						</spring:bind>
					</div>
					<div class="mb-3">
						<label for="studyNotes" class="form-label fw-semibold">
							<fmt:message key="study.notes"/>
						</label>
						<spring:bind path="study.notes">
							<textarea class="form-control form-control-lg" id="studyNotes" rows="4" name="<c:out value="${status.expression}"/>">${status.value}</textarea>
							<span class="fieldError text-danger"><c:out value="${status.errorMessage}"/></span>
						</spring:bind>
					</div>
					<div class="mb-3">
						<c:if test="${publicationState eq 'Ready' || publicationState eq 'Published'}">
							<strong>For now, this study is read only.</strong>
						</c:if>
						<%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
							<% request.setAttribute("isEditable","yes");%>
						<% } %>
						<c:if test="${publicationState eq 'NotReady'||isEditable eq 'yes'}">
							<div class="d-flex justify-content-end gap-2 mt-4">
								<c:choose>
									<c:when test="${study.id == null }">
										<button type="submit" name="Submit" class="btn btn-primary btn-lg"><fmt:message key="button.submit"/></button>
									</c:when>
									<c:otherwise>
										<button type="submit" name="Update" class="btn btn-primary btn-lg"><fmt:message key="button.update"/></button>
										<!-- <button type="submit" name="Delete" class="btn btn-danger btn-lg"><fmt:message key="button.delete"/></button> -->
									</c:otherwise>
								</c:choose>
								<button type="reset" name="Reset" class="btn btn-secondary btn-lg"><fmt:message key="button.reset"/></button>
								<button type="submit" name="_cancel" class="btn btn-outline-secondary btn-lg"><fmt:message key="button.cancel"/></button>
							</div>
						</c:if>
					</div>
				</fieldset>
			</form>
		</div>
	</div>
</div>

<v:javascript formName="study" staticJavascript="false" cdata="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
