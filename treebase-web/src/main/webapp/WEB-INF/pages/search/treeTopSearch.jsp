
<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.tree"/></title>

<div class="container py-4">
  <div class="card shadow mb-4">
    <div class="card-body">
      <h2 class="card-title mb-4 text-center"><fmt:message key="search.tree"/></h2>
      <c:set var="searchType" value="tree" scope="request"/>
      <div class="mb-3">
        <jsp:include page="treeTopology3SearchForm.jsp"/>
        <jsp:include page="treeTopology4aSearchForm.jsp"/>
        <jsp:include page="treeTopology4sSearchForm.jsp"/>
      </div>
      <jsp:include page="searchMessages.jsp"/>
      <c:if test="${not empty resultSet && ! resultSet.isTrivial }">
        <c:set var="items" value="items"/>
        <c:if test="${resultSet.count == 1 }"><c:set var="items" value="item"/></c:if>
        <h4 class="mt-4">Your search has yielded ${resultSet.count } ${items }:</h4>
      </c:if>
      <jsp:include page="searchResultsList.jsp"/>
    </div>
  </div>
</div>
