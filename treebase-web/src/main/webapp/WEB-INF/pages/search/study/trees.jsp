<%@ include file="/common/taglibs.jsp"%>
<title>Trees for study ${study.id}</title>
<body id="s-tree"/>

<c:set var="currentSection" scope="request" value="Trees"/>
<c:set var="headerPrefix" scope="request" value="${currentSection} for "/>
<jsp:include page="nav.jsp"/>

<c:url var="studyURL" value="summary.html">
<c:param name="id" value="${study.id}"/>
</c:url>
<%
	pageContext.setAttribute("accesscode",request.getSession().getAttribute("x-access-code"));
%>

<display:table name="trees"
			   requestURI=""
			   class="list"
			   id="tree"
			   cellspacing="3"
			   cellpadding="3"
			   export="false">
	
	<display:column sortable="false" title="ID">
		<c:url var="treeViewerURL" value="/search/study/tree.html">
			<c:param name="treeid" value="${tree.id}"/>
			<c:if test="${!empty accesscode}">
				<c:param name="x-access-code" value="${accesscode}"/>
			</c:if>
		</c:url>
		<a href="${treeViewerURL}">Tr${tree.id}</a>
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
	
	<c:set var="baseURL" scope="request" value="${tree.phyloWSPath.purl}"/>	
	
	<display:column 
		sortable="false"
		class="iconColumn" 
		headerClass="iconColumn">
		<c:url var="url" value="${baseURL}">
			<c:param name="format">nexml</c:param>
			<c:if test="${!empty accesscode}">
				<c:param name="x-access-code">
					<c:out value='${accesscode}' />
				</c:param>
			</c:if>
		</c:url>
		<a href="${url}">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.xml"/>" 
				title="<fmt:message key="download.nexml"/>" 
				alt="<fmt:message key="download.nexml"/>"/>				
		</a>
	</display:column>	

	<%--<display:column 
		sortable="false"
		class="iconColumn" 
		headerClass="iconColumn">
		<c:url var="url" value="${baseURL}"><c:param name="format">rdf</c:param></c:url>
		<a href="${url}">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.rdf"/>" 
				title="<fmt:message key="download.rdf"/>" 
				alt="<fmt:message key="download.rdf"/>"/>				
		</a>
	</display:column>	--%>
	
	<display:column 
		sortable="false"
		class="iconColumn" 
		headerClass="iconColumn">
		<c:url var="url" value="${baseURL}">
			<c:param name="format">nexus</c:param>
			<c:if test="${!empty accesscode}">
				<c:param name="x-access-code">
					<c:out value='${accesscode}' />
				</c:param>
			</c:if>
		</c:url>
		<a href="${url}">
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
			<c:if test="${!empty accesscode}">
				<c:param name="x-access-code">
					<c:out value='${accesscode}' />
				</c:param>
			</c:if>		
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
		<c:url var="treeViewerURL" value="/search/study/tree.html">
			<c:param name="treeid" value="${tree.id}"/>
			<c:if test="${!empty accesscode}">
				<c:param name="x-access-code" value="${accesscode}"/>
			</c:if>
		</c:url>
		<a href="${treeViewerURL}">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.tree.view"/>" 
				title="<fmt:message key="tree.view"/>" 
				alt="<fmt:message key="tree.view"/>"/>   		
   		</a>
	</display:column>	

</display:table>


