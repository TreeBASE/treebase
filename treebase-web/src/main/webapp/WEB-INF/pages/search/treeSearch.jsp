
<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.tree"/></title>

<div class="container-fluid">
      <h2><fmt:message key="search.tree"/></h2>
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
      <div class="mb-3">
        <jsp:include page="treeSimpleSearchForm.jsp"/>
      </div>
      <c:set var="searchType" value="tree" scope="request"/>
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
