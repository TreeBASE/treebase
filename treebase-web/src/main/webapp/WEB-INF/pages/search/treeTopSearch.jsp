
<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.tree"/></title>

<div class="container-fluid">
      <c:set var="searchType" value="tree" scope="request"/>
        <div class="row">
            <div class="col-md-4">
                <jsp:include page="treeTopology3SearchForm.jsp"/>
            </div>
            <div class="col-md-4">
                <jsp:include page="treeTopology4aSearchForm.jsp"/>
            </div>
            <div class="col-md-4">
                <jsp:include page="treeTopology4sSearchForm.jsp"/>
            </div>
        </div>
      <jsp:include page="searchMessages.jsp"/>
      <c:if test="${not empty resultSet && ! resultSet.isTrivial }">
        <c:set var="items" value="items"/>
        <c:if test="${resultSet.count == 1 }"><c:set var="items" value="item"/></c:if>
          <h4 class="mt-4">Your search has yielded ${resultSet.count } ${items }:</h4>
        </c:if>
        <jsp:include page="searchResultsList.jsp"/>
  </div>
