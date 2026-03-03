
<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.tree"/></title>

<div class="container-fluid">
  <script type="text/javascript">
    //<![CDATA[
    var predicates = {
      'integer' : [ 'tb.identifier.tree', 'tb.ntax.tree' ],
      'id' : [ 'tb.identifier.tree' ],
      'word' : [ 'tb.title.tree', 'tb.type.tree', 'tb.kind.tree', 'tb.quality.tree' ],
    };
    var phyloWSURI = purlBase + 'tree/find?query=';
    //]]>
  </script>
  <div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
      <span class="fw-semibold"><fmt:message key="search.tree"/></span>
      <a href="#" class="openHelp" onclick="openHelp('treeSimpleSearchForm')">
        <i class="fa fa-question-circle fa-icon"></i> Help
      </a>
    </div>
    <div class="card-body">
      <jsp:include page="treeSimpleSearchForm.jsp"/>
    </div>
  </div>
  <c:set var="searchType" value="tree" scope="request"/>
  <jsp:include page="searchMessages.jsp"/>
  <c:if test="${not empty resultSet && ! resultSet.isTrivial }">
    <c:set var="items" value="items"/>
    <c:if test="${resultSet.count == 1 }"><c:set var="items" value="item"/></c:if>
      <div class="alert alert-info mt-4">
        <strong>Your search has yielded ${resultSet.count } ${items }.</strong>
      </div>
    </c:if>
    <jsp:include page="searchResultsList.jsp"/>
  </div>
