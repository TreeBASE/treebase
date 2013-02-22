<%@ include file="/common/taglibs.jsp"%>
<div>
	<div class="gutter" id="sidebarLeftGutter">
		<div id="navcontainer">
			<ul id="navlist">
				<li><a href="<c:url value="/search/studySearch.html"/>"><fmt:message key="nav.search.treebase"/></a></li>
				<li><a href="<c:url value="/submitTutorial.html"/>"><fmt:message key="nav.submittutorial"/></a></li>
				<li><a href="<c:url value="/user/processUser.html"/>"><fmt:message key="nav.submit"/></a></li>					
				<li><span class="navlistHeading"><fmt:message key="nav.about"/></span>
					<ul id="innerNavlist">
						<li><a href="<c:url value="/about.html"/>"><fmt:message key="nav.overview"/></a></li>
						<li><a href="<c:url value="/technology.html"/>"><fmt:message key="nav.technology"/></a></li>
						<li><a href="<c:url value="/people.html"/>"><fmt:message key="nav.people"/></a></li>
						<li><a href="<c:url value="/partnership.html"/>"><fmt:message key="nav.partnerships"/></a></li>
						<li><a href="<c:url value="/reference.html"/>"><fmt:message key="nav.references"/></a></li>
					</ul>
				</li>			
				<li><a href="<c:url value="/dataMan.html"/>"><fmt:message key="nav.dataman"/></a></li>
				<li><a href="<c:url value="/urlAPI.html"/>"><fmt:message key="nav.dataaccess"/></a></li>
				<li><a href="<c:url value="/journal.html"/>"><fmt:message key="nav.journals"/></a></li>
				<li><a href="<c:url value="/contact.html"/>"><fmt:message key="nav.contact"/></a></li>			
			</ul>
		</div>	
		<br /><br />		
		<center>
			<a href="http://www.mendeley.com/groups/734351/treebase/" title="All publications in TreeBASE on Mendeley"><img src="<c:url value="images/mendeley-logo.png"/>" alt="All publications in TreeBASE on Mendeley" /></a>
			<br /><br />
			<a href="http://twitter.com/treebase" title="Follow @TreeBASE on Twitter"><img src="<c:url value="images/twitter-logo.png"/>" alt="Follow @TreeBASE on twitter" /></a>			
			<br /><br />
			<a href="http://wokinfo.com//products_tools/multidisciplinary/dci/" title="Covered by Data Citation Index"><img src="<c:url value="images/DCI_button.png"/>" alt="Covered by Data Citation Index" /></a>			
		</center>
	</div>
	<img src="<c:url value="images/footer_bg.gif"/>" style="width:100%" alt="" />
</div>