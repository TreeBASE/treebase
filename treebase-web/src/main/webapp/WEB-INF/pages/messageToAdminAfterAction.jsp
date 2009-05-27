<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="admin.message"/></title>
<content tag="heading"><fmt:message key="admin.message"/></content>
<body id="admin"/>
<c:if test="${not empty MESSAGE_TO_ADMINISTRATOR}">
	<h3><strong> ${MESSAGE_TO_ADMINISTRATOR}</strong></h3>
</c:if>