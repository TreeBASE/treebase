<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="data.list.title"/></title>
<content tag="heading">List of uploaded <b>${data.dataType} data</b></content>
<body id="submissions"/>

<div class="container py-5">
	<spring:bind path="data.*">
		<c:if test="${not empty status.errorMessages}">
		<div class="alert alert-danger d-flex align-items-start mb-4" role="alert">
			<i class="bi bi-exclamation-triangle-fill me-3 mt-1"></i>
			<div>
				<c:forEach var="error" items="${status.errorMessages}">
					<c:out value="${error}" escapeXml="false"/><br />
				</c:forEach>
			</div>
		</div>
		</c:if>
	</spring:bind>

	<form method="post" name="dataform">
		<input type="hidden" name="_page" value="2"/>
		
		<div class="card shadow-lg mb-4">
			<div class="card-header d-flex justify-content-between align-items-center">
				<span class="fw-semibold"><i class="bi bi-collection me-2"></i> Analyzed Tree Block Selection</span>
				<a href="#" class="openHelp" onclick="openHelp('analyzedTreeBlockSelection')">
					<i class="bi bi-question-circle"></i> Help
				</a>
			</div>
			<div class="card-body">
				<p class="text-muted mb-3">Check the list of ${data.dataType} (TreeBlock) data that will be used for analysis step</p>
				
				<c:set var="counter" value="0"/>
				<display:table name="${data.treeBlockList}"
							   requestURI=""
							   defaultsort="1"
							   class="table table-striped table-hover"
							   id="matrix">

					<display:column class="text-center" style="width:5%">
						<spring:bind path="data.treeBlockList[${counter}].checked">
							<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
							<input type="checkbox" class="form-check-input" name="${status.expression}" value="true" 
							<c:if test="${not empty matrix.selected}">checked disabled</c:if>/>
						</spring:bind>
					</display:column>
							
					<display:column property="treeBlock.title" title="Tree block title" 
								sortable="true" style="width:80%">
					</display:column>
					
					<display:column paramProperty="treeBlock.id"
							url="/user/treeList.html" 
							paramId="treeblockid" 
							sortable="false"
							style="width:15%">
							<span class="btn btn-sm btn-outline-primary">
								<i class="bi bi-list-ul"></i> Trees
							</span>
					</display:column>
					
					<c:set var="counter" value="${counter+1}"/>
					<display:setProperty name="basic.empty.showtable" value="true"/>
					
				</display:table>
			</div>
			<div class="card-footer">
				<div class="d-flex gap-2">
					<button type="submit" name="_target0" class="btn btn-outline-secondary">
						<i class="bi bi-arrow-left"></i> <fmt:message key="button.previous"/>
					</button>
					<button type="submit" name="_finish" class="btn btn-primary">
						<i class="bi bi-check-lg"></i> <fmt:message key="button.finish"/>
					</button>
					<button type="submit" name="_cancel" class="btn btn-outline-secondary">
						<fmt:message key="button.cancel"/>
					</button>
				</div>
			</div>
		</div>
	</form>
</div>
