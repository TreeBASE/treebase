<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.study"/></title>
<body id="s-study"  onload="TreeBASE.initialize()">

<div id="wrap">
		<jsp:include page="/common/search-nav.jsp"/>



<div id="s-clear"></div>

<script type="text/javascript">
  //<![CDATA[
    // These are the mappings from inferred search term types to search predicates for this scope
    var predicates = {
      'integer' : [ 'tb.identifier.study', 'tb.identifier.study.tb1' ],
      'id' : [ 'tb.identifier.study', 'tb.identifier.study.tb1' ],
      'word' : [ 'tb.title.study', 'dcterms.contributor', 'dcterms.abstract', 'dcterms.bibliographicCitation', 'dcterms.subject' ],
      'doi' : [ 'prism.doi' ]
    };
    
 	// purlBase is assigned in /common/search-nav.jsp
    var phyloWSURI = purlBase + 'study/find?query=';
  //]]>
</script>

<c:set var="searchType" value="study" scope="request"/>

<jsp:include page="studyKeywordSearchForm.jsp"/>

<div id="s-clear"></div>

<jsp:include page="searchMessages.jsp"/>

<c:if test="${not empty resultSet && ! resultSet.isTrivial }">
  <c:set var="items" value="items"/>
  <c:if test="${resultSet.count == 1 }"><c:set var="items" value="item"/></c:if>
  <h3>Your search has yielded ${resultSet.count } ${items }:</h3>
</c:if>

<jsp:include page="searchResultsList.jsp"/> 

</div>
</body>
