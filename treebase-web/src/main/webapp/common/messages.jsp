<%@ include file="/common/taglibs.jsp"%>
<%-- Error Messages --%>
<c:if test="${not empty errors}">
    <div class="alert alert-danger error" id="errorMessages" role="alert">
        <%-- Use scriptlet to safely check type (avoids EL issues with .class on Lists) --%>
        <% Object errorsObj = request.getAttribute("errors");
           if (errorsObj == null) errorsObj = session.getAttribute("errors");
           boolean errorsIsString = errorsObj instanceof String;
           request.setAttribute("errorsIsString", errorsIsString);
        %>
        <c:choose>
            <c:when test="${errorsIsString}">
                <%-- Handle single string (may contain newlines) --%>
                <c:forTokens var="error" items="${errors}" delims="&#10;">
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
    <c:remove var="errorsIsString"/>
</c:if>

<%-- Success Messages --%>
<c:if test="${not empty messages}">
    <div class="alert alert-success message" id="successMessages" role="alert">
        <%-- Use scriptlet to safely check type (avoids EL issues with .class on Lists) --%>
        <% Object messagesObj = request.getAttribute("messages");
           if (messagesObj == null) messagesObj = session.getAttribute("messages");
           boolean messagesIsString = messagesObj instanceof String;
           request.setAttribute("messagesIsString", messagesIsString);
        %>
        <c:choose>
            <c:when test="${messagesIsString}">
                <%-- Handle single string (may contain newlines) --%>
                <c:forTokens var="msg" items="${messages}" delims="&#10;">
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
    <c:remove var="messagesIsString"/>
</c:if>