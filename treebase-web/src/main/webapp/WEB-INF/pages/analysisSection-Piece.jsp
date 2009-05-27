	<%@ include file="/common/taglibs.jsp"%>
	<c:url var="matrixRowURL" value="/user/matrixRowList.html"/>
	<c:url var="phylowidgetURL" value="/user/directToPhyloWidget.html" />
	
	<li>
		<b>Software Used:</b><c:out value="${analysisStepCommand.softwareInfo.name}"/>
	</li>
	<li>
		<b>Software Version:</b><c:out value="${analysisStepCommand.softwareInfo.version}"/>
	</li>
	 <li>
		<b>Command Strings:</b><c:out value="${analysisStepCommand.commands}"/>
	</li>
	<li>
		<b>Algorithm Used:</b><c:out value="${analysisStepCommand.algorithmType}"/>
	</li>
	
	<c:forEach var="analyzedData" items="${analysisStepCommand.analyzedDataCommandList}">
		<li>
			<b><c:out value="${analyzedData.inputOutputType }"/> <c:out value="${analyzedData.dataType}"/>:</b> 
			
			<c:if test="${analyzedData.dataType eq 'matrix'}"> 
				<a href="${matrixRowURL}?id=${analyzedData.dataId}"><c:out value="${analyzedData.displayName}"/></a> 
			</c:if>
			
			<c:if test="${analyzedData.dataType eq 'tree'}"> 
				<a href="${phylowidgetURL}?treeid=${analyzedData.dataId}" target = "_blank"><c:out value="${analyzedData.displayName}"/></a> 
			</c:if>
			
			<c:if test="${search != 'y' && pageContext['request'].remoteUser != null &&  publicationState eq 'NotReady'}">
				<a href="<c:url value="/user/updateAnalyzedDataList.html?id=${analyzedData.id}&amp;analysis_step_id=${analysisStepCommand.id}"/>">[Delete]</a>
			</c:if>
		</li>
	</c:forEach>