<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="search.results.title.matrices"/></title>
<%--content tag="heading"><fmt:message key="search.results.title.matrices"/></content>
<body id="submissions"/ --%>

<form method="post">

<c:set var="counter"   value="0"/>

<display:table name="${resultSet.results}"
			   requestURI=""
			   class="list"
			   id="matrix"
			   cellspacing="3"
			   cellpadding="3"
			   export = "false">
			   
	<display:column title="" sortable="true" class="checkBoxColumn">
		<c:url var="url" value="study/matrix.html">
			<c:param name="matrixid" value="${matrix.id}" />
			<c:param name="id" value="${matrix.study.id}" />
		</c:url>
		<input type="checkbox"  name="selection" id="s-${matrix.id}" value="${matrix.id }" /> 
		<a href="${url}">M${matrix.id }</a>
	</display:column>
				
	<display:column  titleKey="matrix.title" property="title" sortable="true">
		<c:set var="counter" value="${counter+1}"/>	
	</display:column>
	
	<display:column property="description" titleKey="matrix.description" sortable="true"/>
	
	<display:column property="matrixKind.description" title="Data type" sortable="true"/>
				
	<%--display:column property="dataType.description" title="Matrix type" sortable="true"/ --%>
				
	<display:column title="NTAX" property="nTax" sortable="true"/>	
	
	<display:column title="NCHAR" property="nChar" sortable="true"/>

	<%--display:column title="Taxa">
		<c:url value="study/taxa.html" var="taxaURL">
			<c:param name="id">${matrix.study.id}</c:param>
			<c:param name="matrixid">${matrix.id}</c:param>
		</c:url>
		<a href="${taxaURL}">View Taxa</a>
	</display:column --%>
	
	<display:column  
		sortable="false"
		class="iconColumn" 
		headerClass="iconColumn">
		<c:url value="/search/downloadANexusFile.html" var="originalMatrixURL">
			<c:param name="id">${matrix.study.id}</c:param>
			<c:param name="matrixid">${matrix.id}</c:param>		
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
		<c:url value="/search/downloadAMatrix.html" var="newMatrixURL">
			<c:param name="id">${matrix.study.id}</c:param>
			<c:param name="matrixid">${matrix.id}</c:param>
		</c:url>
		<a href="${newMatrixURL}">
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
		<c:url var="url" value="study/matrix.html">
			<c:param name="matrixid" value="${matrix.id}" />
			<c:param name="id" value="${matrix.study.id}" />
		</c:url>
		<a href="${url}">		
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.list"/>" 
				title="<fmt:message key="matrix.row.list"/>" 
				alt="<fmt:message key="matrix.row.list"/>"/>
		</a>				
	</display:column>	
				
<%--  
	<display:column titleKey="matrix.download" sortable="false"
				url="/search/downloadAMatrix.html" paramId="matrixid" paramProperty="id"
				style="text-align:left;width:10%">Download Matrix
	</display:column>
	
				
	<display:column titleKey="link.view" 
				sortable="false"
				style="text-align:left;width:7%">
						<c:url var="url" value="study/matrix.html">
			<c:param name="matrixid" value="${matrix.id}" />
			<c:param name="id" value="${matrix.study.id}" />
		</c:url>
		<a href="${url }">View rows</a> 
	</display:column>
				
	<c:if test="${publicationState eq 'NotReady'}">			
		<display:column titleKey="link.delete" 
				sortable="false"
				url="/user/deleteAMatrix.html" paramId="matrixid" paramProperty="id"
				style="text-align:left;width:7%">Delete Matrix
		</display:column>	
	</c:if>
	
		
	<display:column titleKey="matrix.download" sortable="false"
				url="/search/downloadANexusFile.html" paramId="matrixid" paramProperty="id"
				style="text-align:left;width:15%">Original Nexus File
	</display:column>	
	
	--%>	
	
	<display:footer>
		<tr>
			<!--  this id is important, because we might add additional buttons here -->
    		<td colspan="9" align="center" id="buttonContainer">
				<!--  insert additional controls here -->
        	</td>
    	</tr>
    </display:footer>
    <display:setProperty name="basic.empty.showtable" value="true"/>	
	
</display:table>
</form>
