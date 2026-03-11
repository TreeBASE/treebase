<%@ include file="/common/taglibs.jsp"%>

<c:if test="${empty resultSet || resultSet.isAll || resultSet.resultType == 'NONE' }" var="isEmpty" scope="request"/>

<c:if test="${!isEmpty}">
<div class="card mt-4">
    <div class="card-header d-flex justify-content-between align-items-center">
        <span class="fw-semibold">
            Search Results
            <c:if test="${not empty resultSet}">
                <span class="badge bg-secondary ms-2">${resultSet.count}</span>
            </c:if>
        </span>
    </div>
    <div class="card-body">
        <form id="resultsActionForm" name="resultsAction" method="post">
            <input type="hidden" name="formName" value="resultsAction">
            <input type="hidden" name="action" id="action" value="">

            <c:if test="${not empty messages}">
                <div class="alert alert-danger" role="alert">
                    <c:forEach var="msg" items="${messages}">
                        <strong>${msg}</strong><br/>
                    </c:forEach>
                </div>
            </c:if>

            <div id="searchResultsList" class="table-responsive">
                <c:choose>
                    <c:when test="${resultSet.resultType == 'STUDY'}">
                        <jsp:include page="studyList.jsp"/>
                    </c:when>
                    <c:when test="${resultSet.resultType == 'MATRIX'}">
                        <jsp:include page="matrixList.jsp"/>
                    </c:when>
                    <c:when test="${resultSet.resultType == 'TREE'}">
                        <jsp:include page="treeList.jsp"/>
                    </c:when>
                    <c:when test="${resultSet.resultType == 'TAXON'}">
                        <jsp:include page="taxonList.jsp"/>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning" role="alert">Unknown search results type</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </form>
    </div>
    <div class="card-footer">
        <jsp:include page="searchResultsListControls.jsp"/>
    </div>
</div>
</c:if>

<div id="output"></div>

<script type="text/javascript">
    function doAction(action) {
        document.getElementById('action').value = action;
        var form = document.getElementById('resultsActionForm');
        if (location.search !== "") {
            var url = location.href;
            var url_parts = url.split('?');
            form.action = url_parts[0];
        }
        form.submit();
    }
</script>

<%-- <jsp:include page="searchResultsConvert.jsp"/> --%>

