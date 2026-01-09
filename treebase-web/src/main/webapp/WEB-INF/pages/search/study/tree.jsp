<%@include file="/common/taglibs.jsp" %>
<%-- 
    This page now redirects to the unified tree viewer.
    The old Java applet has been replaced with phylotree.js.
--%>
<c:redirect url="/user/directMapToPhyloWidget.html">
    <c:if test="${not empty param.treeid}">
        <c:param name="treeid" value="${param.treeid}"/>
    </c:if>
    <c:if test="${not empty param.treeblockid}">
        <c:param name="treeblockid" value="${param.treeblockid}"/>
    </c:if>
</c:redirect>

