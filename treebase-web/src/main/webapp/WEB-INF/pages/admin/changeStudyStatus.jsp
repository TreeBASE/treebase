<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="submission.list.title"/></title>
<content tag="heading"><fmt:message key="submission.list.title"/></content>
<body id="admin"/>

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle me-3 mt-1"></i>
		<div>
			<p class="mb-0">The table below shows a list of TreeBASE submissions. You can change study status and delete studies from this page.</p>
		</div>
	</div>

	<form method="post">
		<c:url var="deletePageURL" value="/user/deleteStudy.html?" />

		<div class="card shadow-lg mb-4">
			<div class="card-header d-flex justify-content-between align-items-center">
				<span class="fw-semibold"><i class="fa fa-list"></i> <fmt:message key="submission.list.title"/></span>
			</div>
			<div class="card-body p-0">
				<c:set var="counter" value="0"/>
				<div class="table-responsive">
					<display:table name="requestScope.asubmissionlist.myList" 
								   requestURI=""
								   defaultsort="1"
								   class="table table-striped table-hover mb-0"
								   id="userList"
								   export="true">
						
						<display:column property="id" title="ID" 
									sortable="true"
									url="/user/summary.html" paramId="id" paramProperty="id"/>
									
						<display:column title="Submitter" sortable="true">
							<a href='mailto:${userList.submitter.emailAddressString}?subject=TreeBASE Submission S${userList.id}' class="text-decoration-none">
								<i class="fa fa-envelope me-1"></i>${userList.submitter.username}
							</a>
						</display:column>
									
						<display:column property="study.name" title="Study Name" sortable="true"/>
									
						<display:column property="study.notes" title="Study Notes" sortable="true"/>		
						
						<display:column property="createDate" title="Created" sortable="true"/>		

						<display:column property="lastModifiedDate" title="Last Modified" sortable="true"/>		

						<display:column title="Change Status">
							<spring:bind path="asubmissionlist.myList[${counter}].study.transientDescription">
								<div class="d-flex flex-column gap-1">
									<div class="form-check form-check-inline">
										<input class="form-check-input" type="radio" name="${status.expression}" value="${studyStatusTypes[1]}" id="status_${counter}_1" <c:if test="${status.value eq studyStatusTypes[1]}">checked</c:if>>
										<label class="form-check-label" for="status_${counter}_1">${studyStatusTypes[1]}</label>
									</div>
									<div class="form-check form-check-inline">
										<input class="form-check-input" type="radio" name="${status.expression}" value="${studyStatusTypes[2]}" id="status_${counter}_2" <c:if test="${status.value eq studyStatusTypes[2]}">checked</c:if>>
										<label class="form-check-label" for="status_${counter}_2">${studyStatusTypes[2]}</label>
									</div>
									<div class="form-check form-check-inline">
										<input class="form-check-input" type="radio" name="${status.expression}" value="${studyStatusTypes[3]}" id="status_${counter}_3" <c:if test="${status.value eq studyStatusTypes[3]}">checked</c:if>>
										<label class="form-check-label" for="status_${counter}_3">${studyStatusTypes[3]}</label>
									</div>
								</div>
							</spring:bind>	
							<c:set var="counter" value="${counter+1}"/>	
						</display:column>

						<display:column titleKey="link.delete">
							<a href="<c:out value="${deletePageURL}"/><c:out value="submissionid="/><c:out value="${userList.id}" />" class="btn btn-outline-danger btn-sm">
								<i class="fa fa-trash"></i> Delete
							</a>
						</display:column>
									
						<display:setProperty name="export.pdf" value="true" />	
						<display:setProperty name="basic.empty.showtable" value="true"/>
						
					</display:table>
				</div>
			</div>
			<div class="card-footer">
				<div class="d-flex gap-2 justify-content-end">
					<button type="submit" name="Update" class="btn btn-primary">
						<i class="fa fa-save"></i> <fmt:message key="button.update"/>
					</button>
					<button type="submit" name="_cancel" class="btn btn-outline-secondary">
						<fmt:message key="button.cancel"/>
					</button>
				</div>
			</div>
		</div>
	</form>
</div>