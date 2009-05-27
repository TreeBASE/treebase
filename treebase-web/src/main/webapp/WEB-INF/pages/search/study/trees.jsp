<%@ include file="/common/taglibs.jsp"%>
<title>Trees for study ${study.id}</title>
<body id="s-tree"/>

<c:set var="currentSection" scope="request" value="Trees"/>
<c:set var="headerPrefix" scope="request" value="${currentSection} for "/>
<jsp:include page="nav.jsp"/>

<c:url var="studyURL" value="summary.html">
<c:param name="id" value="${study.id}"/>
</c:url>

<display:table name="trees"
			   requestURI=""
			   class="list"
			   id="tree"
			   cellspacing="3"
			   cellpadding="3"
			   export="false">
	
	<display:column sortable="false" title="ID">
    	<c:url var="treeURL" value="tree.html">
      		<c:param name="id" value="${study.id}" />
      		<c:param name="treeid" value="${tree.id}" />
   		</c:url>
   		<a href="javascript:popupWithSizes('${treeURL}',1000,900,'1')">Tr${tree.id}</a>
	</display:column>	
	
	<display:column  
		property="label" 
		titleKey="tree.label" 
		sortable="true">
	</display:column>
	
	<display:column 
		property="title" 
		titleKey="tree.title" 
		sortable="true">
	</display:column>
	
	<display:column 
		property="treeType.description" 
		title="Tree Type" 
		sortable="true">	
	</display:column>
	
	<display:column property="treeKind.description" title="Tree Kind" sortable="true"/>
	
	<display:column title="Taxa">
		<c:url value="taxa.html" var="taxaURL">
			<c:param name="id">${study.id}</c:param>
			<c:param name="treeid">${tree.id}</c:param>
		</c:url>
		<a href="${taxaURL}">View Taxa</a>
	</display:column>
	
	<display:column 
		sortable="false"
		class="iconColumn" 
		headerClass="iconColumn">
		<c:url value="/search/downloadATree.html" var="newTreeURL">
			<c:param name="id">${study.id}</c:param>
			<c:param name="treeid">${tree.id}</c:param>
		</c:url>
		<a href="${newTreeURL}">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.download.reconstructed"/>" 
				title="<fmt:message key="download.reconstructedfile"/>" 
				alt="<fmt:message key="download.reconstructedfile"/>"/>				
		</a>
	</display:column>
	
	<display:column  
		sortable="false"
		class="iconColumn" 
		headerClass="iconColumn">
		<c:url value="/search/downloadANexusFile.html" var="originalTreeURL">
			<c:param name="id">${study.id}</c:param>
			<c:param name="treeid">${tree.id}</c:param>		
		</c:url>
		<a href="${originalTreeURL}">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.download.original"/>" 
				title="<fmt:message key="download.original"/>" 
				alt="<fmt:message key="download.original"/>"/>		
		</a>
	</display:column>
	
	<display:column 
		sortable="false"
		class="iconColumn" 
		headerClass="iconColumn">
    	<c:url var="treeURL" value="tree.html">
      		<c:param name="id" value="${study.id}" />
      		<c:param name="treeid" value="${tree.id}" />
   		</c:url>
   		<a href="javascript:popupWithSizes('${treeURL}',1000,900,'1')">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.list"/>" 
				title="<fmt:message key="tree.list.title"/>" 
				alt="<fmt:message key="tree.list.title"/>"/>   		
   		</a>
	</display:column>	

</display:table>


