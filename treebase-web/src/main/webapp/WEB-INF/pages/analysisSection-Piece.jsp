	<%@ include file="/common/taglibs.jsp"%>
	<c:url var="matrixRowURL" value="/user/matrixRowList.html"/>
	<c:url var="phylowidgetURL" value="/user/directMapToPhyloWidget.html" />
	
	<li class="list-group-item d-flex justify-content-between align-items-start py-1">
		<span><strong>Software Used:</strong> <c:out value="${analysisStepCommand.softwareInfo.name}"/></span>
	</li>
	<li class="list-group-item d-flex justify-content-between align-items-start py-1">
		<span><strong>Software Version:</strong> <c:out value="${analysisStepCommand.softwareInfo.version}"/></span>
	</li>
	<li class="list-group-item d-flex justify-content-between align-items-start py-1">
		<span><strong>Command Strings:</strong> <c:out value="${analysisStepCommand.commands}"/></span>
	</li>
	<li class="list-group-item d-flex justify-content-between align-items-start py-1">
		<span><strong>Algorithm Used:</strong> <c:out value="${analysisStepCommand.algorithmType}"/></span>
	</li>
	
	<c:forEach var="analyzedData" items="${analysisStepCommand.analyzedDataCommandList}">
		<li class="list-group-item d-flex justify-content-between align-items-start py-1">
			<span>
				<strong><c:out value="${analyzedData.inputOutputType }"/> <c:out value="${analyzedData.dataType}"/>:</strong> 
				
				<c:if test="${analyzedData.dataType eq 'matrix'}"> 
					<a href="${matrixRowURL}?id=${analyzedData.dataId}" class="text-primary"><c:out value="${analyzedData.displayName}"/></a> 
				</c:if>
				
				<c:if test="${analyzedData.dataType eq 'tree'}"> 
					<a href="${phylowidgetURL}?treeid=${analyzedData.dataId}" class="text-primary"><c:out value="${analyzedData.displayName}"/></a> 
				</c:if>
			</span>
			<c:if test="${search != 'y' && pageContext['request'].remoteUser != null && publicationState eq 'NotReady'}">
				<a href="<c:url value="/user/updateAnalyzedDataList.html?id=${analyzedData.id}&amp;analysis_step_id=${analysisStepCommand.id}"/>" 
				   class="btn btn-outline-danger btn-sm">
					<i class="fa fa-trash"></i>
				</a>
			</c:if>
		</li>
	</c:forEach>