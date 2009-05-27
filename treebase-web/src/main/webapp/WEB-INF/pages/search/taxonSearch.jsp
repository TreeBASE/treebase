<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.taxon"/></title>
<body id="s-taxon"/>

<div id="wrap">
		<jsp:include page="/common/search-nav.jsp"/>

<div id="s-clear"></div>

<c:set var="searchType" value="taxon" scope="request"/>

<jsp:include page="taxonSearchForm.jsp"/>


<div id="s-clear"></div>

<jsp:include page="searchMessages.jsp"/>

<c:if test="${not empty taxonSearchResults && ! taxonSearchResults.isTrivial}">
  <c:set var="items" value="items"/>
  <c:if test="${taxonSearchResults.count == 1 }"><c:set var="items" value="item"/></c:if>
  <h3>Your search has yielded ${taxonSearchResults.count } ${items }:</h3>
</c:if>

<jsp:include page="searchResultsList.jsp"/> 

</div>