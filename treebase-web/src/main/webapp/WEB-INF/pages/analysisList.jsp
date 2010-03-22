<%@ include file="/common/taglibs.jsp"%>
<%--
	This view displays a study's analyses, possibly editable.
	It needs a org.cipres.treebase.web.model.StudyCommand object
	called "studyCommand" and a boolean "editable".
	Usage:
	<c:set var="studyCommand" value="..." scope="request"/>
	<c:set var="editable" value="true|false" scope="request"/>
	<jsp:include page="analysisList.jsp"/>
--%> 
<script type="text/javascript" src="/treebase-web/scripts/user/analysisEditor.js"></script>
<script type="text/javascript">
	var myAnalysisIDs = new Array();
</script>
<c:set var="analysisStepCounter" value="0" scope="request"/>
<c:set var="redirect" value="redirect:/user/analyses.html" scope="request"/>
<c:forEach var="analysisCommand" items="${studyCommand.analysisCommandList}" varStatus="status_analysis">
	<c:set var="analysisCommand" value="${analysisCommand}" scope="request"/>
	<c:set var="AnalysisId" value="Analysis${status_analysis.count}"/>
	
	<h2>
		<a onclick="TreeBASE.collapseExpand('analysis<c:out value="${analysisCommand.id}"/>','block',this)"
			style="border:none"
			id="analysisCollapser<c:out value="${analysisCommand.id}"/>"
			title="collapse">
			<img 
				class="iconButton" 
				src="<fmt:message 
				key="icons.collapse"/>"
				style="vertical-align:middle" 
				alt="collapse" />
		</a>
		Analysis ${status_analysis.count}
	</h2>
	
	<%-- the block below worked, but gave spurious error messages in eclipse --%>
	<%--fieldset 
		style="padding-left:20px;padding-right:20px;background-color:#E5E5E5;border:none" 
		id="analysis<c:out value="${analysisCommand.id}"/>">
		<c:if test="${editable}">
		<form 
			onsubmit="return TreeBASE.analysisEditor.submitIfNotReady('${publicationState}')"
			method="post" 
			action="/treebase-web/user/analysisForm.html?id=<c:out value="${analysisCommand.id}"/>">
			<fieldset style="background-color:white">
			</c:if>
				<legend>
					Analysis details
					<c:if test="${editable}">
						<a 
							href="#" 
							class="openHelp" 
							onclick="openHelp('analysisDetailsViewEdit')">
							<img class="iconButton" alt="help" src="<fmt:message key="icons.help"/>" />
						</a>					
					</c:if>
				</legend>
				<c:if test="${editable || not empty analysisCommand.name || not empty analysisCommand.notes}">	
					<table width="100%" cellpadding="2px" cellspacing="0">
						<c:if test="${editable || not empty analysisCommand.name}">					
							<tr>
								<td>
									<label class="software" for="analysis${status_analysis.count}name">Name</label>
								</td>
								<td style="width:100%">
									<input 
										readonly="readonly"
										type="text" 
										class="disabled software textCell" 
										<c:if test="${editable}">
										style="width:100%" 
										</c:if>
										<c:if test="${!editable}">
										style="width:100%;background-color:#E5E5E5" 
										</c:if>								
										name="name" 
										id="analysis${status_analysis.count}name" 
										value="<c:out value="${analysisCommand.name}"/>"/>
								</td>
							</tr>
						</c:if>
						<c:if test="${editable || not empty analysisCommand.notes}">
							<tr>
								<td>
									<label class="software" for="analysis${status_analysis.count}notes">Notes</label>
								</td>
								<td>
									<input 
										readonly="readonly"
										type="text" 
										class="disabled software textCell" 
										<c:if test="${editable}">
										style="width:100%" 
										</c:if>
										<c:if test="${!editable}">
										style="width:100%;background-color:#E5E5E5" 
										</c:if>	
										name="notes" 
										id="analysis${status_analysis.count}notes" 
										value="<c:out value="${analysisCommand.notes}"/>"/>
								</td>
							</tr>	
						</c:if>
						<c:if test="${editable}">	
							<tr>
								<td colspan="2" style="text-align:right">
									<input type="hidden" name="redirect" value="${redirect}"/>
									<input type="submit" name="Update" value="Update" style="display:none" />
									<input type="submit" name="Delete" value="Delete" style="display:none" />	
									<c:if test="${empty analysisCommand.notes && empty analysisCommand.name}">
										Edit analysis details by clicking this button ->
									</c:if>					
									<a 
										href="#" 
										onclick="return TreeBASE.analysisEditor.editAnalysis(this,${status_analysis.count})" 
										title="Edit analysis details">
										<img 
											src="<fmt:message 
											key="icons.edit"/>" 
											class="iconButton" 
											width="16" 
											height="16" 
											alt="Edit" 
											style="vertical-align:middle"/>
									</a>
								</td>
							</tr>
						</c:if>
					</table>
				</c:if>
		<c:if test="${editable}">
			</fieldset>
			</form>	
		</c:if--%>	
		
	<fieldset 
		style="padding-left:20px;padding-right:20px;background-color:#E5E5E5;border:none" 
		id="analysis<c:out value="${analysisCommand.id}"/>">	
		<script type="text/javascript">
			myAnalysisIDs.push('<c:out value="${analysisCommand.id}"/>');
		</script>		
		
		<!--  EDITABLE -->				
		<c:if test="${editable}">
		<form 
			onsubmit="return TreeBASE.analysisEditor.submitIfNotReady('${publicationState}')"
			method="post" 
			action="/treebase-web/user/analysisForm.html?id=<c:out value="${analysisCommand.id}"/>">
			<fieldset style="background-color:white">
				<legend>
					Analysis details
					<a 
						href="#" 
						class="openHelp" 
						onclick="openHelp('analysisDetailsViewEdit')">
						<img class="iconButton" alt="help" src="<fmt:message key="icons.help"/>" />
					</a>					
				</legend>
				<table width="100%" cellpadding="2px" cellspacing="0">
					<tr>
						<td>
							<label class="software" for="analysis${status_analysis.count}name">Name</label>
						</td>
						<td style="width:100%">
							<input 
								readonly="readonly"
								type="text" 
								class="disabled software textCell" 
								style="width:100%" 							
								name="name" 
								id="analysis${status_analysis.count}name" 
								value="<c:out value="${analysisCommand.name}"/>"/>
						</td>
					</tr>
					<tr>
						<td>
							<label class="software" for="analysis${status_analysis.count}notes">Notes</label>
						</td>
						<td>
							<input 
								readonly="readonly"
								type="text" 
								class="disabled software textCell" 
								style="width:100%" 
								name="notes" 
								id="analysis${status_analysis.count}notes" 
								value="<c:out value="${analysisCommand.notes}"/>"/>
						</td>
					</tr>	
					<tr>
						<td colspan="2" style="text-align:right">
							<input type="hidden" name="redirect" value="${redirect}"/>
							<input type="submit" name="Update" value="Update" style="display:none" />
							<input type="submit" name="Delete" value="Delete" style="display:none" />	
							<c:if test="${empty analysisCommand.notes && empty analysisCommand.name}">
								Edit analysis details by clicking this button ->
							</c:if>					
							<a 
								href="#" 
								onclick="return TreeBASE.analysisEditor.editAnalysis(this,${status_analysis.count})" 
								title="Edit analysis details">
								<img 
									src="<fmt:message 
									key="icons.edit"/>" 
									class="iconButton" 
									width="16" 
									height="16" 
									alt="Edit" 
									style="vertical-align:middle"/>
							</a>
						</td>
					</tr>
				</table>
				</fieldset>										
			</form>				
		</c:if>	
		<!-- EO EDITABLE -->
		
		<!--  NOT EDITABLE -->
		<c:if test="${!editable}">
			<fieldset 
				style="padding-left:20px;padding-right:20px;background-color:#E5E5E5;border:none" 
				id="analysis<c:out value="${analysisCommand.id}"/>">
				<legend>
					Analysis details
				</legend>
				<c:if test="${not empty analysisCommand.name || not empty analysisCommand.notes}">	
					<table width="100%" cellpadding="2px" cellspacing="0">
						<c:if test="${not empty analysisCommand.name}">					
							<tr>
								<td>
									<label class="software" for="analysis${status_analysis.count}name">Name</label>
								</td>
								<td style="width:100%">
									<input 
										readonly="readonly"
										type="text" 
										class="disabled software textCell" 
										style="width:100%;background-color:#E5E5E5" 	
										name="name" 
										id="analysis${status_analysis.count}name" 
										value="<c:out value="${analysisCommand.name}"/>"/>
								</td>
							</tr>
						</c:if>
						<c:if test="${not empty analysisCommand.notes}">
							<tr>
								<td>
									<label class="software" for="analysis${status_analysis.count}notes">Notes</label>
								</td>
								<td>
									<input 
										readonly="readonly"
										type="text" 
										class="disabled software textCell" 
										style="width:100%;background-color:#E5E5E5" 
										name="notes" 
										id="analysis${status_analysis.count}notes" 
										value="<c:out value="${analysisCommand.notes}"/>"/>
								</td>
							</tr>	
						</c:if>
					</table>
				</c:if>
			</fieldset>
		</c:if>	
		<!-- EO NOT EDITABLE -->		
	
		<div id="${AnalysisId}" style="display:block">
		
			<!--  process each analysis step for the analysis -->
			<c:forEach var="analysisStepCommand" items="${analysisCommand.analysisStepCommandList}" varStatus="status_analysisStep">	
				<c:set var="analysisStepCommand" value="${analysisStepCommand}" scope="request"/>				
				<jsp:include page="analysisStep.jsp"/>
				<c:set var="analysisStepCounter" value="${ analysisStepCounter + 1 }" scope="request" /> 	
				<!-- COUNTER:  <c:out value="${analysisStepCounter}"/> -->	
			</c:forEach>
		</div>
		
		<c:if test="${editable}">
			<div style="width:100%;text-align:right">
				<form 
				    method="post"  
				    action="/treebase-web/user/analysisStepForm.html?analysis_id=${analysisCommand.id}">
				    <input type="hidden" name="redirect" value="${redirect}"/>				    
				    <input type="hidden" name="id" value=""/>
				    <input type="hidden" name="name" value=""/>
				    <input type="hidden" name="notes" value=""/>
				    <input type="hidden" name="commands" value=""/>
				    <input type="hidden" name="softwareInfo.name" value=""/>
				    <input type="hidden" name="softwareInfo.softwareVersion" value=""/>
				    <input type="hidden" name="softwareInfo.softwareURL" value=""/>
				    <input type="hidden" name="softwareInfo.description" value=""/>
				    <input type="hidden" name="algorithmType" value=""/>
				    <input type="hidden" name="Submit" value="Submit"/>
				    <c:if test="${empty analysisCommand.analysisStepCommandList}">
				    	Add your first step to this analysis by clicking this button ->
				    </c:if>
					<a href="#" onclick="return TreeBASE.analysisEditor.addStep(this)" title="Add analysis step">
						<img 
							src="<fmt:message 
							key="icons.add"/>" 
							class="iconButton" 
							width="16" 
							height="16" 
							alt="Add" 
							style="vertical-align:middle"/>
					</a>				    
				</form>			
			</div>
		</c:if>
	</fieldset>
</c:forEach>

<c:if test="${editable}">
	<div style="width:100%;text-align:right">
		<form 
		    method="post"  
		    action="/treebase-web/user/analysisForm.html">
		    <input type="hidden" name="name" value=""/>
		    <input type="hidden" name="notes" value=""/>
		    <input type="hidden" name="Submit" value="Submit"/>
			<input type="hidden" name="redirect" value="${redirect}"/>		 
			<c:if test="${empty studyCommand.analysisCommandList}">
				Add your first analysis by clicking this button ->
			</c:if>   
			<a href="#" onclick="return TreeBASE.analysisEditor.addStep(this)" title="Add analysis">
				<img 
					src="<fmt:message 
					key="icons.add"/>" 
					class="iconButton" 
					width="16" 
					height="16" 
					alt="Add" 
					style="vertical-align:middle"/>
			</a>				    
		</form>			
	</div>
</c:if>

<c:if test="${editable}">
	<script type="text/javascript">
		for ( var i = 0; i < ( myAnalysisIDs.length - 1 ); i++ ) {
			TreeBASE.collapseExpand(
				'analysis'+myAnalysisIDs[i],
				'block',
				$('analysisCollapser'+myAnalysisIDs[i])
			);
		}
	</script>
</c:if>