
<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.study"/></title>

<div class="container-fluid">
      <h2><fmt:message key="search.study"/></h2>
      <script type="text/javascript">
        //<![CDATA[
        var predicates = {
          'integer' : [ 'tb.identifier.study', 'tb.identifier.study.tb1' ],
          'id' : [ 'tb.identifier.study', 'tb.identifier.study.tb1' ],
          'word' : [ 'tb.title.study', 'dcterms.contributor', 'dcterms.abstract', 'dcterms.bibliographicCitation', 'dcterms.subject' ],
          'doi' : [ 'prism.doi' ]
        };
        var phyloWSURI = purlBase + 'study/find?query=';
        //]]>
      </script>
      <c:set var="searchType" value="study" scope="request"/>
      <div class="mb-3">
        <jsp:include page="studyKeywordSearchForm.jsp"/>
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
