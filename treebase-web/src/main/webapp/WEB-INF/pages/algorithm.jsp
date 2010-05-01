<%@ include file="/common/taglibs.jsp"%>
<c:set var="counter" value="${analysisStepCounter}"/>
<form 
	method="post" 
	onsubmit="return TreeBASE.analysisEditor.checkValue(this,${counter},'${publicationState}')" 
	id="form${counter}" 
	action="/treebase-web/user/analysisStepForm.html?id=<c:out value="${analysisStepCommand.id}"/>">
<input type="hidden" name="redirect" value="${redirect}"/>
<fieldset class="analysisStep">
	<legend>
		<a onclick="TreeBASE.collapseExpand('analysisStepCommand<c:out value="${analysisStepCommand.id}"/>','block',this)"
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
		Analysis step
		<c:if test="${editable}">
			<a 
				href="#" 
				class="openHelp" 
				onclick="openHelp('analysisStepDetailsViewEdit')">
				<img class="iconButton" alt="help" src="<fmt:message key="icons.help"/>" />
			</a>	
		</c:if>
	</legend>	
	<h2 style="font-size:small">
		<span class="isAnalysisStepValid" title="${analysisStepCommand.id}" style="display:none">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.notanalyzed"/>" 
				title="<fmt:message key="analysis.notvalidated"/>" 
				alt="<fmt:message key="analysis.notvalidated"/>"/>			
		</span>
<!-- VG 2010-03-17 Disabled analysis download as requested in #2972107, to mitigate #2970700 and #2970457   -->		
		<a href="/treebase-web/search/downloadAnAnalysisStep.html?analysisid=${analysisStepCommand.id}&id=${analysisStepCommand.analysis.study.id}">  
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.download.reconstructed"/>" 

				title="<fmt:message key="download.reconstructedfile"/>" 
				alt="<fmt:message key="download.reconstructedfile"/>"

<%-- 
				title="<fmt:message key="download.unavailable"/>" 
				alt="<fmt:message key="download.unavailable"/>"	
