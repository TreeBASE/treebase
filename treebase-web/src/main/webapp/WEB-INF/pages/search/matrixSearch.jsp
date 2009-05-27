<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.matrix"/></title>
<body id="s-matrix"/>

<div id="wrap">
		<jsp:include page="/common/search-nav.jsp"/>

<div id="s-clear"></div>


<jsp:include page="matrixSimpleSearchForm.jsp"/>

<c:set var="searchType" value="matrix" scope="request"/>

<%-- <jsp:include page="studyTaxonLabelSearchForm.jsp"/> --%>

<div id="s-clear"></div>

<jsp:include page="searchMessages.jsp"/> 

<c:if test="${not empty resultSet && ! resultSet.isTrivial }">
  <c:set var="items" value="items"/>
  <c:if test="${resultSet.count == 1 }"><c:set var="items" value="item"/></c:if>
  <h3>Your search has yielded ${resultSet.count } ${items }:</h3>
</c:if>

<jsp:include page="searchResultsList.jsp"/> 

</div>