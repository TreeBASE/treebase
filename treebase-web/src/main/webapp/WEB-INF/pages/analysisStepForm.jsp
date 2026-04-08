<%@ include file="/common/taglibs.jsp"%>

<script language="Javascript"><!--

		
        function showDiv(divName, show) {
          if (show) {
            display = '';
          } else {
            display = 'none';
          }
          
          document.getElementById(divName).style.display = display;
        }

        function showAndHideDivs(divToShow) {
          var indexvalue = document.getElementById('algorithmType');
          if(indexvalue.selectedIndex ==  indexvalue.length - 1){
          	showDiv('algorithmSpan', true);
          } else {
          	showDiv('algorithmSpan', false);
          }
     
        }

		function checkValue(form)
		{
			if (form.algorithmType.value == "Other Algorithm")
			{
				var newAlgorithm = form.newAlgorithm.value;
				if (newAlgorithm.length <= 0)
				{
					alert("New Algorithm must not be null when Other Algorithm is selected");
					return false;
				}
			}
		}
        
--></script>

<title><fmt:message key="analysis.step.title"/></title>
<content tag="heading"><fmt:message key="analysis.step.title"/></content>
<body id="submissions"/>

<div class="container py-5">
	<spring:bind path="step.*">
		<c:if test="${not empty status.errorMessages}">
			<div class="alert alert-danger d-flex align-items-start mb-4" role="alert">
				<i class="fa fa-exclamation-triangle me-2 fs-5"></i>
				<div>
					<c:forEach var="error" items="${status.errorMessages}">
						<c:out value="${error}" escapeXml="false"/><br/>
					</c:forEach>
				</div>
			</div>
		</c:if>
	</spring:bind>

	<form method="post" onsubmit="return checkValue(this)">
		<div class="card shadow-lg mb-4">
			<div class="card-header bg-primary text-white d-flex align-items-center justify-content-between">
				<div>
					<i class="fa fa-gear me-2"></i>
					<span>Analysis Step - Information</span>
				</div>

				<tb:helpButton topic="analysisStepInfo"/>
			</div>
			<div class="card-body">
				<p class="text-muted mb-4">
					Please complete the following analysis step information for <strong>analysis ${analysisMap['id']} - ${analysisMap['name']}</strong>.
				</p>
				<input type="hidden" name="id" value="${status.value}"/>
				
				<c:choose>
					<c:when test="${empty step.algorithmType }">
						<c:set var="algorithmType" value="maximum likelihood"/>
					</c:when>
					<c:otherwise>
						<c:set var="algorithmType" value="${step.algorithmType}"/>
					</c:otherwise>
				</c:choose>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label"><fmt:message key="analysis.step.name"/>:</label>
					<div class="col-sm-9">
						<spring:bind path="step.name">
							<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
							<c:if test="${not empty status.errorMessage}">
								<div class="invalid-feedback d-block"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label"><fmt:message key="analysis.step.notes"/>:</label>
					<div class="col-sm-9">
						<spring:bind path="step.notes">
							<textarea rows="2" class="form-control" name="<c:out value="${status.expression}"/>">${status.value}</textarea>
							<c:if test="${not empty status.errorMessage}">
								<div class="invalid-feedback d-block"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label"><fmt:message key="analysis.step.commands"/>:</label>
					<div class="col-sm-9">
						<spring:bind path="step.commands">
							<textarea rows="2" class="form-control" name="<c:out value="${status.expression}"/>">${status.value}</textarea>
							<c:if test="${not empty status.errorMessage}">
								<div class="invalid-feedback d-block"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>
				
				<hr class="my-4">
				<h6 class="text-muted mb-3"><i class="fa fa-microchip me-1"></i> Software Information</h6>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label"><fmt:message key="analysis.step.software.name"/>:</label>
					<div class="col-sm-9">
						<spring:bind path="step.softwareInfo.name">
							<div id="ac">
								<input class="form-control" type="text" id="<c:out value="${status.expression}"/>" 
									   name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
								<div id="aSoftwareNameList" class="auto_complete"></div>
								<script type="text/javascript">
									new Autocompleter.DWR('<c:out value="${status.expression}"/>', 'aSoftwareNameList', updateSoftwareNameList, {valueSelector: nameValueSelector, partialChars: 0});
								</script>
							</div>
							<c:if test="${not empty status.errorMessage}">
								<div class="invalid-feedback d-block"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label"><fmt:message key="analysis.step.software.version"/>:</label>
					<div class="col-sm-9">
						<spring:bind path="step.softwareInfo.softwareVersion">
							<input type="text" class="form-control" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
							<c:if test="${not empty status.errorMessage}">
								<div class="invalid-feedback d-block"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label"><fmt:message key="analysis.step.software.url"/>:</label>
					<div class="col-sm-9">
						<spring:bind path="step.softwareInfo.softwareURL">
							<input class="form-control" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
							<c:if test="${not empty status.errorMessage}">
								<div class="invalid-feedback d-block"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label"><fmt:message key="analysis.step.software.description"/>:</label>
					<div class="col-sm-9">
						<spring:bind path="step.softwareInfo.description">
							<input class="form-control" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
							<c:if test="${not empty status.errorMessage}">
								<div class="invalid-feedback d-block"><c:out value="${status.errorMessage}"/></div>
							</c:if>
						</spring:bind>
					</div>
				</div>
				
				<hr class="my-4">
				<h6 class="text-muted mb-3"><i class="fa fa-project-diagram me-1"></i> Algorithm Information</h6>
				
				<div class="row mb-3">
					<label class="col-sm-3 col-form-label"><fmt:message key="analysis.step.algorithm.type"/>:</label>
					<div class="col-sm-4">
						<spring:bind path="step.algorithmType">
							<select name="${status.expression}" id="${status.expression}" class="form-select" onchange="formSubmit(form)">
								<option value="">--- Please Select ---</option>
								<c:forEach var="type" items="${algorithmtypes}">
									<option value="${type}" <c:if test="${type == step.algorithmType}">selected="true"</c:if>>
										<c:out value="${type}"/>
									</option>
								</c:forEach>
							</select>
						</spring:bind>
					</div>
					<c:if test="${step.algorithmType =='other algorithm'}">
						<label class="col-sm-2 col-form-label">New Algorithm:</label>
						<div class="col-sm-3">
							<spring:bind path="step.algorithmMap[${algorithmType}].description">
								<div id="ac">
									<input type="text" class="form-control" id="<c:out value="${status.expression}"/>" 
										   name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
									<div id="aUniqueOtherAlgorithmList" class="auto_complete"></div>
									<script type="text/javascript">
										new Autocompleter.DWR('<c:out value="${status.expression}"/>', 'aUniqueOtherAlgorithmList', updateUniqueOtherAlgorithmList, {valueSelector: nameValueSelector, partialChars: 0});
									</script>
								</div>
							</spring:bind>
						</div>
					</c:if>
				</div>
			</div>
			<c:if test="${publicationState eq 'NotReady'}">
				<div class="card-footer">
					<div class="btn-group">
						<c:choose>
							<c:when test="${empty step.id}">
								<button type="submit" name="Submit" class="btn btn-success">
									<i class="fa fa-check-circle me-1"></i> <fmt:message key="button.submit"/>
								</button>
							</c:when>
							<c:otherwise>
								<button type="submit" name="Update" class="btn btn-primary">
									<i class="fa fa-save me-1"></i> <fmt:message key="button.update"/>
								</button>
								<button type="submit" name="Delete" class="btn btn-danger">
									<i class="fa fa-trash me-1"></i> <fmt:message key="button.delete"/>
								</button>
							</c:otherwise>
						</c:choose>
						<button type="submit" name="Reset" class="btn btn-outline-secondary">
							<i class="fa fa-undo me-1"></i> <fmt:message key="button.reset"/>
						</button>
						<button type="submit" name="_cancel" class="btn btn-outline-secondary">
							<i class="fa fa-times-circle me-1"></i> <fmt:message key="button.cancel"/>
						</button>
					</div>
				</div>
			</c:if>
		</div>
	</form>
	
	<script type="text/javascript">
	function formSubmit(form) {
		form.submit();
	}
	</script>

	<c:if test="${! empty step.id }">
		<div class="card shadow-lg">
			<div class="card-header bg-info text-white d-flex align-items-center justify-content-between">
				<div>
					<i class="fa fa-database me-2"></i>
					<span>Analyzed Data</span>
				</div>

				<tb:helpButton topic="analyzedDataList"/>
			</div>
			<div class="card-body">
				<div class="mb-3">
					<a href="/treebase-web/user/analyzedDataForm.html?analysis_step_id=${step.id}" class="btn btn-success btn-sm">
						<i class="fa fa-plus-circle me-1"></i> Add Data
					</a>
				</div>
				
				<c:set var="updateAnalyzedDataURL" value="/treebase-web/user/updateAnalyzedDataList.html?id="/>
				<c:set var="counter" value="0"/>
				<display:table name="requestScope.step.analyzedDataCommandList"
							   requestURI=""
							   defaultsort="1"
							   class="table table-striped table-hover"
							   id="userList">
					
					<display:column property="displayName" title="Analysis data name" sortable="true"/>
					<display:column property="dataType" title="Data type" sortable="true"/>
					<display:column property="inputOutputType" title="Input/output" sortable="true"/>
					
					<display:column title="Actions" class="text-center">
						<c:if test="${not empty step.analyzedDataCommandList}">
							<spring:bind path="step.analyzedDataCommandList[${counter}]">
								<a href="${updateAnalyzedDataURL}<c:out value="${status.value.id}"/>&analysis_step_id=${step.id}" 
								   class="btn btn-outline-danger btn-sm" title="Delete">
									<i class="fa fa-trash"></i>
								</a>
							</spring:bind>
						</c:if>
					</display:column>
					
					<display:setProperty name="export.pdf" value="true"/>
					<display:setProperty name="basic.empty.showtable" value="true"/>
					<c:set var="counter" value="${counter+1}"/>
				</display:table>
			</div>
		</div>
	</c:if>
</div>

