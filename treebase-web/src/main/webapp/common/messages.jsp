<%@ include file="/common/taglibs.jsp"%>
<%-- Define newline character for splitting concatenated messages --%>
<c:set var="newline" value="&#10;"/>
<%-- Error Messages --%>
<c:if test="${not empty errors}">
    <div class="error" id="errorMessages">
        <c:choose>
            <c:when test="${errors.class.simpleName eq 'String'}">
                <%-- Handle single concatenated string with newlines --%>
                <c:forTokens var="error" items="${errors}" delims="${newline}">
                    <c:if test="${not empty fn:trim(error)}">
                        <img src="<fmt:message key="icons.warn"/>"
                            alt="<fmt:message key="icon.warning"/>" class="icon" />
                        <c:out value="${error}" escapeXml="false"/><br />
                    </c:if>
                </c:forTokens>
            </c:when>
            <c:otherwise>
                <%-- Handle collection of errors --%>
                <c:forEach var="error" items="${errors}">
                    <img src="<fmt:message key="icons.warn"/>"
                        alt="<fmt:message key="icon.warning"/>" class="icon" />
                    <c:out value="${error}" escapeXml="false"/><br />
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
    <c:remove var="errors"/>
</c:if>

<%-- Success Messages --%>
<c:if test="${not empty messages}">
    <div class="message" id="successMessages">
        <c:choose>
            <c:when test="${messages.class.simpleName eq 'String'}">
                <%-- Handle single concatenated string with newlines --%>
                <c:forTokens var="msg" items="${messages}" delims="${newline}">
                    <c:if test="${not empty fn:trim(msg)}">
                        <img src="<fmt:message key="icons.info"/>"
                            alt="<fmt:message key="icon.information"/>" class="icon" />
                        <c:out value="${msg}" escapeXml="false"/><br />
                    </c:if>
                </c:forTokens>
            </c:when>
            <c:otherwise>
                <%-- Handle collection of messages --%>
                <c:forEach var="msg" items="${messages}">
                    <img src="<fmt:message key="icons.info"/>"
                        alt="<fmt:message key="icon.information"/>" class="icon" />
                    <c:out value="${msg}" escapeXml="false"/><br />
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>