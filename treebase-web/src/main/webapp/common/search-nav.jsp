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


<nav class="navbar navbar-expand-lg navbar-light bg-light">
	<div class="container-fluid">
		<ul class="navbar-nav me-auto mb-2 mb-lg-0">
			<li class="nav-item">
				<a class="nav-link" href="<c:url value="/search/studySearch.html"/>">
					<i  class="fa fa-book fa-icon"></i>
					<fmt:message key="search.nav.studyTab"/>
				</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="<c:url value="/search/matrixSearch.html"/>">
					<i class="fa fa-th fa-icon"></i>
					<fmt:message key="search.nav.matrixTab"/>
				</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="<c:url value="/search/treeSearch.html"/>">
					<i class="fa fa-tree fa-icon"></i>
					<fmt:message key="search.nav.treeTab"/>
				</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="<c:url value="/search/taxonSearch.html"/>">
					<i class="fa fa-leaf fa-icon"></i>
					<fmt:message key="search.nav.taxonTab"/>
				</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="<c:url value="/search/treeTopSearch.html"/>">
					<i class="fa fa-tree fa-icon"></i>
					<fmt:message key="search.nav.treeTopTab"/>
				</a>
			</li>
			<li class="nav-item">
				<a class="nav-link openHelp" href="#" onclick="openHelp('searchTabs')">
					<i class="fa fa-question-circle fa-icon"></i> Help
				</a>
			</li>
			<li class="nav-item">
				<a class="nav-link" href="<%=purlBase%>study/find?query=prism.modificationDate%3E%22<%=isoFormat.format(cal.getTime())%>T05:00:00Z%22&format=rss1" target="_blank">
					<i class="fa fa-rss fa-icon"></i> RSS
				</a>
			</li>
		</ul>
	</div>
</nav>
