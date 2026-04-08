<%@ include file="/common/taglibs.jsp"%>
<div class="card shadow-sm mb-3">
	<div class="card-header d-flex justify-content-between align-items-center">
		<div class="d-flex align-items-center">
			<a onclick="TreeBASE.collapseExpand('analyzedData${inputOutput}<c:out value="${analysisStepCommand.id}"/>','block',this)"
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
			<span class="fw-semibold">
				<c:if test="${inputOutput == 'Input'}"><i class="fa fa-sign-in-alt me-1"></i></c:if>
				<c:if test="${inputOutput == 'Output'}"><i class="fa fa-share-square me-1"></i></c:if>
				${inputOutput} Data
			</span>
			<c:if test="${editable}">
				<a href="#" class="openHelp ms-2" onclick="openHelp('analysisStep${inputOutput}DataDetailsViewEdit')">
					<i class="fa fa-question-circle text-muted"></i>
				</a>
			</c:if>
		</div>
		<c:set var="dataCount" value="0"/>
		<c:forEach var="analyzedData" items="${analysisStepCommand.analyzedDataCommandList}">
			<c:if test="${analyzedData.inputOutputType == inputOutput}"><c:set var="dataCount" value="${dataCount + 1 }"/></c:if>
		</c:forEach>
		<span class="badge bg-secondary">${dataCount} object(s)</span>
	</div>
	
	<div <c:if test="${!editable}">style="display:none"</c:if> id="analyzedData${inputOutput}<c:out value="${analysisStepCommand.id}"/>">
		<ul class="list-group list-group-flush">
			<c:forEach var="analyzedData" items="${analysisStepCommand.analyzedDataCommandList}">
				<c:if test="${analyzedData.inputOutputType == inputOutput}">
					<li class="list-group-item d-flex justify-content-between align-items-center">
						<div class="d-flex align-items-center">
							<c:choose>
								<c:when test="${analyzedData.dataType == 'tree'}">
									<i class="fa fa-project-diagram text-success me-2" title="Tree"></i>
								</c:when>
								<c:when test="${analyzedData.dataType == 'matrix'}">
									<i class="fa fa-th text-primary me-2" title="Matrix"></i>
								</c:when>
								<c:when test="${analyzedData.dataType == 'treeBlock'}">
									<i class="fa fa-th-list text-info me-2" title="Tree Block"></i>
								</c:when>
							</c:choose>
							<span><c:out value="${analyzedData.displayName}"/></span>
						</div>
						<c:if test="${editable}">
							<c:choose>
								<c:when test="${analyzedData.dataType == 'tree'}">
									<form 
										method="post" 
										action="/treebase-web/user/addAnalyzedData.html" 
										class="d-inline"
										title="Remove ${inputOutput} Tree">
										<input type="hidden" name="deleteMe" value="<c:out value="${analyzedData.id}"/>"/>
										<input type="hidden" name="action" value="remove"/>
										<input type="hidden" name="dataType" value="Trees"/>
										<input type="hidden" name="inputOutput" value="${inputOutput}"/>
										<input type="hidden" name="analysisStepId" value="<c:out value="${analysisStepCommand.id}"/>"/>
										<button type="submit" class="btn btn-sm btn-outline-danger" title="Remove">
											<i class="fa fa-trash"></i>
										</button>
									</form>
								</c:when>
								<c:when test="${analyzedData.dataType == 'matrix'}">
									<form 
										method="post" 
										action="/treebase-web/user/addAnalyzedData.html" 
										class="d-inline"
										title="Remove ${inputOutput} Matrix">
										<input type="hidden" name="deleteMe" value="<c:out value="${analyzedData.id}"/>"/>
										<input type="hidden" name="action" value="remove"/>
										<input type="hidden" name="dataType" value="Matrices"/>
										<input type="hidden" name="inputOutput" value="${inputOutput}"/>
										<input type="hidden" name="analysisStepId" value="<c:out value="${analysisStepCommand.id}"/>"/>
										<button type="submit" class="btn btn-sm btn-outline-danger" title="Remove">
											<i class="fa fa-trash"></i>
										</button>
									</form>
								</c:when>
							</c:choose>
						</c:if>
					</li>
				</c:if>
			</c:forEach>
		</ul>
		
		<c:if test="${editable}">
			<div class="card-footer">
				<div class="d-flex flex-column gap-2">
					<a href="#" class="btn btn-sm btn-outline-primary" onclick="return TreeBASE.analysisEditor.addData(this)" title="Add ${inputOutput} Data">
						<i class="fa fa-plus-circle"></i> Add ${inputOutput} Data
					</a>
					<select class="form-select form-select-sm" style="display:none" onchange="TreeBASE.analysisEditor.selectData(this)">
						<option>---select data type---</option>
						<option value="Matrices">Matrices</option>
						<option value="TreeBlocks">Tree blocks</option>
						<option value="Trees">Trees</option>
					</select>
					<select class="form-select form-select-sm" style="display:none" multiple="multiple" size="10">
						<option>---select data---</option>
					</select>
					<button type="button" class="btn btn-sm btn-primary" style="display:none" onclick="TreeBASE.analysisEditor.addSelected(this,'${inputOutput}',<c:out value="${analysisStepCommand.id}"/>)">Add selected</button>
					<div></div>
				</div>
			</div>
		</c:if>
	</div>
</div>