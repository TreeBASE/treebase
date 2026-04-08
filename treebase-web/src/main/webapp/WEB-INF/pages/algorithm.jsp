<%@ include file="/common/taglibs.jsp"%>
<c:set var="counter" value="${analysisStepCounter}"/>
<form 
	method="post" 
	onsubmit="return TreeBASE.analysisEditor.checkValue(this,${counter},'${publicationState}')" 
	id="form${counter}" 
	action="/treebase-web/user/analysisStepForm.html?id=<c:out value="${analysisStepCommand.id}"/>">
<input type="hidden" name="redirect" value="${redirect}"/>

<div class="card shadow-sm mb-3">
	<div class="card-header d-flex justify-content-between align-items-center">
		<div class="d-flex align-items-center">
			<a onclick="TreeBASE.collapseExpand('analysisStepCommand<c:out value="${analysisStepCommand.id}"/>','block',this)"
				class="text-decoration-none me-2"
				title="collapse"
				role="button">
				<c:if test="${editable}">
					<i class="fa fa-chevron-down"></i>
				</c:if>
				<c:if test="${!editable}">
					<i class="fa fa-chevron-right"></i>
				</c:if>
			</a>
			<span class="fw-semibold"><i class="fa fa-gear me-1"></i> Analysis Step</span>
			<c:if test="${editable}">
				<a href="#" class="openHelp ms-2" onclick="openHelp('analysisStepDetailsViewEdit')">
					<i class="fa fa-question-circle text-muted"></i>
				</a>
			</c:if>
		</div>
		<div class="d-flex align-items-center gap-2">
			<span class="isAnalysisStepValid" title="${analysisStepCommand.id}" style="display:none">
				<span class="badge bg-warning text-dark" title="<fmt:message key="analysis.notvalidated"/>">
					<i class="fa fa-exclamation-triangle"></i> Not Validated
				</span>
			</span>
			<a href="/treebase-web/search/downloadAnAnalysisStep.html?analysisid=${analysisStepCommand.id}&id=${analysisStepCommand.analysis.study.id}"
				class="btn btn-sm btn-outline-primary"
				title="<fmt:message key="download.reconstructedfile"/>">
				<i class="fa fa-download"></i> Download
			</a>
		</div>
	</div>
	
	<div class="card-body">
		<h6 class="mb-3">
			<c:if test="${not empty analysisStepCommand.displayName}">
				<c:out value="${analysisStepCommand.displayName}"/>
			</c:if>
			<c:if test="${empty analysisStepCommand.displayName}">
				<em class="text-muted">Untitled</em>
			</c:if>
		</h6>
		
		<div <c:if test="${!editable}">style="display:none"</c:if> id="analysisStepCommand<c:out value="${analysisStepCommand.id}"/>">
			
			<%-- Step Details Section --%>
			<c:if test="${editable || ( not empty analysisStepCommand.displayName || not empty analysisStepCommand.notes )}">
			<div class="card mb-3">
				<div class="card-header py-2">
					<span class="fw-semibold"><i class="fa fa-info-circle me-1"></i> Step Details</span>
				</div>
				<div class="card-body">
					<c:if test="${editable || not empty analysisStepCommand.displayName}">
						<div class="row mb-2">
							<label for="name${counter}" class="col-sm-3 col-form-label">Name</label>
							<div class="col-sm-9">
								<input 
									readonly="readonly"
									class="form-control form-control-sm" 
									type="text" 
									name="name" 
									id="name${counter}" 
									value="<c:out value="${analysisStepCommand.displayName}"/>"/>
							</div>
						</div>
					</c:if>
					<c:if test="${editable || not empty analysisStepCommand.notes}">
						<div class="row mb-2">
							<label for="notes${counter}" class="col-sm-3 col-form-label">Notes</label>
							<div class="col-sm-9">
								<input 
									readonly="readonly"
									class="form-control form-control-sm" 
									type="text" 
									name="notes" 
									id="notes${counter}" 
									value="<c:out value="${analysisStepCommand.notes}"/>"/>
							</div>
						</div>
					</c:if>
				</div>
			</div>
			</c:if>
			
			<%-- Software Used Section --%>
			<c:if test="${editable || not empty analysisStepCommand.softwareInfo.name}">
			<div class="card mb-3">
				<div class="card-header py-2">
					<span class="fw-semibold"><i class="fa fa-code-square me-1"></i> Software Used</span>
				</div>
				<div class="card-body">
					<c:if test="${editable || not empty analysisStepCommand.softwareInfo.name}">
						<div class="row mb-2">
							<label for="softwareInfo.name${counter}" class="col-sm-3 col-form-label">Name</label>
							<div class="col-sm-9">
								<input
									readonly="readonly"
									class="form-control form-control-sm"
									type="text" 
									id="softwareInfo.name${counter}"  
									name="softwareInfo.name" 
									value="<c:out value="${analysisStepCommand.softwareInfo.name}"/>"/>
								<div id="aSoftwareNameList${counter}" class="auto_complete"></div>
								<script type="text/javascript">
									new Autocompleter.DWR( 
										'softwareInfo.name${counter}', 
										'aSoftwareNameList${counter}',  
										updateSoftwareNameList,
										{ valueSelector: nameValueSelector, partialChars: 0 }
									);
								</script>
								<span class="fieldError text-danger small"></span>
							</div>
						</div>
					</c:if>
					<c:if test="${editable || not empty analysisStepCommand.softwareInfo.softwareVersion}">
						<div class="row mb-2">
							<label for="softwareInfo.softwareVersion${counter}" class="col-sm-3 col-form-label">Version</label>
							<div class="col-sm-9">
								<input  
									readonly="readonly"
									class="form-control form-control-sm"
									type="text" 
									id="softwareInfo.softwareVersion${counter}"     	
									name="softwareInfo.softwareVersion" 
									value="<c:out value="${analysisStepCommand.softwareInfo.softwareVersion}"/>"/>
								<span class="fieldError text-danger small"></span>
							</div>
						</div>
					</c:if>
					<c:if test="${editable || not empty analysisStepCommand.softwareInfo.softwareURL}">
						<div class="row mb-2">
							<label for="softwareInfo.softwareURL${counter}" class="col-sm-3 col-form-label">URL</label>
							<div class="col-sm-9">
								<%-- displayed in edit mode --%>
								<input 
									style="display:none"
									class="form-control form-control-sm"
									type="text" 
									id="softwareInfo.softwareURL${counter}"
									name="softwareInfo.softwareURL" 
									value="<c:out value="${analysisStepCommand.softwareInfo.softwareURL}"/>"/> 
								<span class="fieldError text-danger small"></span>
								
								<%-- displayed in view mode --%>
								<c:if test="${not empty analysisStepCommand.softwareInfo.softwareURL}">
									<a
										class="btn btn-sm btn-outline-secondary"
										id="softwareInfo.softwareLink${counter}"
										href="<c:out value="${analysisStepCommand.softwareInfo.softwareURL}"/>"
										target="_blank">
										<i class="fa fa-box-arrow-up-right"></i> Visit Website
									</a>
								</c:if>
							</div>
						</div>
					</c:if>
					<c:if test="${editable || not empty analysisStepCommand.softwareInfo.description}">
						<div class="row mb-2">
							<label for="softwareInfo.description${counter}" class="col-sm-3 col-form-label">Description</label>
							<div class="col-sm-9">
								<input 
									readonly="readonly"			         	
									class="form-control form-control-sm"
									type="text" 
									id="softwareInfo.description${counter}"
									name="softwareInfo.description" 
									value="<c:out value="${analysisStepCommand.softwareInfo.description}"/>"/>	            
								<span class="fieldError text-danger small"></span>
							</div>
						</div>
					</c:if>
					<c:if test="${editable || not empty analysisStepCommand.algorithmInfo.description}">
						<spring:bind path="analysisStepCommand.algorithmType">
							<div class="row mb-2">
								<label for="algorithmTypeInput${counter}" class="col-sm-3 col-form-label">Algorithm</label>
								<div class="col-sm-9">
									<%-- this part is displayed in view mode --%>
									<input 
										readonly="readonly"
										class="form-control form-control-sm"
										type="text" 
										id="algorithmTypeInput${counter}"
										value="<c:out value="${analysisStepCommand.algorithmInfo.description}"/>"/>
									<span class="fieldError text-danger small"></span>
									
									<%-- this part is displayed in edit mode --%>
									<div id="algorithmSelectWidget${counter}" style="display:none">
										<select name="${status.expression}" class="form-select form-select-sm" onchange="TreeBASE.analysisEditor.checkOther(this,${counter})">
											<option value="">--- Please Select ---</option>
											<c:forEach var="type" items="${algorithmtypes}">
												<option value="${type}" 
													<c:if test="${type == analysisStepCommand.algorithmType}">selected="selected"</c:if> >
													<c:out value="${type}"/>
												</option>
											</c:forEach>
										</select>
										<c:set var="algorithmType" value="other algorithm"/>
										<spring:bind path="analysisStepCommand.algorithmMap[${algorithmType}].description">
											<div id="ac${counter}" class="mt-2" <c:if test="${analysisStepCommand.algorithmInfo.description != 'other algorithm'}">style="display:none"</c:if>>
												<input 
													class="form-control form-control-sm"
													type="text" 
													id="algorithmType${counter}" 
													name="<c:out value="${status.expression}"/>" 
													value="<c:out value="${analysisStepCommand.algorithmInfo.description}"/>"
													placeholder="Specify other algorithm"/>
												<div id="aUniqueOtherAlgorithmList${counter}" class="auto_complete"></div>
												<script type="text/javascript">
													new Autocompleter.DWR( 
														'algorithmType${counter}', 
														'aUniqueOtherAlgorithmList${counter}',  
														updateUniqueOtherAlgorithmList,
														{ valueSelector: nameValueSelector, partialChars: 0 }
													);
												</script>
											</div>
										</spring:bind>
									</div>
								</div>
							</div>
						</spring:bind>
					</c:if>
					<c:if test="${editable || not empty analysisStepCommand.commands}">
						<div class="row mb-2">
							<label for="commands${counter}" class="col-sm-3 col-form-label">Commands</label>
							<div class="col-sm-9">
								<textarea 
									readonly="readonly"
									class="form-control form-control-sm font-monospace"
									rows="4"
									id="commands${counter}"
									name="commands"><c:out value="${analysisStepCommand.commands}"/></textarea>
								<span class="fieldError text-danger small"></span>
							</div>
						</div>
					</c:if>
				</div>
			</div>
			</c:if>
			
			<%-- Edit Actions --%>
			<c:if test="${editable}">
			<div class="d-flex justify-content-end gap-2 mt-3">
				<input style="display:none" type="submit" name="Update" value="<fmt:message key="button.update"/>" />
				<input style="display:none" type="submit" name="Delete" value="<fmt:message key="button.delete"/>" />
				<a href="#" class="btn btn-sm btn-outline-primary" onclick="return TreeBASE.analysisEditor.enableEdit(${counter})" title="Edit analysis step details">
					<i class="fa fa-pencil"></i> Edit
				</a>
			</div>
			</c:if>
		</div>
	</div>
</div>
</form>
