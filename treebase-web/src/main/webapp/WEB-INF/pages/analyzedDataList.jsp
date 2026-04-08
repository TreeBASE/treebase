<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="data.list.title"/></title>
<content tag="heading">List of input and output data for selected analysis step</content>
<body id="submissions"/>

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle me-3 mt-1"></i>
		<div>
			<p class="mb-0">The table below shows a list of the input and output data for <strong>analysis step ${analysisStepMap['id']} - ${analysisStepMap['name']}</strong></p>
		</div>
	</div>
	
	<form method="post">
		<div class="card shadow-lg mb-4">
			<div class="card-header">
				<span class="fw-semibold"><i class="fa fa-database me-2"></i> Analyzed Data</span>
			</div>
			<div class="card-body p-0">
				<c:set var="counter" value="0"/>
				<display:table name="${analyzedDataList}"
							   requestURI=""
							   defaultsort="1"
							   class="table table-striped table-hover mb-0"
							   id="userList">
					
					<display:column property="dataId" titleKey="data.id" 
								sortable="true" style="width:15%"/>
								
					<display:column property="inputOutputType" titleKey="data.input.output.type" 
								sortable="true" style="width:20%"/>
								
					<display:column property="dataType" titleKey="data.matrix.tree.type" 
								sortable="true" style="width:20%"/>
								
					<display:column property="title" titleKey="data.notes" 
								sortable="true" style="width:35%"/>

					<display:column titleKey="link.action" style="width:10%">
						<c:url value="/user/updateAnalyzedDataList.html" var="url">
							<c:param name="id" value="${analyzedDataList[counter].id}"/>
						</c:url>
						<a href="<c:out value="${url}"/>" class="btn btn-sm btn-outline-danger">
							<i class="fa fa-trash"></i> Delete
						</a>
						<c:set var="counter" value="${counter+1}"/>
					</display:column>
								
					<display:setProperty name="basic.empty.showtable" value="true"/>
					
				</display:table>
			</div>
			<div class="card-footer">
				<button type="button" class="btn btn-primary"
						onclick="location.href='<c:url value="/user/analyzedDataForm.html"/>'">
					<i class="fa fa-plus-circle"></i> <fmt:message key="button.add.data"/>
				</button>
			</div>
		</div>
	</form>
</div>
