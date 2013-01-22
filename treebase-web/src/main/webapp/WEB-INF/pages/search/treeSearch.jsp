
<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.tree"/></title>
<body id="s-tree"/>

<div id="wrap">
		<jsp:include page="/common/search-nav.jsp"/>


<div id="s-clear"></div>

<script type="text/javascript">
  //<![CDATA[
    // These are the mappings from inferred search term types to search predicates for this scope
    var predicates = {
      'integer' : [ 'tb.identifier.tree', 'tb.ntax.tree' ],
      'id' : [ 'tb.identifier.tree' ],
      'word' : [ 'tb.title.tree', 'tb.type.tree', 'tb.kind.tree', 'tb.quality.tree' ],
    };
    
 	// purlBase is assigned in /common/search-nav.jsp
    var phyloWSURI = purlBase + 'tree/find?query=';
  //]]>
</script>

<jsp:include page="treeSimpleSearchForm.jsp"/>

<c:set var="searchType" value="tree" scope="request"/>

<div id="s-clear"></div>

<jsp:include page="searchMessages.jsp"/>

<c:if test="${not empty resultSet && ! resultSet.isTrivial }">
  <c:set var="items" value="items"/>
  <c:if test="${resultSet.count == 1 }"><c:set var="items" value="item"/></c:if>
  <h3>Your search has yielded ${resultSet.count } ${items }:</h3>
</c:if>

<jsp:include page="searchResultsList.jsp"/> 

</div>