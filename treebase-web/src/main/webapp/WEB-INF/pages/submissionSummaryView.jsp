<%@page import="org.cipres.treebase.TreebaseUtil"%>
<% String purlBase = TreebaseUtil.getPurlBase(); %>
<%@ include file="/common/taglibs.jsp"%>

<title>Summary Information</title>

<div class="container py-5">
	<div class="card shadow-lg mb-4">
		<div class="card-header d-flex justify-content-between align-items-center">
			<span class="fw-semibold">Summary</span>
			<a href="#" class="openHelp" onclick="openHelp('submissionSummaryView')">
				<i class="fa fa-question-circle"></i> Help
			</a>
		</div>
		<div class="card-body">
			<div class="row mb-3">
				<div class="col-md-6">
					<p class="mb-2">
						<strong>Submission:</strong> <c:out value="${submissionNumber}"/>
						<span class="badge bg-secondary ms-2"><c:out value="${studyStatus}"/></span>
						<%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
						<a href="/treebase-web/admin/changeStudyStatus.html" class="btn btn-sm btn-outline-primary ms-2">
							<i class="fa fa-pencil"></i> Update Status
						</a>
						<%}%>
					</p>
					<p class="text-muted mb-0">
						<i class="fa fa-calendar"></i> Submission initiated: <c:out value="${initiatedDate}"/>
					</p>
				</div>
			</div>

			<hr/>

			<div class="row g-3 mb-4">
				<div class="col-md-6">
					<a href='mailto:${submission.submitter.emailAddressString}?subject=TreeBASE Submission S${submission.id}' class="btn btn-outline-primary">
						<i class="fa fa-envelope"></i> Contact Submitter
					</a>
				</div>
				<div class="col-md-6">
					<a href='https://github.com/TreeBASE/treebase/issues/new?title=TreeBASE Submission S${submission.id}' class="btn btn-outline-secondary">
						<i class="fa fa-external-link"></i> Contact TreeBASE Help
					</a>
				</div>
			</div>

			<div class="card bg-light mb-3">
				<div class="card-body">
					<h6 class="card-title"><i class="fa fa-link"></i> Study Accession</h6>
					<p class="mb-2">
						<a href="/treebase-web/phylows/study/TB2:S${submission.study.id}" class="text-decoration-none">
							<code>TB2:S${submission.study.id}</code>
						</a>
					</p>
					<p class="text-muted small mb-0">
						<i class="fa fa-info-circle"></i> You can cite this accession in your manuscript. It will become the permanent and resolvable resource locator after your submission has been approved and the data are made public.
					</p>
				</div>
			</div>

			<div class="card bg-light mb-3">
				<div class="card-body">
					<h6 class="card-title"><i class="fa fa-eye"></i> Reviewer Access</h6>
					<p class="mb-2">
						<a href="/treebase-web/phylows/study/TB2:S${submission.study.id}?x-access-code=<c:out value="${submission.study.namespacedGUID.hashedIDString}"/>&format=html" class="text-decoration-none">
							<code>TB2:S${submission.study.id}?x-access-code=<c:out value="${submission.study.namespacedGUID.hashedIDString}"/></code>
						</a>
					</p>
					<p class="text-muted small mb-0">
						<i class="fa fa-info-circle"></i> You can copy and send this accession to your journal editor to provide reviewers with limited, read-only access to your data, even if your submission has not yet been approved and the data are not yet public.
					</p>
				</div>
			</div>

			<c:if test="${not empty citationsummary.study}">
				<c:if test="${not empty citationsummary.study.name}">
					<p class="mb-3"><strong>Study name:</strong> <c:out value="${citationsummary.study.name}"/></p>
				</c:if>
			</c:if>

			<c:if test="${empty citationsummary.title}">
				<div class="alert alert-warning d-flex align-items-center" role="alert">
					<i class="fa fa-exclamation-triangle me-2"></i>
					<div>Citation information not yet entered. Click the <strong>Citation</strong> menu item to add it.</div>
				</div>
			</c:if>

			<c:if test="${not empty citationsummary.title}">
				<div class="card mb-3">
					<div class="card-header">
						<i class="fa fa-book"></i> Citation
					</div>
					<div class="card-body">
						<p class="mb-2">
							<span class="badge bg-info"><c:out value="${citationsummary.citationType}"/></span>
							<span class="badge bg-secondary"><c:out value="${citationsummary.citationStatusDescription}"/></span>
						</p>
						<p class="mb-0">${citationsummary.authorsCitationStyle}</p>
					</div>
				</div>
			</c:if>

			<c:if test="${not empty citationsummary.abstract}">
				<div class="card mb-3">
					<div class="card-header">
						<i class="fa fa-file-text-o"></i> Abstract
					</div>
					<div class="card-body">
						<p class="mb-0"><c:out value="${citationsummary.abstract}"/></p>
					</div>
				</div>
			</c:if>
		</div>
	</div>

	<!-- imports & variables necessary before running analysis jsps -->
	<c:set var="editable" value="${false}" scope="request"/>
	<!-- also need studyCommand from controller -->
	
	<!-- now run analysis jsps -->
	<jsp:include page="analysisList.jsp"/>
</div>
 		