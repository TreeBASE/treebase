<%@ include file="/common/taglibs.jsp"%>

<c:if test="${not empty searchMessage}">
    <div class="alert alert-info d-flex align-items-start" id="searchMessages" role="alert">
        <img src="<fmt:message key="icons.info"/>" alt="<fmt:message key="icon.information"/>" class="me-2" style="height: 1.5em;"/>
        <div>
            <c:forEach var="msg" items="${searchMessage}">
                <c:out value="${msg}" escapeXml="false"/><br />
            </c:forEach>
        </div>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
