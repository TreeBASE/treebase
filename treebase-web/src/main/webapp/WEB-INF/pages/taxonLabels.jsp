<%@ include file="/common/taglibs.jsp"%>

<title>Taxon Label Information</title>

<c:set var="ncbiTaxonomyURL" value="http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id=" />
<c:set var="uBioTaxonomyURL" value="http://www.ubio.org/browser/details.php?namebankID=" />

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle me-3 mt-1"></i>
		<div>
			<p class="mb-0">Taxon labels must be in compliance with TreeBASE guidelines, and wherever possible they should be validated against 
			names in <a href="http://www.ubio.org" target="_blank">uBIO</a> and <a href="http://www.ncbi.nlm.nih.gov/Taxonomy/" target="_blank">NCBI</a>.
			For more information, please see the 
			<a href="#" class="openHelp" onclick="openHelp('taxonLabels')"><i class="fa fa-question-circle"></i> help</a>.</p>
		</div>
	</div>

	<form method="post">
		<div class="card shadow-lg mb-4">
			<div class="card-header d-flex justify-content-between align-items-center">
				<span class="fw-semibold"><i class="fa fa-leaf"></i> Taxon Labels</span>
				<a href="#" class="openHelp" onclick="openHelp('taxonLabels')">
					<i class="fa fa-question-circle"></i> Help
				</a>
			</div>
			<div class="card-body">
				<display:table name="${txnlabelset}"
							   requestURI=""			 
							   class="table table-striped table-hover"
							   id="userList"
							   export="true">
				   
					<%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
						<% request.setAttribute("isEditable","yes");%>
					<% } %>	  
					
					<c:if test="${editable||isEditable eq 'yes'}">
						<display:column class="text-center" style="width: 40px;" title="">
							<div class="form-check d-flex justify-content-center">
								<input type="checkbox" class="form-check-input" name="validate" value="${userList.id}" 
									title="Include/exclude from validation"
									<c:if test="${!userList.attemptedLinking}"> checked="checked"</c:if>
								/>
							</div>
						</display:column>
					</c:if>
					
					<display:column property="taxonLabel" sortable="true" titleKey="taxonlabel.title" />
								
					<display:column sortable="true" property="taxonVariant.fullName" titleKey="taxon.name" />
								
					<display:column titleKey="taxon.ncbiTaxID" sortable="true">
						<c:if test="${userList.taxonVariant!=null}">
							<c:set var="ncbiTaxoURL" value="${ncbiTaxonomyURL}${userList.ncbiTaxID}"/>
							<a href="${ncbiTaxoURL}" target="_blank">${userList.ncbiTaxID} <i class="fa fa-external-link-alt fa-xs"></i></a>
						</c:if>
					</display:column>
					
					<display:column titleKey="taxon.uBioTaxID" sortable="true">
						<c:if test="${userList.taxonVariant!=null}">
							<c:set var="ubioTaxoURL" value="${uBioTaxonomyURL}${userList.taxonVariant.namebankID}"/>
							<a href="${ubioTaxoURL}" target="_blank">${userList.taxonVariant.namebankID} <i class="fa fa-external-link-alt fa-xs"></i></a>
						</c:if>
					</display:column>	
					
					<display:column class="text-center" style="width: 50px;" title="Status">
						<c:if test="${userList.attemptedLinking}">
							<span class="badge bg-success" title="<fmt:message key="taxonlabel.attemptedLinking"/>">
								<i class="fa fa-check"></i>
							</span>
						</c:if>
						<c:if test="${!userList.attemptedLinking}">
							<span class="badge bg-warning text-dark" title="<fmt:message key="taxonlabel.attemptedLinking"/>">
								<i class="fa fa-clock"></i>
							</span>
						</c:if>	
					</display:column>
								
					<c:if test="${editable||isEditable eq 'yes'}">	
						<display:column class="text-center" style="width: 50px;" title="Actions">
							<a href="<c:url value='/user/editTaxonLabel.html'/>?taxonlabelid=${userList.id}" 
							   class="btn btn-sm btn-outline-primary" 
							   title="<fmt:message key="taxonlabel.change.title"/>">
								<i class="fa fa-edit"></i>
							</a>
						</display:column>
					</c:if>

					<c:if test="${editable||isEditable eq 'yes'}">
						<display:footer>
							<tr>
								<td colspan="7" class="text-center py-3">
									<div class="d-flex gap-2 justify-content-center">
										<button type="submit" class="btn btn-primary" name="Update">
											<i class="fa fa-sync-alt"></i> Update Multiple Taxon Labels
										</button>
										<button type="submit" class="btn btn-success" name="Validate">
											<i class="fa fa-check-double"></i> Validate Taxon Labels
										</button>
									</div>
								</td>
							</tr>
						</display:footer>
					</c:if>
					
					<display:setProperty name="export.pdf" value="true" />
					<display:setProperty name="basic.empty.showtable" value="true"/>
					
				</display:table>
			</div>
		</div>
	</form>
</div>
