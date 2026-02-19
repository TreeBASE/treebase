<%@ include file="/common/taglibs.jsp"%>
<div id="sidebarLeftGutter">
	<div id="navcontainer">
		<div class="list-group" id="navlist">
			<a class="list-group-item list-group-item-action" href="<c:url value="/search/studySearch.html"/>"><fmt:message key="nav.search.treebase"/></a>
			<a class="list-group-item list-group-item-action" href="<c:url value="/user/processUser.html"/>"><fmt:message key="nav.submit"/></a>
			<span class="list-group-item heading"><fmt:message key="nav.documentation"/></span>
			<div class="inner-nav list-group">
				<a class="list-group-item list-group-item-action" href="<c:url value="/technology.html"/>"><fmt:message key="nav.technology"/></a>
				<a class="list-group-item list-group-item-action" href="<c:url value="/submitTutorial.html"/>"><fmt:message key="nav.submittutorial"/></a>
				<a class="list-group-item list-group-item-action" href="<c:url value="/urlAPI.html"/>"><fmt:message key="nav.dataaccess"/></a>
			</div>
			<span class="list-group-item heading"><fmt:message key="nav.about"/></span>
			<div class="inner-nav list-group">
				<a class="list-group-item list-group-item-action" href="<c:url value="/about.html"/>"><fmt:message key="nav.overview"/></a>
				<a class="list-group-item list-group-item-action" href="<c:url value="/people.html"/>"><fmt:message key="nav.people"/></a>
				<a class="list-group-item list-group-item-action" href="<c:url value="/partnership.html"/>"><fmt:message key="nav.partnerships"/></a>
				<a class="list-group-item list-group-item-action" href="<c:url value="/reference.html"/>"><fmt:message key="nav.references"/></a>
			</div>
			<a class="list-group-item list-group-item-action" href="<c:url value="/dataMan.html"/>"><fmt:message key="nav.dataman"/></a>
			<a class="list-group-item list-group-item-action" href="<c:url value="/journal.html"/>"><fmt:message key="nav.journals"/></a>
			<a class="list-group-item list-group-item-action" href="<c:url value="/contact.html"/>"><fmt:message key="nav.contact"/></a>
		</div>
	</div>
	<br/>
	<center>
		<a href="http://wokinfo.com//products_tools/multidisciplinary/dci/" title="Covered by Data Citation Index">
			<img src="<c:url value="images/DCI_button.png"/>" alt="Covered by Data Citation Index" />
		</a>
	</center>
</div>
