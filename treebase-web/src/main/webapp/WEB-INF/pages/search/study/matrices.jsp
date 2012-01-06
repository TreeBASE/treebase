<%@ include file="/common/taglibs.jsp"%>

<title>Matrices for study ${study.id}</title>
<body id="s-matrix"/>

<c:set var="currentSection" scope="request" value="Matrices"/>
<c:set var="headerPrefix" scope="request" value="${currentSection} for "/>

<jsp:include page="nav.jsp"/>

<c:url var="studyURL" value="summary.html">
<c:param name="id" value="${study.id}"/>
</c:url>

<c:set var="counter" value="0"/>
<%
	pageContext.setAttribute("accesscode",request.getSession().getAttribute("x-access-code"));
%>
<display:table name="matrices"
			   requestURI=""
			   class="list"
			   id="matrix"
			   cellspacing="3"
			   cellpadding="3">

	<display:column title="ID">
		<c:url var="url" value="${matrix.phyloWSPath.purl}"><c:param name="format">html</c:param></c:url>
		<a href="${url}">${matrix.treebaseIDString}</a>
	</display:column>
	
	<display:column  
		property="title" 
		titleKey="matrix.title" 
		sortable="true"/>

				
	<display:column  
		property="description" 
		titleKey="matrix.description" 
		sortable="true"/>

	<display:column 
		property="matrixKind.description" 
		title="Data type"
		sortable="false">
	</display:column>
	
	<display:column
		property="nTax"
		title="NTAX"
		sortable="true">
	</display:column>	
	
	<display:column
		property="nChar"
		title="NCHAR"
		sortable="true">
	</display:column>	

	<display:column title="Taxa">
		<c:url value="taxa.html" var="taxaURL">
			<c:param name="id">${study.id}</c:param>
			<c:param name="matrixid">${matrix.id}</c:param>
		</c:url>
		<a href="${taxaURL}">View Taxa</a>
	</display:column>
	
	<display:column  
		sortable="false"
		class="iconColumn" 
		headerClass="iconColumn">
		<c:url var="url" value="${matrix.phyloWSPath.purl}">
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
		<c:url var="url" value="${matrix.phyloWSPath.purl}"><c:param name="format">rdf</c:param></c:url>
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
		<c:url var="url" value="${matrix.phyloWSPath.purl}">
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
		<c:url value="/search/downloadANexusFile.html" var="originalMatrixURL">
			<c:param name="id">${study.id}</c:param>
			<c:param name="matrixid">${matrix.id}</c:param>
			<c:if test="${!empty accesscode}"><c:param name="x-access-code"><c:out value='${accesscode}' /></c:param></c:if>		
		</c:url>
		<a href="${originalMatrixURL}">
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
		<c:url var="url" value="${matrix.phyloWSPath.purl}">
			<c:param name="format">html</c:param>
			<c:if test="${!empty accesscode}">
				<c:param name="x-access-code">
					<c:out value='${accesscode}' />
				</c:param>
			</c:if>
		</c:url>
		<a href="${url}">		
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.list"/>" 
				title="<fmt:message key="matrix.row.list"/>" 
				alt="<fmt:message key="matrix.row.list"/>"/>
		</a>				
	</display:column>					

</display:table>

