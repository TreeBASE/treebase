<%@ include file="/common/taglibs.jsp"%>
<title><fmt:message key="analysis.list.title"/></title>
<content tag="heading"><fmt:message key="analysis.list.title"/></content>
<body id="submissions"/>

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle me-2 fs-5"></i>
		<div>
			TreeBASE will only publish matrices and trees that are listed with analysis entries. 
			At a minimum, each submission must have at least one analysis entry containing at least one analysis step.
		</div>
	</div>
	
	<div class="card shadow-lg">
		<div class="card-header bg-primary text-white d-flex align-items-center justify-content-between">
			<div>
				<i class="fa fa-bar-chart me-2"></i>
				<span>List of Analyses</span>
			</div>
			<tb:helpButton topic="analysisSection"/>			
		</div>
		<div class="card-body">
			<p class="text-muted mb-3">
				The table below shows a list of the analyses for <strong>submission ${studyMap['id']} - ${studyMap['name']}</strong>. 
				To add an analysis, click the <i class="fa fa-plus-circle text-success"></i> button below. 
				To delete an analysis, click its <i class="fa fa-trash text-danger"></i> icon. 
				To add steps to the analysis, define input and output data and record metadata, click the analysis's 
				<i class="fa fa-pencil text-primary"></i> icon.
			</p>
			
			<div class="mb-3">
				<a href="<c:url value="/user/analysisForm.html"/>" class="btn btn-success btn-sm">
					<i class="fa fa-plus-circle me-1"></i> Add Analysis
				</a>
			</div>
			
			<c:set var="counter" value="1"/>
			<display:table name="${studyCommand.analysisCommandList}"
						   requestURI=""
						   defaultsort="1"
						   class="table table-striped table-hover"
						   id="userList">
							
				<display:column titleKey="analysis.name" sortable="true">				
					<c:choose>
						<c:when test='${not empty userList.name and userList.name != ""}'>
							<c:out value="${userList.name}"/>
						</c:when>
						<c:otherwise>
							<c:out value="analysis ${counter}"/>
						</c:otherwise>
					</c:choose>
					<c:set var="counter" value="${counter+1}"/>
				</display:column>
							
				<display:column property="notes" titleKey="analysis.notes" sortable="true"/>
							
				<display:column sortable="false" title="Status" class="text-center">
					<c:choose>
						<c:when test="${userList.validated}">
							<span class="badge bg-success" title="<fmt:message key="analysis.validated"/>">
								<i class="fa fa-check-circle"></i> Valid
							</span>
						</c:when>
						<c:otherwise>
							<span class="badge bg-warning text-dark" title="<fmt:message key="analysis.notvalidated"/>">
								<i class="fa fa-exclamation-circle"></i> Incomplete
							</span>
						</c:otherwise>
					</c:choose>
				</display:column>
							
				<display:column sortable="false" title="Actions" class="text-center">
					<div class="btn-group btn-group-sm">
						<a href="/user/analysisForm.html?id=${userList.id}" class="btn btn-outline-primary" 
						   title="<fmt:message key="analysis.edit"/>">
							<i class="fa fa-pencil"></i>
						</a>
						<form method="post" action="analysisForm.html" id="form${userList.id}" 
							  style="display:inline;" class="m-0">
							<button type="submit" class="btn btn-outline-danger" 
									title="<fmt:message key="analysis.delete"/>">
								<i class="fa fa-trash"></i>
							</button>
							<input type="hidden" name="id" value="${userList.id}"/>
							<input type="hidden" name="Delete" value="Delete"/>						
						</form>
					</div>
				</display:column>			
				
				<display:setProperty name="basic.empty.showtable" value="true"/>
				
			</display:table>
		</div>
	</div>
</div>