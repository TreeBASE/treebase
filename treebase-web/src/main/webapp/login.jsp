<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="login.title"/></title>
<content tag="heading"><fmt:message key="login.title"/></content>

<p>Please fill in following information to login<br/>
<span class="required">* Required Fields</span></p>

<body id="info"/>

<!-- loginForm.jsp -->
<c:import url="/WEB-INF/pages/loginForm.jsp"/>
<!-- password -->
<p>Forgot your password? Have your <a href="<c:url value="passwordForm.html"/>">password e-mailed to you</a></p>
