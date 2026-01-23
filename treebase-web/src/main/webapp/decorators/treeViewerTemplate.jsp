<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    
<%@include file="/common/taglibs.jsp" %>

<!--
    Tree Viewer Template
    
    This is a minimal template specifically for the phylogenetic tree viewer pages.
    It intentionally does NOT include prototype.js, scriptaculous, or DWR to avoid
    JavaScript conflicts with D3.js and phylotree.js which require unmodified
    JavaScript primitives for SVG coordinate calculations.
    
    If prototype.js is loaded, it modifies Array.prototype and other core objects
    which can cause NaN values in SVG path coordinates when D3.js tries to compute
    layout positions.
-->

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head><%@ include file="/common/meta.jsp" %>

<title>TreeBASE - <decorator:title/></title>
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/styles.css'/>" />
<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/treebase.css'/>" />

<!-- 
    NOTE: We deliberately exclude the following scripts that would conflict with D3.js:
    - prototype.js and prototype-1.6.0.3.js (modifies Array.prototype)
    - scriptaculous effects.js and controls.js (depends on prototype)
    - DWR scripts (uses prototype patterns)
    - common.js (depends on prototype's $$ and $ functions)
-->

<decorator:head/>
</head>
    
<body <decorator:getProperty property="body.id" writeEntireProperty="true"/>>
<% if( isOldMSIE ){ %>
<c:import url="/common/updateBrowser.jsp"/>
<% } %>
<!-- BEGIN WRAP -->
<div id="wrap">
    <!--  BEGIN HEADER -->
    <div id="header"><jsp:include page="/common/header.jsp"/></div>
    
    <!-- BEGIN CONTENT - Full width for tree viewer -->
    <div id="content" style="width: 100%; float: none;">
        <div class="gutter">
        <h2><decorator:getProperty property="page.heading"/></h2>
        <%@ include file="/common/messages.jsp" %>
        <decorator:body/>
        </div>
    </div>
    
    <!-- FOOTER -->
    <div id="footer"><c:import url="/common/footer.jsp" /></div>
</div> <!-- END WRAP -->
<jsp:include page="/common/googleAnalytics.jsp"/>
</body>
</html>
