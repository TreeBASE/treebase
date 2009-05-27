<%@ include file="/common/taglibs.jsp"%>
<head>
	<title>Analyses</title>
	<content tag="heading">Analyses</content>
</head>
<body id="submissions"/>
	<p>
		TreeBASE will only publish matrices and trees that are listed with analysis entries. 
		At a minimum, each submission must have at least one analysis entry containing at 
		least one analysis step.
	</p> 
	
	<!-- imports & variables necessary before running analysis jsps -->
	<c:set var="editable" value="${publicationState eq 'NotReady'}" scope="request"/>
	<!-- also need studyCommand from controller -->
	
	<!-- now run analysis jsps -->
	<jsp:include page="analysisList.jsp"/>

</body>
 		