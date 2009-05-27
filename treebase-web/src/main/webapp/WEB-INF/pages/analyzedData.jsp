<%@ include file="/common/taglibs.jsp"%>
<fieldset class="analysisStep">
	<legend>
		<a onclick="TreeBASE.collapseExpand('analyzedData${inputOutput}<c:out value="${analysisStepCommand.id}"/>','block',this)"
			style="border:none"
			title="collapse">
			<c:if test="${editable}">
				<img 
					class="iconButton" 
					src="<fmt:message key="icons.collapse"/>"
					style="vertical-align:middle" 
					alt="collapse" />
			</c:if>
			<c:if test="${!editable}">
				<img 
					class="iconButton" 
					src="<fmt:message key="icons.expand"/>"
					style="vertical-align:middle" 
					alt="collapse" />
			</c:if>	
		</a>	
		${inputOutput} data
		<c:if test="${editable}">
			<a 
				href="#" 
				class="openHelp" 
				onclick="openHelp('analysisStep${inputOutput}DataDetailsViewEdit')">
				<img class="iconButton" alt="help" src="<fmt:message key="icons.help"/>" />
			</a>
		</c:if>
	</legend>
	<c:set var="dataCount" value="0"/>
	<c:forEach var="analyzedData" items="${analysisStepCommand.analyzedDataCommandList}">
		<c:if test="${analyzedData.inputOutputType == inputOutput}"><c:set var="dataCount" value="${dataCount + 1 }"/></c:if>
	</c:forEach>
	<h2 style="font-size:small">${dataCount} ${inputOutput} object(s)</h2>	
	<div
		<c:if test="${!editable}">style="display:none"</c:if> 
		id="analyzedData${inputOutput}<c:out value="${analysisStepCommand.id}"/>">
	<c:forEach var="analyzedData" items="${analysisStepCommand.analyzedDataCommandList}">
		<c:if test="${analyzedData.inputOutputType == inputOutput}">
			<table width="100%">
				<tr>
					<c:choose>
						<c:when test="${analyzedData.dataType == 'tree'}">
							<td>
								<img 
									src="<fmt:message key="icons.tree"/>" 
									width="16" 
									height="16" 
									alt="Tree" 
									style="vertical-align:middle"/>
							</td>
							<c:if test="${editable}">
								<td>
									<form 
										method="post" 
										action="/treebase-web/user/addAnalyzedData.html" 
										style="display:inline;vertical-align:middle"
										title="Remove ${inputOutput} Tree">
										<input type="hidden" name="deleteMe" value="<c:out value="${analyzedData.id}"/>"/>
										<input type="hidden" name="action" value="remove"/>
										<input type="hidden" name="dataType" value="Trees"/>
										<input type="hidden" name="inputOutput" value="${inputOutput}"/>
										<input type="hidden" name="analysisStepId" value="<c:out value="${analysisStepCommand.id}"/>"/>
										<input style="display:inline;vertical-align:middle" type="image" src="<fmt:message key="icons.delete"/>"/>
									</form>
								</td>
							</c:if>
						</c:when>
						<c:when test="${analyzedData.dataType == 'matrix'}">
							<td>
								<img 
									src="<fmt:message key="icons.matrix"/>" 
									width="16" 
									height="16" 
									alt="Matrix" 
									style="vertical-align:middle"/>
							</td>
							<c:if test="${editable}">
								<td>
									<form 
										method="post" 
										action="/treebase-web/user/addAnalyzedData.html" 
										style="display:inline;vertical-align:middle"
										title="Remove ${inputOutput} Matrix">
										<input type="hidden" name="deleteMe" value="<c:out value="${analyzedData.id}"/>"/>
										<input type="hidden" name="action" value="remove"/>
										<input type="hidden" name="dataType" value="Matrices"/>
										<input type="hidden" name="inputOutput" value="${inputOutput}"/>
										<input type="hidden" name="analysisStepId" value="<c:out value="${analysisStepCommand.id}"/>"/>
										<input style="display:inline;vertical-align:middle" type="image" src="<fmt:message key="icons.delete"/>"/>
									</form>	
								</td>					
							</c:if>
						</c:when>	
						<c:when test="${analyzedData.dataType == 'treeBlock'}">
							<td>
								<img 
									src="<fmt:message key="icons.treeBlock"/>" 
									width="16" 
									height="16" 
									alt="Tree Block" 
									style="vertical-align:middle"/>
							</td>
							<c:if test="${editable}">
								<td>
								</td>					
							</c:if>							
						</c:when>	
					</c:choose>
					<td width="100%"><c:out value="${analyzedData.displayName}"/></td>
				</tr>
			</table>
		</c:if>
	</c:forEach>
	<c:if test="${editable}">
		<div style="margin-top:10px;text-align:right">
			<a href="#" onclick="return TreeBASE.analysisEditor.addData(this)" title="Add ${inputOutput} Data">
				<img 
					src="<fmt:message 
					key="icons.add"/>" 
					class="iconButton" 
					width="16" 
					height="16" 
					alt="Add" 
					style="vertical-align:middle"/>
			</a>
			<select style="display:none;width:100%" onchange="TreeBASE.analysisEditor.selectData(this)">
				<option>---select data type---</option>
				<option value="Matrices">Matrices</option>
				<option value="TreeBlocks">Tree blocks</option>
				<option value="Trees">Trees</option>
			</select>
			<select style="display:none;width:100%" multiple="multiple" size="10">
				<option>---select data---</option>
			</select>	
			<input type="button" style="display:none;width:100%" value="Add selected" onclick="TreeBASE.analysisEditor.addSelected(this,'${inputOutput}',<c:out value="${analysisStepCommand.id}"/>)"/>					
			<div></div>
		</div>
	</c:if>		
	</div>									
</fieldset>