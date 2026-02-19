<%@ include file="/common/taglibs.jsp"%>
<%@page import="org.cipres.treebase.TreebaseUtil"%>
<%@page import="java.util.Date" %>
<%@page import="java.util.Calendar" %> 
<%@page import="java.text.SimpleDateFormat" %> 

<% String purlBase = TreebaseUtil.getPurlBase(); %>

<script type="text/javascript">
	var purlBase = '<%= purlBase %>';
</script>

<%
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.MONTH, -6);
	SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");  
%>

<ul class="navbar-nav me-auto" id="s-nav">
	<li class="nav-item" id="st-study">
		<a class="nav-link" href="<c:url value="/search/studySearch.html"/>">
		<img class="iconButton" src="<fmt:message key="icons.citation"/>" alt="<fmt:message key="search.nav.studyTab"/>"/>
		<fmt:message key="search.nav.studyTab"/></a>
	</li>
	<li class="nav-item" id="st-matrix">
		<a class="nav-link" href="<c:url value="/search/matrixSearch.html"/>">
		<img class="iconButton" src="<fmt:message key="icons.matrices"/>" alt="<fmt:message key="search.nav.matrixTab"/>"/>
		<fmt:message key="search.nav.matrixTab"/></a>
	</li>
	<li class="nav-item" id="st-tree">
		<a class="nav-link" href="<c:url value="/search/treeSearch.html"/>">
		<img class="iconButton" src="<fmt:message key="icons.trees"/>" alt="<fmt:message key="search.nav.treeTab"/>"/>
		<fmt:message key="search.nav.treeTab"/></a>
	</li>
	<li class="nav-item" id="st-taxon">
		<a class="nav-link" href="<c:url value="/search/taxonSearch.html"/>">
		<img class="iconButton" src="<fmt:message key="icons.taxa"/>" alt="<fmt:message key="search.nav.taxonTab"/>"/>
		<fmt:message key="search.nav.taxonTab"/></a>
	</li>
	<li class="nav-item" id="st-treeTop">
		<a class="nav-link" href="<c:url value="/search/treeTopSearch.html"/>">
		<img class="iconButton" src="<fmt:message key="icons.tree"/>" alt="<fmt:message key="search.nav.treeTopTab"/>"/>
		<fmt:message key="search.nav.treeTopTab"/></a>
	</li>
	<li class="nav-item">
		<a class="nav-link openHelp" href="#" onclick="openHelp('searchTabs')">
			<img class="iconButton" src="<fmt:message key="icons.help"/>" alt="?"/>
		</a>
	</li>
	<li class="nav-item">
		<a class="nav-link" href="<%=purlBase%>study/find?query=prism.modificationDate%3E%22<%=isoFormat.format(cal.getTime())%>T05:00:00Z%22&format=rss1" target="_blank">
			<img class="iconButton" src="<fmt:message key="icons.rss"/>" title="<fmt:message key="download.rss"/>" alt="<fmt:message key="download.rss"/>"/>
		</a>
	</li>
	<!-- 
	<li class="nav-item" id="st-taxonView"><a class="nav-link" href="<c:url value="/search/taxonView.html"/>"><fmt:message key="search.nav.taxonViewTab"/></a></li>
	 -->
</ul>
