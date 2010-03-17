<%@ include file="/common/taglibs.jsp"%>

<title>Analyses for study ${study.id}</title>
<body id="s-analysis"/>

<c:set var="currentSection" scope="request" value="Analyses"/>
<c:set var="headerPrefix" scope="request" value="${currentSection} for "/>
<jsp:include page="nav.jsp"/>

<%--
<ol id="AnalysisSection" style="display: block">

	<!--  process each analysis -->
	<c:forEach var="analysis" items="${study.analyses}"
		varStatus="status_analysis">
		<c:set var="AnalysisId" value="Analysis${status_analysis.count}" />

		<li><img src=<c:url value="/images/minus.gif"/>
			name="img${AnalysisId}" width="9" height="9" border="0"
			onClick="CollapseExpand('${AnalysisId}')"> 
			<c:url var="analysisUrl" value="analysis.html">
				<c:param name="analysisid" value="${analysis.id}"/>
				<c:param name="id" value="${study.id}"/>
			</c:url>
			<strong><a href='${analysisUrl}'>Analysis
		${status_analysis.count}</a>:</strong><c:out value="${analysisCommand.name}" />


		<ol id="${AnalysisId}" style="display: block">
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
						onClick="CollapseExpand('${AnalysisStepId}')" /> 
                                        <!-- commented out due to issue# 2970700 and issue# 2970457 -->
					<!-- <a href="/treebase-web/search/downloadAnAnalysisStep.html?analysisid=${analysisStep.id}"> -->
						<img 
							class="iconButton" 
							src="<fmt:message key="icons.download.reconstructed"/>" 
							title="<fmt:message key="download.reconstructedfile"/>" 
							alt="<fmt:message key="download.reconstructedfile"/>"/>	
					<!-- </a> --->
					<strong>Analysis Step ${status_analysisStep.count}:</strong>
					<c:out value="${analysisStepCommand.name}" />
				</li>

				<!--  process information related to analysis step -->
				<ul id="${AnalysisStepId}" style="display: block">
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
						
						<li><strong><span style="text-transform: capitalize;">${analyzedData.inputOutput}</span>
						            <span style="text-transform: capitalize;">${analyzedData.dataType}</span>
						     </strong>
							<c:if test="${not empty url}">
							    <a href='${url}'>
  						 		<c:out value="${analyzedData.displayName}" />	</a>						
							</c:if>
							<c:if test="${empty url}">
  						 		<c:out value="${analyzedData.displayName}" />
							</c:if>
							</li>
					</c:forEach>


				</ul>
			</c:forEach>

		</ol>
		</li>
	</c:forEach>
</ol>

<script type="text/javascript">
function CollapseExpand(id) { 

        if (document.getElementById) { // DOM3 = IE5, NS6
                if (document.getElementById(id).style.display == "none"){
                        document.getElementById(id).style.display = 'block';
                        document.images["img"+id].src="/treebase-web/images/minus.gif";
                } else {
                        document.getElementById(id).style.display = 'none';
                        document.images["img"+id].src="/treebase-web/images/plus.gif";
                }
        } else { 
                if (document.layers) {
                        if (document.id.display == "none"){
                                document.id.display = 'block';
                                document.images["img"+id].src="/treebase-web/images/minus.gif";
                        } else {
                                document.id.display = 'none';
                                document.images["img"+id].src="/treebase-web/images/plus.gif";
                        }
                } else {
                        if (document.all.id.style.visibility == "none"){
                                document.all.id.style.display = 'block';
                                document.images["img"+id].src="/treebase-web/images/minus.gif";
                        } else {
                                document.all.id.style.display = 'none';
                                document.images["img"+id].src="/treebase-web/images/plus.gif";
                        }
                }
        }
}

</script>
--%>

<br/>

	<!-- imports & variables necessary before running analysis jsps -->
	<c:set var="editable" value="${false}" scope="request"/>
	<!-- also need studyCommand from controller -->
	
	<!-- now run analysis jsps -->
	<jsp:include page="../../analysisList.jsp"/>