<%@ include file="/common/taglibs.jsp"%>


<% if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){ %>


<li class="nav-item dropdown">
    <a class="nav-link dropdown-toggle" href="#" id="studyMgmtMenu" role="button" data-bs-toggle="dropdown" aria-expanded="false">
        <fmt:message key="study.management"/>
    </a>
    <ul class="dropdown-menu">
        <li ><a class="dropdown-item" href="<c:url value="/admin/userManagement.html"/>"><fmt:message key="study.management.user"/></a></li>
        <li ><a class="dropdown-item" href="<c:url value="/admin/readyStateStudies.html"/>"><fmt:message key="studies.ready.state"/></a></li>
        <li ><a class="dropdown-item" href="<c:url value="/admin/searchBySubmissionID.html"/>"><fmt:message key="search.by.submission.id"/></a></li>
        <li ><a class="dropdown-item" href="<c:url value="/admin/selectStudies.html"/>"><fmt:message key="select.studies"/></a></li>
    </ul>
</li>



<li class="nav-item dropdown">
    <a class="nav-link dropdown-toggle" href="#" id="userMgmtMenu" role="button" data-bs-toggle="dropdown" aria-expanded="false">
        <fmt:message key="user.management"/>
    </a>
    <ul class="dropdown-menu">
        <li ><a class="dropdown-item" href="<c:url value="/admin/adminSelectUsers.html"/>"><fmt:message key="user.select"/></a></li>
        <li ><a class="dropdown-item" href="<c:url value="/admin/adminUpdatingUserInfo.html"/>"><fmt:message key="user.update.info"/></a></li>
        <li ><a class="dropdown-item" href="<c:url value="/admin/adminDeletingUserStepOne.html"/>"><fmt:message key="user.delete"/></a></li>
        <li ><a class="dropdown-item" href="<c:url value="/admin/adminMergingUsers.html"/>"><fmt:message key="user.merge"/></a></li>
        <li ><a class="dropdown-item" href="<c:url value="/admin/adminSelectPersons.html"/>"><fmt:message key="person.select"/></a></li>
        <li ><a class="dropdown-item" href="<c:url value="/admin/adminMergingPersons.html"/>"><fmt:message key="person.merge"/></a></li>
    </ul>
</li>
<li class="nav-item nav-divider"></li>

<% } %>

