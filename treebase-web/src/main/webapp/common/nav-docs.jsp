<%@ include file="/common/taglibs.jsp"%>
<li class="nav-item dropdown">
    <a class="nav-link dropdown-toggle" href="#" id="documentationMenu" role="button" data-bs-toggle="dropdown" aria-expanded="false">
        <fmt:message key="nav.documentation"/>
    </a>
    <ul class="dropdown-menu">
        <li ><a class="dropdown-item" href="<c:url value="/technology.html"/>"><fmt:message key="nav.technology"/></a></li>
        <li ><a class="dropdown-item" href="<c:url value="/submitTutorial.html"/>"><fmt:message key="nav.submittutorial"/></a></li>
        <li ><a class="dropdown-item" href="<c:url value="/urlAPI.html"/>"><fmt:message key="nav.dataaccess"/></a></li>
    </ul>
</li>