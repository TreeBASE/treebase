<%@ include file="/common/taglibs.jsp"%>

<c:if test="${empty resultSet || resultSet.isAll || resultSet.resultType == 'NONE' }" var="isEmpty" scope="request"/>

<form id="resultsActionForm" name="resultsAction" method="post">
    <input type="hidden" name="formName" value="resultsAction">
    <input type="hidden" name="action" id="action" value="">

    <%--jsp:include page="searchResultsListControls.jsp"--%>
    <c:if test="${not empty messages}">
        <div class="alert alert-danger" role="alert">
            <c:forEach var="msg" items="${messages}">
                <strong>${msg}</strong><br/>
            </c:forEach>
        </div>
    </c:if>

    <div id="searchResultsList">
        <c:choose>
            <c:when test="${isEmpty}">
                <!--   (No search result set yet.) -->
            </c:when>
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

<jsp:include page="searchResultsListControls.jsp"/>

<div id="output"></div>

<script type="text/javascript">
    function doAction(action) {
        $('action').value = action;
        if (!(location.search == "")) {
            var url = location.href;
            var url_parts = url.split('?');
            $('resultsActionForm').writeAttribute('action', url_parts[0]);
        }
        $('resultsActionForm').submit();
    }
</script>

<%-- <jsp:include page="searchResultsConvert.jsp"/> --%>

