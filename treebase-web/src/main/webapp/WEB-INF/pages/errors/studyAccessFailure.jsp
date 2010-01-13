<%@ include file="/common/taglibs.jsp" %>

<title>Submission Access Error</title>
<content tag="heading">Submission Access Error</content>

<p>
<img src="<c:url value="/images/iconWarning.gif"/>"
     alt="<fmt:message key="icon.warning"/>" class="icon" />
	<c:out value="${requestScope.exception.message}"/>
</p>

<c:import url="/user/submissionList.html"/>