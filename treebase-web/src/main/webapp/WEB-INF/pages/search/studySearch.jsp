
<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.study"/></title>

<div class="container-fluid">
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
    <div class="card">
        <div class="card-header d-flex justify-content-between align-items-center">
            <span class="fw-semibold"><fmt:message key="search.study"/></span>
            <tb:helpButton topic="studyKeywordSearchForm"/>
        </div>
        <div class="card-body">
            <jsp:include page="studyKeywordSearchForm.jsp"/>
        </div>
    </div>
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
