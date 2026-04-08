
<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.matrix"/></title>

<div class="container-fluid">
            <script type="text/javascript">
                //<![CDATA[
                var predicates = {
                    'integer' : [ 'tb.identifier.matrix', 'tb.identifier.matrix.tb1', 'tb.ntax.matrix', 'tb.nchar.matrix' ],
                    'id' : [ 'tb.identifier.matrix', 'tb.identifier.matrix.tb1' ],
                    'word' : [ 'tb.title.matrix', 'tb.type.matrix' ],
                    'doi' : [ 'prism.doi' ] // this doesn't work yet, we have no search on doi
                };
                var phyloWSURI = purlBase + 'matrix/find?query=';
                //]]>
            </script>
            <div class="card">
              <div class="card-header d-flex justify-content-between align-items-center">
                    <span class="fw-semibold"><fmt:message key="search.matrix"/></span>

                    <tb:helpButton topic="matrixSimpleSearchForm"/>
                </div>
                <div class="card-body">
                    <jsp:include page="matrixSimpleSearchForm.jsp"/>
                </div>
            </div>
            <c:set var="searchType" value="matrix" scope="request"/>
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
    </div>
</div>
