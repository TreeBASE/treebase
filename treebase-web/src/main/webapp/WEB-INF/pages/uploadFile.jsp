<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="upload.file.title"/></title>
<script type="text/javascript" src="../scripts/xp_progress.js"></script>

<script type="text/javascript">
function show_or_hide(layer_ref, state) { 
  if (document.all) { //IS IE 4 or 5 (or 6 beta) 
    eval( "document.all." + layer_ref + ".style.display = state"); 
  } 
  else if (document.layers) { //IS NETSCAPE 4 or below 
    document.layers[layer_ref].display = state; 
  } 
  else if (document.getElementById && !document.all) { 
    hza = document.getElementById(layer_ref); 
    hza.style.display = state; 
  } 
} 
</script>

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle me-3 mt-1"></i>
		<div>
			<p class="mb-2">Use this form to upload your Nexus files for <strong>submission ${studyMap['id']} - ${studyMap['name']}</strong></p>
			<p class="mb-0 small">Please note that only the first ~30 trees will be parsed, otherwise large numbers of trees resulting from the 
			same analysis will overwhelm the user experience in TreeBASE's search interface with what are largely redundant trees. 
			If you have a large number of trees, please put your preferred trees, or a consensus tree, within the first ~30 
			trees in the tree block. For more information, please see the 
			<a href="#" class="openHelp" onclick="openHelp('uploadFile')"><i class="fa fa-question-circle"></i> help</a>.</p>
		</div>
	</div>

	<form method="post" enctype="multipart/form-data">
		<div class="card shadow-lg mb-4">
			<div class="card-header d-flex justify-content-between align-items-center">
				<span class="fw-semibold"><i class="fa fa-upload"></i> Nexus Files Upload</span>
				<a href="#" class="openHelp" onclick="openHelp('uploadFile')">
					<i class="fa fa-question-circle"></i> Help
				</a>
			</div>
			<div class="card-body">
				<c:if test="${publicationState eq 'Ready' || publicationState eq 'Published'}">
					<div class="alert alert-warning d-flex align-items-center" role="alert">
						<i class="fa fa-exclamation-triangle me-2"></i>
						<div>For this study, file upload feature is not available.</div>
					</div>
				</c:if>
				
				<c:if test="${publicationState eq 'NotReady'}">
					<div class="mb-3">
						<label class="form-label fw-semibold">Select Nexus File</label>
						<input type="file" name="data" class="form-control"/>
					</div>
					
					<div id="attachments" class="mb-3"></div>
					
					<div class="mb-4">
						<a href="javascript:addFile()" class="btn btn-outline-secondary btn-sm">
							<i class="fa fa-plus"></i> Attach another file
						</a>
					</div>
					
					<div class="d-flex gap-2">
						<button type="submit" name="upload" class="btn btn-primary" onclick="show_or_hide('kids', 'block');">
							<i class="fa fa-upload"></i> <fmt:message key="button.upload"/>
						</button>
						<button type="submit" name="_cancel" class="btn btn-outline-secondary" onclick="bCancel=true">
							<fmt:message key="button.cancel"/>
						</button>
					</div>
				</c:if>
				
				<div id="progressBar" style="display: none;">
					<div id="progressBarBox">
						<div id="progressBarBoxContent"></div>
						<div id="progressBarText">
							<spring:message code="progressBarTransferLabel"/>
							<span id="percentage"></span>
							<spring:message code="progressBarPercentLabel"/>
							<spring:message code="progressBarTransferSizeLabel"/>;
						</div>
						<div id="progressBarSuccessful">
							<spring:message code="progressBarTextSuccessLabel"/>
						</div>
					</div>
				</div>
				
				<div id="kids" style="display: none;" class="mt-4">
					<div class="d-flex align-items-center">

						<h5 class="mb-0">Uploading...</h5>
					</div>
					<script type="text/javascript">
						var bar1 = createBar(500,15,'white',1,'black','#7DCBDA',85,7,3,"");
					</script>
				</div>
			</div>
		</div>
	</form>
</div>

<%@ include file="/scripts/multiFileUpload.js"%>
