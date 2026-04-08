<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.taxon"/></title>

<div class="container-fluid">
            <script type="text/javascript">
                //<![CDATA[
                // These are the mappings from inferred search term types to search predicates for this scope
                var predicates = {
                    'integer' : [ 'tb.identifier.ncbi', 'tb.identifier.ubio', 'tb.identifier.taxon', 'tb.identifier.taxon.tb1' ],
                    'id' : [ 'tb.identifier.taxon', 'tb.identifier.taxon.tb1' ],
                    'word' : [ 'tb.title.taxon', 'tb.title.taxonLabel', 'tb.title.taxonVariant' ],
                    'doi' : [ 'prism.doi' ] // this doesn't work yet, we have no search on doi
                };
                
                // purlBase is assigned in /common/search-nav.jsp
                var phyloWSURI = purlBase + 'taxon/find?query=';
                //]]>
            </script>

            <c:set var="searchType" value="taxon" scope="request"/>

            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <span class="fw-semibold"><fmt:message key="search.taxon"/></span>

                    <tb:helpButton topic="taxonSearchTextSearch"/>
                </div>
                <div class="card-body">
                    <jsp:include page="simpleSearchForm.jsp"/>
                </div>
            </div>

            <jsp:include page="searchMessages.jsp"/>

            <c:if test="${not empty taxonSearchResults && ! taxonSearchResults.isTrivial}">
                <c:set var="items" value="items"/>
                <c:if test="${taxonSearchResults.count == 1 }"><c:set var="items" value="item"/></c:if>
                <div class="alert alert-info mt-4">
                    <strong>Your search has yielded ${taxonSearchResults.count } ${items }.</strong>
                </div>
            </c:if>

            <jsp:include page="searchResultsList.jsp"/>
</div> 
