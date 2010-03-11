<%@ include file="/common/taglibs.jsp"%>

<div class="gutter">
	<div id="navcontainer">
		<ul id="navlist">
			<li><a href="<c:url value="/search/studySearch.html"/>"><fmt:message key="nav.search.treebase"/></a></li>
			<li><a href="<c:url value="/user/processUser.html"/>"><fmt:message key="nav.submit"/></a></li>					
			<li><span style="font-weight:bold;border-bottom: 1px solid #CCC;padding:3px;display:block"><fmt:message key="nav.about"/></span>
				<ul id="innerNavlist">
					<li><a href="<c:url value="/about.html"/>"><fmt:message key="nav.overview"/></a></li>
					<li><a href="#"><fmt:message key="nav.technology"/></a></li>
					<li><a href="<c:url value="/people.html"/>"><fmt:message key="nav.people"/></a></li>
					<li><a href="#"><fmt:message key="nav.partnerships"/></a></li>
				</ul>
			</li>			
			<li><a href="<c:url value="/urlAPI.html"/>"><fmt:message key="nav.dataaccess"/></a></li>
			<li><a href="#"><fmt:message key="nav.contact"/></a></li>			
		</ul>
	</div>
</div>
