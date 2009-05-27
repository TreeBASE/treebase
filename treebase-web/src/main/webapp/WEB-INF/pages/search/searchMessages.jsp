<%@ include file="/common/taglibs.jsp"%>



<c:if test="${not empty searchMessage}">
    <div class="message" id="searchMessages">
        <c:forEach var="msg" items="${searchMessage}">
            <img src="<fmt:message key="icons.info"/>"
                alt="<fmt:message key="icon.information"/>" class="icon" />
            <c:out value="${msg}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>