--%>
				/>	
		</a>	  	
		<c:if test="${not empty analysisStepCommand.displayName}">
			<c:out value="${analysisStepCommand.displayName}"/>
		</c:if>	
		<c:if test="${empty analysisStepCommand.displayName}">
			<em>Untitled</em>
		</c:if>			
	--</h2>
	<div <c:if test="${!editable}">style="display:none"</c:if> id="analysisStepCommand<c:out value="${analysisStepCommand.id}"/>">
		<c:if test="${editable || ( not empty analysisStepCommand.displayName || not empty analysisStepCommand.notes )}">
		<fieldset>
			<legend>Step details</legend>
				<table width="100%">
					<c:if test="${editable || not empty analysisStepCommand.displayName}">
						<tr class="software">
							<td style="text-align:right">
								<label for="name${counter}" class="software">Name</label>
							</td>
							<td style="width:100%">
								<input 
									readonly="readonly"
									class="software disabled textCell" 
									type="text" 
									name="name" 
									id="name${counter}" 
									value="<c:out value="${analysisStepCommand.displayName}"/>"/>
							</td>
						</tr>	
					</c:if>
					<c:if test="${editable || not empty analysisStepCommand.notes}">
						<tr class="software">
							<td style="text-align:right">
								<label for="notes${counter}" class="software">Notes</label>
							</td>
							<td>
								<input 
									readonly="readonly"
									class="software disabled textCell" 
									type="text" 
									name="notes" 
									id="notes${counter}" 
									value="<c:out value="${analysisStepCommand.notes}"/>"/>
							</td>
						</tr>	
					</c:if>
				</table>
		</fieldset>
		</c:if>
		<c:if test="${editable || not empty analysisStepCommand.softwareInfo.name}">	
			<fieldset>
				<legend>Software used</legend>
				<table width="100%">	
					<c:if test="${editable || not empty analysisStepCommand.softwareInfo.name}">			
						<tr class="software">
							<td style="text-align:right">
								<label for="softwareInfo.name${counter}" class="software">Name</label>
							</td>
							<td style="width:100%">
								<input
									readonly="readonly"
									class="software disabled textCell"
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
								<span class="fieldError"></span>
							</td>
						</tr>	
					</c:if>	
					<c:if test="${editable || not empty analysisStepCommand.softwareInfo.softwareVersion}">			
						<tr class="software">
							<td style="text-align:right">
								<label for="softwareInfo.softwareVersion${counter}" class="software">Version</label>
							</td>
							<td>	
							    <input  
							    	readonly="readonly"
							    	class="software disabled textCell"
							    	type="text" 
							    	id="softwareInfo.softwareVersion${counter}"     	
							    	name="softwareInfo.softwareVersion" 
							    	value="<c:out value="${analysisStepCommand.softwareInfo.softwareVersion}"/>"/>
							    <span class="fieldError"></span>
						    </td>
						</tr>	
					</c:if>		
					<c:if test="${editable || not empty analysisStepCommand.softwareInfo.softwareURL}">				
						<tr class="software">
							<td style="text-align:right">
								<label for="softwareInfo.softwareURL${counter}" class="software">URL</label>
							</td>	
							<td>	
							
								<%-- displayed in edit mode --%>
						        <input 
						        	style="display:none"
						        	class="software disabled textCell"
						        	type="text" 
						        	id="softwareInfo.softwareURL${counter}"
						        	name="softwareInfo.softwareURL" 
						        	value="<c:out value="${analysisStepCommand.softwareInfo.softwareURL}"/>"/> 
						        <span class="fieldError"></span>
						        
						        <%-- displayed in view mode --%>
						        <c:if test="${not empty analysisStepCommand.softwareInfo.softwareURL}">
							        <a
							        	style="white-space:nowrap"
							        	id="softwareInfo.softwareLink${counter}"
							        	href="<c:out value="${analysisStepCommand.softwareInfo.softwareURL}"/>">
							        	<img class="iconButton" alt="link" src="<fmt:message key="icons.weblink"/>" />
							        	<%-- c:out value="${analysisStepCommand.softwareInfo.softwareURL}"/ --%>
							        </a>
						        </c:if>
						        
					        </td>
					    </tr>
				    </c:if>  
				    <c:if test="${editable || not empty analysisStepCommand.softwareInfo.description}">		      
					    <tr class="software">
					    	<td style="text-align:right;vertical-align:top">
								<label for="softwareInfo.description${counter}" class="software">Description</label>
							</td>
							<td>		    
						         <input 
						         	readonly="readonly"			         	
						         	class="software disabled textCell"
						         	type="text" 
						         	id="softwareInfo.description${counter}"
						         	name="softwareInfo.description" 
						         	value="<c:out value="${analysisStepCommand.softwareInfo.description}"/>"/>	            
						         <span class="fieldError"></span>
					         </td>
					    </tr>   
				    </c:if>  
				    <c:if test="${editable || not empty analysisStepCommand.algorithmInfo.description}">		       	
						<tr class="software">
					      	<spring:bind path="analysisStepCommand.algorithmType">
					      		<td style="text-align:right">        	
						      		<label for="algorithmTypeInput${counter}" class="software">Algorithm</label>
						      	</td>  
						      	<td>    
						      		
						      		<%-- this part is displayed in view mode --%>
									<input 
										readonly="readonly"
										class="software disabled textCell"
										type="text" 
										id="algorithmTypeInput${counter}"
										value="<c:out value="${analysisStepCommand.algorithmInfo.description}"/>"/>
									<span class="fieldError"></span>
									
									<%-- this part is displayed in edit mode --%>     	     	
							      	<div id="algorithmSelectWidget${counter}" style="display:none">      	 	
								      	<select style="display:inline" name="${status.expression}" class="software" onchange="TreeBASE.analysisEditor.checkOther(this,${counter})">
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
											<div id="ac${counter}" <c:if test="${analysisStepCommand.algorithmInfo.description != 'other algorithm'}">style="display:none"</c:if>>        				
												<input 
													style="width:100%"
													class="textCell"
													type="text" 
													id="algorithmType${counter}" 
													name="<c:out value="${status.expression}"/>" 
													value="<c:out value="${analysisStepCommand.algorithmInfo.description}"/>"/>
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
								</td>
					      	</spring:bind>
						</tr>
					</c:if>
				    <c:if test="${editable || not empty analysisStepCommand.commands}">			
					    <tr class="software">
					    	<td colspan="2" style="text-align:center">
								<label for="commands${counter}" class="software">Commands</label>
							</td>
						</tr>
						<tr class="software">
							<td colspan="2" style="text-align:center">		        
								<textarea 
									readonly="readonly"
									class="software disabled textCell"
									style="width:100%;height:100px"
									id="commands${counter}"
									name="commands"><c:out value="${analysisStepCommand.commands}"/></textarea>
						        <span class="fieldError"></span>
					         </td>
						</tr>	
					</c:if>		
				</table>
			</fieldset>
		</c:if>
		<c:if test="${editable}">	
			<table width="100%">
				<tr>
					<td style="margin-top:10px;text-align:right" colspan="2">
						<input style="display:none" type="submit" name="Update" value="<fmt:message key="button.update"/>" />
						<input style="display:none" type="submit" name="Delete" value="<fmt:message key="button.delete"/>" />				
						<a href="#" onclick="return TreeBASE.analysisEditor.enableEdit(${counter})" title="Edit analysis step details">
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
		</c:if>	
	</div>
</fieldset>
</form>
