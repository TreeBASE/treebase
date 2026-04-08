<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="nav.nexusfiles"/></title>
<c:url var="nexusdownloadURL" value="/user/downloadANexusFile.html" />
<c:url var="nexusrctdownloadURL" value="/user/downloadANexusRCTFile.html" />

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle me-3 mt-1"></i>
		<div>
			<p class="mb-0 small">The table below shows a list of all the NEXUS files uploaded to the submission. 
			These files cannot be deleted unless the entire submission is deleted, however 
			they are not visible to the public unless trees or matrices brought in by them 
			are published. Consequently, if all trees and matrices are deleted, the original 
			file is only visible to the submitter and to the editors of TreeBASE. Retaining 
			these files is useful for TreeBASE staff as a record of prior submission activity.</p>
		</div>
	</div>

	<form method="post">
		<div class="card shadow-lg mb-4">
			<div class="card-header d-flex justify-content-between align-items-center">
				<span class="fw-semibold"><i class="fa fa-file-code-o"></i> Nexus Files</span>

				<tb:helpButton topic="nexusFiles"/>
			</div>
			<div class="card-body">
				<c:set var="counter" value="0"/>
				<div class="table-responsive">
					<display:table name="${nexusFileList}"
								   requestURI=""
								   class="table table-striped table-hover"
								   id="userList"
								   cellspacing="0"
								   cellpadding="0">	

						<display:column title="File name" sortable="true">${userList}</display:column>		

						<display:column 
							title="Reconstructed"
							class="text-center"
							headerClass="text-center"
							style="width: 120px;"
							sortable="false">
							<a href="<c:out value="${nexusrctdownloadURL}"/>?nexusfile=${userList}" 
							   class="btn btn-sm btn-outline-primary" 
							   title="<fmt:message key="download.reconstructedfile"/>">
								<i class="fa fa-download"></i> Reconstructed
							</a>
						</display:column>
							
						<display:column
							title="Original"
							class="text-center"
							headerClass="text-center"
							style="width: 100px;"
							sortable="false">
							<a href="<c:out value="${nexusdownloadURL}"/>?nexusfile=${userList}" 
							   class="btn btn-sm btn-outline-secondary" 
							   title="<fmt:message key="download.original"/>">
								<i class="fa fa-file-o"></i> Original
							</a>
						</display:column>		
						
						<display:setProperty name="export.pdf" value="true" />
						<display:setProperty name="basic.empty.showtable" value="true"/>
						
					</display:table>
				</div>
			</div>
		</div>
	</form>
</div>