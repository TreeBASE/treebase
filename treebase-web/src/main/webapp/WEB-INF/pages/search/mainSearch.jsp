<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.main"/></title>
<body id="s-main"/>

<div id="wrap">
	<!-- these are the tabs that switch between search forms -->
	<jsp:include page="/common/search-nav.jsp"/>
	<div id="s-clear"></div>
	
	<c:set var="searchType" value="main" scope="request"/>

	<!-- this is the simple search box -->
	<jsp:include page="mainSimpleSearchForm.jsp"/>
	<div id="s-clear"></div>

	<!-- this contains any search warnings, e.g. if an empty query string was provided -->
	<jsp:include page="searchMessages.jsp"/>

	<div id="searchResultsList">
		<c:set var="resultSet" value="${studyResults}"/>
		<jsp:include page="studyList.jsp"/>
		 
		<c:set var="resultSet" value="${matrixResults}"/>
		<jsp:include page="matrixList.jsp"/>

		<c:set var="resultSet" value="${treeResults}"/>
		<jsp:include page="treeList.jsp"/> 
		
		<c:set var="resultSet" value="${taxonResults}"/>
		<jsp:include page="taxonList.jsp"/>
	</div>

	<div id="output"></div>

</div>