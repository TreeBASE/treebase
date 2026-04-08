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
	
	<div class="card shadow-sm mb-4">
		<div class="card-header bg-secondary text-white d-flex align-items-center justify-content-between">
			<div class="d-flex align-items-center">
				<a onclick="TreeBASE.collapseExpand('analysis<c:out value="${analysisCommand.id}"/>','block',this)"
					class="text-white me-2"
					id="analysisCollapser<c:out value="${analysisCommand.id}"/>"
					title="collapse"
					style="cursor:pointer">
					<i class="fa fa-chevron-down"></i>
				</a>
				<i class="fa fa-bar-chart me-2"></i>
				<span>Analysis ${status_analysis.count}</span>
			</div>
		</div>
		
		<div id="analysis<c:out value="${analysisCommand.id}"/>" class="card-body">
			<script type="text/javascript">
				myAnalysisIDs.push('<c:out value="${analysisCommand.id}"/>');
			</script>		
			
			<!--  EDITABLE -->				
			<c:if test="${editable}">
				<form 
					onsubmit="return TreeBASE.analysisEditor.submitIfNotReady('${publicationState}')"
					method="post" 
					action="/treebase-web/user/analysisForm.html?id=<c:out value="${analysisCommand.id}"/>">
					<div class="card mb-3 border-primary">
						<div class="card-header bg-light d-flex align-items-center justify-content-between">
							<span><i class="fa fa-info-circle me-2"></i>Analysis Details</span>

							<tb:helpButton topic="analysisDetailsViewEdit"/>				
						</div>
						<div class="card-body">
							<div class="row mb-2">
								<label class="col-sm-2 col-form-label" for="analysis${status_analysis.count}name">Name</label>
								<div class="col-sm-10">
									<input 
										readonly="readonly"
										type="text" 
										class="form-control disabled" 						
										name="name" 
										id="analysis${status_analysis.count}name" 
										value="<c:out value="${analysisCommand.name}"/>"/>
								</div>
							</div>
							<div class="row mb-2">
								<label class="col-sm-2 col-form-label" for="analysis${status_analysis.count}notes">Notes</label>
								<div class="col-sm-10">
									<input 
										readonly="readonly"
										type="text" 
										class="form-control disabled" 
										name="notes" 
										id="analysis${status_analysis.count}notes" 
										value="<c:out value="${analysisCommand.notes}"/>"/>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12 text-end">
									<input type="hidden" name="redirect" value="${redirect}"/>
									<input type="submit" name="Update" value="Update" style="display:none" />
									<input type="submit" name="Delete" value="Delete" style="display:none" />	
									<c:if test="${empty analysisCommand.notes && empty analysisCommand.name}">
										<span class="text-muted me-2">Edit analysis details by clicking this button:</span>
									</c:if>					
									<a 
										href="#" 
										onclick="return TreeBASE.analysisEditor.editAnalysis(this,${status_analysis.count})" 
										title="Edit analysis details"
										class="btn btn-outline-primary btn-sm">
										<i class="fa fa-pencil"></i> Edit
									</a>
								</div>
							</div>
						</div>
					</div>
				</form>				
			</c:if>	
			<!-- EO EDITABLE -->
			
			<!--  NOT EDITABLE -->
			<c:if test="${!editable}">
				<c:if test="${not empty analysisCommand.name || not empty analysisCommand.notes}">
					<div class="card mb-3 border-secondary">
						<div class="card-header bg-light">
							<i class="fa fa-info-circle me-2"></i>Analysis Details
						</div>
						<div class="card-body">
							<c:if test="${not empty analysisCommand.name}">					
								<div class="row mb-2">
									<label class="col-sm-2 col-form-label" for="analysis${status_analysis.count}name">Name</label>
									<div class="col-sm-10">
										<input 
											readonly="readonly"
											type="text" 
											class="form-control-plaintext bg-light" 
											name="name" 
											id="analysis${status_analysis.count}name" 
											value="<c:out value="${analysisCommand.name}"/>"/>
									</div>
								</div>
							</c:if>
							<c:if test="${not empty analysisCommand.notes}">
								<div class="row">
									<label class="col-sm-2 col-form-label" for="analysis${status_analysis.count}notes">Notes</label>
									<div class="col-sm-10">
										<input 
											readonly="readonly"
											type="text" 
											class="form-control-plaintext bg-light" 
											name="notes" 
											id="analysis${status_analysis.count}notes" 
											value="<c:out value="${analysisCommand.notes}"/>"/>
									</div>
								</div>
							</c:if>
						</div>
					</div>
				</c:if>
			</c:if>	
			<!-- EO NOT EDITABLE -->		
		
			<div id="${AnalysisId}" style="display:block">
				<!--  process each analysis step for the analysis -->
				<c:forEach var="analysisStepCommand" items="${analysisCommand.analysisStepCommandList}" varStatus="status_analysisStep">	
					<c:set var="analysisStepCommand" value="${analysisStepCommand}" scope="request"/>				
					<jsp:include page="analysisStep.jsp"/>
					<c:set var="analysisStepCounter" value="${ analysisStepCounter + 1 }" scope="request" /> 	
				</c:forEach>
			</div>
			
			<c:if test="${editable}">
				<div class="text-end mt-3">
					<form 
					    method="post"  
					    action="/treebase-web/user/analysisStepForm.html?analysis_id=${analysisCommand.id}"
					    class="d-inline">
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
					    	<span class="text-muted me-2">Add your first step to this analysis:</span>
					    </c:if>
						<a href="#" onclick="return TreeBASE.analysisEditor.addStep(this)" 
						   title="Add analysis step" class="btn btn-success btn-sm">
							<i class="fa fa-plus-circle me-1"></i> Add Step
						</a>				    
					</form>			
				</div>
			</c:if>
		</div>
	</div>
</c:forEach>

<c:if test="${editable}">
	<div class="text-end mt-3">
		<form 
		    method="post"  
		    action="/treebase-web/user/analysisForm.html"
		    class="d-inline">
		    <input type="hidden" name="name" value=""/>
		    <input type="hidden" name="notes" value=""/>
		    <input type="hidden" name="Submit" value="Submit"/>
			<input type="hidden" name="redirect" value="${redirect}"/>		 
			<c:if test="${empty studyCommand.analysisCommandList}">
				<span class="text-muted me-2">Add your first analysis:</span>
			</c:if>   
			<a href="#" onclick="return TreeBASE.analysisEditor.addStep(this)" 
			   title="Add analysis" class="btn btn-primary">
				<i class="fa fa-plus-circle me-1"></i> Add Analysis
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