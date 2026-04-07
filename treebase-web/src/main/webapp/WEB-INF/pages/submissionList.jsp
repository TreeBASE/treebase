<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="submission.list.title"/></title>

<div class="container-fluid">
	<c:url var="inProgressURL" value="/user/submissionList.html?method=inProgressSubmission" />
	<c:url var="deletePageURL" value="/user/deleteStudy.html?" />
	<c:url var="readyStateURL" value="/user/readyState.html?"/>
	
	<div class="card mb-4">
		<div class="card-header d-flex justify-content-between align-items-center">
			<span class="fw-semibold"><fmt:message key="submission.list.title"/></span>
			<a href="#" class="openHelp" onclick="openHelp('submissionList')">
				<i class="fa fa-question-circle fa-icon"></i> Help
			</a>
		</div>
		<div class="card-body">
			<p class="text-muted mb-3">The table below shows a list of your TreeBASE submissions. Submissions can only be deleted after all trees and matrices are deleted first.</p>
			
			<form method="post" action="<c:url value='/user/submissionList.html'/>" class="mb-3">
				<div class="row align-items-center">
					<div class="col-auto">
						<label class="col-form-label fw-semibold">Filter by status:</label>
					</div>
					<div class="col-auto">
						<select class="form-select" name="method" onChange="this.form.submit();">
							<option value="submissionsByUser">All Submissions</option>
							<c:forEach var="type" items="${submissiontypes}">
								<option value="${type.value}" <c:if test="${type.value == method}">selected="selected"</c:if>>
									<c:out value="${type.label}"/>
								</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-auto ms-auto">
						<a href="<c:url value='/user/studyForm.html'/>?form=new" class="btn btn-success">
							<i class="fa fa-plus"></i> <fmt:message key="button.new.submission"/>
						</a>
					</div>
				</div>
			</form>
			
			<div class="table-responsive">
				<display:table 
					name="${submissionList}" 
					requestURI=""
					defaultsort="1"
					class="table table-striped table-hover align-middle"
					id="userList"
					cellspacing="0"
					cellpadding="0">
					<display:column title="ID" sortable="true" property="id" class="text-center" style="width: 80px;"/>
					<display:column property="study.name" title="Study Name" sortable="true"/>
					<display:column property="study.notes" title="Notes" sortable="true" maxLength="50"/>
					<display:column property="study.studyStatus.description" title="Status" sortable="true" class="text-center"/>
					<display:column title="Actions" sortable="false" class="text-center text-nowrap" style="width: 200px;">
						<div class="btn-group btn-group-sm" role="group">
							<a href="<c:url value='/user/summary.html'/>?id=${userList.id}" class="btn btn-outline-primary" title="<fmt:message key='study.update.title'/>">
								<i class="fa fa-edit"></i>
							</a>
							<c:if test="${userList.study.studyStatus.description eq 'In Progress'}">
								<a href="${readyStateURL}submissionid=${userList.id}" class="btn btn-outline-success" title="Change to Ready State">
									<i class="fa fa-check"></i>
								</a>
								<a href="${deletePageURL}submissionid=${userList.id}" class="btn btn-outline-danger" title="<fmt:message key='study.delete'/>">
									<i class="fa fa-trash"></i>
								</a>
							</c:if>
						</div>
					</display:column>
					<display:setProperty name="basic.empty.showtable" value="true"/>
				</display:table>
			</div>
		</div>
	</div>
	
	<c:if test="${not empty sessionScope.importStatus}">
		<div class="card">
			<div class="card-header d-flex justify-content-between align-items-center">
				<span class="fw-semibold">Dryad Import Result</span>
				<a href="#" class="openHelp" onclick="openHelp('dryadImport')">
					<i class="fa fa-question-circle fa-icon"></i> Help
				</a>
			</div>
			<div class="card-body text-center">
				<c:choose>
					<c:when test="${sessionScope.importStatus == 'NOT FOUND'}">
						<div class="alert alert-warning mb-0">
							<i class="fa fa-exclamation-triangle"></i> Sorry, we cannot find your data
						</div>
					</c:when>
					<c:when test="${sessionScope.importStatus == 'OK'}">
						<div class="alert alert-success mb-0">
							<i class="fa fa-check-circle"></i> Import finished successfully
						</div>
					</c:when>
					<c:when test="${sessionScope.importStatus == 'FAILED'}">
						<div class="alert alert-danger mb-0">
							<i class="fa fa-times-circle"></i> Sorry, one or more data files could not be parsed correctly. You may add your data via TreeBASE interfaces.
						</div>
					</c:when>
				</c:choose>
			</div>
		</div>
	</c:if>
</div>