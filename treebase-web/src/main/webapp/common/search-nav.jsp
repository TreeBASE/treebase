<%@ include file="/common/taglibs.jsp"%>

<ul id="s-nav">
	<li id="s-navlabel">Search</li>
	<li id="st-study">
		<a href="<c:url value="/search/studySearch.html"/>">
		<img class="iconButton" src="<fmt:message key="icons.citation"/>" />
		<fmt:message key="search.nav.studyTab"/></a>
	</li>
	<li id="st-matrix">
		<a href="<c:url value="/search/matrixSearch.html"/>">
		<img class="iconButton" src="<fmt:message key="icons.matrices"/>" />
		<fmt:message key="search.nav.matrixTab"/></a>
	</li>
	<li id="st-tree">
		<a href="<c:url value="/search/treeSearch.html"/>">
		<img class="iconButton" src="<fmt:message key="icons.trees"/>" />
		<fmt:message key="search.nav.treeTab"/></a>
	</li>
	<li id="st-taxon">
		<a href="<c:url value="/search/taxonSearch.html"/>">
		<img class="iconButton" src="<fmt:message key="icons.taxa"/>" />
		<fmt:message key="search.nav.taxonTab"/></a>
	</li>	
	<li id="st-treeTop">
		<a href="<c:url value="/search/treeTopSearch.html"/>">
		<img class="iconButton" src="<fmt:message key="icons.tree"/>" />
		<fmt:message key="search.nav.treeTopTab"/></a>
	</li>
	<li style="background-color:transparent">
  		<a href="#" class="openHelp" style="background-color:transparent; border:none" onclick="openHelp('searchTabs')">
  			<img class="iconButton" src="<fmt:message key="icons.help"/>" />
  		</a>		
	</li>	
	<!-- 
	<li id="st-taxonView"><a href="<c:url value="/search/taxonView.html"/>"><fmt:message key="search.nav.taxonViewTab"/></a></li>
	 -->
</ul>
