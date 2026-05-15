<%@ include file="/common/taglibs.jsp"%>
<head>
	<title>Analyses</title>
</head>
<body id="submissions"/>

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle fa-lg me-3 mt-1"></i>
		<div>
			<strong>Important:</strong> TreeBASE will only publish matrices and trees that are listed with analysis entries. 
			At a minimum, each submission must have at least one analysis entry containing at 
			least one analysis step.
		</div>
	</div>
	
	<!-- imports & variables necessary before running analysis jsps -->
	<c:set var="editable" value="${publicationState eq 'NotReady'}" scope="request"/>
	<!-- also need studyCommand from controller -->
	
	<!-- now run analysis jsps -->
	<jsp:include page="analysisList.jsp"/>
</div>

</body>
 		