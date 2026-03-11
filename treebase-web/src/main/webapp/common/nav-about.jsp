<%@ include file="/common/taglibs.jsp"%>

<li class="nav-item dropdown">
    <a class="nav-link dropdown-toggle" href="#" id="aboutMenu" role="button" data-bs-toggle="dropdown" aria-expanded="false">
        <fmt:message key="nav.about"/>
    </a>
    <ul class="dropdown-menu">
        <li><a class="dropdown-item" href="<c:url value="/about.html"/>"><fmt:message key="nav.overview"/></a></li>
        <li><a class="dropdown-item" href="<c:url value="/people.html"/>"><fmt:message key="nav.people"/></a></li>
        <li><a class="dropdown-item" href="<c:url value="/partnership.html"/>"><fmt:message key="nav.partnerships"/></a></li>
        <li><a class="dropdown-item" href="<c:url value="/reference.html"/>"><fmt:message key="nav.references"/></a></li>
    </ul>
</li>
