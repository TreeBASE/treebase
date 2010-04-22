<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("carriageReturn","\n"); %>

<c:set var="currentSection" scope="request" value="Analyses"/>
<c:set var="headerPrefix" scope="request" value="Analysis ${analysis.id} of "/>
<jsp:include page="nav.jsp"/>

<c:url var="studyURL" value="summary.html">
<c:param name="id" value="${study.id}"/>
<c:set var="cit" value="${study.citation}"/>
</c:url>

<title>Analysis ${analysis.id} of study ${study.id}</title>
<body id="s-analysis"/>

<c:if test="${not empty analysis.name}">
<h3>Name</h3>

${analysis.name}
</c:if>

<c:if test="${not empty analysis.notes}">
<h3>Notes</h3>

<c:out value="${analysis.notes}"/>
</c:if>

<c:if test="${empty analysis.name &&  empty analysis.notes}">
<p class="bvnotice">[ Bill and Val: If this analysis had has a name or notes attached, they would have appeared here. 
However, all the data in the database is from TB1, and there are no names or notes 
on TB1 analyses.&nbsp;]</p>
</c:if>

		<ol id="main" style="display: block">
			<!--  process each analysis step for the analysis -->
			<c:forEach var="analysisStep"
				items="${analysis.analysisStepsReadOnly}"
				varStatus="status_analysisStep">
				<c:set var="AnalysisStepId"
					value="${AnalysisId}AnalysisStep${status_analysisStep.count}" />

				<li>
					<img 
						src="<c:url value="/images/minus.gif"/>"
						name="img${AnalysisStepId}" 
						width="9" 
						height="9" 
						border="0"
						onClick="CollapseExpand('${AnalysisStepId}')"/> 
                                        <!-- commented out due to issue# 2970700 and issue# 2970457 -->
					<a href="/treebase-web/search/downloadAnAnalysisStep.html?analysisid=${analysisStep.id}"> 
						<img 
							class="iconButton" 
							src="<fmt:message key="icons.download.reconstructed"/>" 
							title="<fmt:message key="download.reconstructedfile"/>" 
							alt="<fmt:message key="download.reconstructedfile"/>"/>	
					</a>					
					<strong>Analysis Step ${status_analysisStep.count}:</strong>
					<c:out value="${analysisStepCommand.name}" />
				</li>

				<!--  process information related to analysis step -->
				<ol id="${AnalysisStepId}" style="display: block">
					<li><strong>Software Used:</strong><c:out
						value="${analysisStep.softwareInfo.name}" /></li>
					<c:if test="${not empty analysisStep.softwareInfo.version }">
						<li><strong>Software Version:</strong><c:out
							value="${analysisStep.softwareInfo.version}" /></li>
					</c:if>
					<c:if test="${not empty analysisStep.commands }">
						<li><strong>Command Strings:</strong><c:out
							value="${analysisStep.commands}" /></li>
					</c:if>
					<li><strong>Algorithm Used:</strong><c:out
						value="${analysisStep.algorithmInfo.algorithmType}" /></li>

					<c:forEach var="analyzedData"
						items="${analysisStep.dataSetReadOnly}">
						<c:choose>
						<c:when test='${ analyzedData.dataType == "matrix" }'>
						    <c:url var="url" value="matrix.html">
						        <c:param name="matrixid" value="${analyzedData.matrixData.id}"/>
						        <c:param name="id" value="${study.id}"/>
						    </c:url>
						</c:when>
						<c:when test='${ analyzedData.dataType == "tree" }'>
						    <c:url var="url" value="tree.html">
						        <c:param name="treeid" value="${analyzedData.treeData.id}"/>
						        <c:param name="id" value="${study.id}"/>
						    </c:url>
						</c:when>
						<c:otherwise><c:remove var="url"/></c:otherwise>
						</c:choose>
						
						<li><strong><c:out
							value="${analyzedData.inputOutput }" /> <c:out
							value="${analyzedData.dataType}" />:</strong>
							<c:if test="${not empty url}">
							    <a href='${url}'>
  						 		<c:out value="${analyzedData.displayName}" />	</a>						
							</c:if>
							<c:if test="${empty url}">
  						 		<c:out value="${analyzedData.displayName}" />
							</c:if>
							</li>
					</c:forEach>


				</ol>
			</c:forEach>

		</ol>
		
</body>
