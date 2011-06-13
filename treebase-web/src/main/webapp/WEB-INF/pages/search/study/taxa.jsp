<%@ include file="/common/taglibs.jsp"%>
<title>Taxa in <c:choose>
	<c:when test="${not empty matrix}">
		matrix ${matrix.id} of
	</c:when>
	<c:when test="${not empty tree}">
		tree ${tree.id} of
	</c:when>
</c:choose> the phylogeny or phylogenetic data in study ${study.id}</title>
<body id="s-taxon" />

<c:set var="ncbiTaxonomyURL" value="http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id=" />
<c:set var="uBioTaxonomyURL" value="http://www.ubio.org/browser/details.php?namebankID=" />
<c:set var="currentSection" scope="request" value="Taxa"/>
<c:choose>
	<c:when test="${not empty matrix}">
		<c:set var="headerPrefix" scope="request" value="${currentSection} for matrix ${matrix.id} of "/>
	</c:when>
	<c:when test="${not empty tree}">
		<c:set var="headerPrefix" scope="request" value="${currentSection} for tree ${tree.id} of "/>
	</c:when>
	<c:otherwise>
		<c:set var="headerPrefix" scope="request" value="${currentSection} for "/>
	</c:otherwise>
</c:choose>

<jsp:include page="nav.jsp"/>

<c:choose>
	<c:when test="${not empty matrix}">
		<c:url var="matrixURL" value="matrix.html">
			<c:param name="id" value="${study.id}" />
			<c:param name="matrixid" value="${matrix.id}" />
		</c:url>
		<p><a href='<c:out value="${matrixURL}"/>'>
			<img class="iconButton" src="<fmt:message key="icons.back"/>" alt="Back"/>
			Return to matrix row view
		</a></p>
	</c:when>
	<c:when test="${not empty tree}">
		<c:url var="treeURL" value="tree.html">
			<c:param name="id" value="${study.id}" />
			<c:param name="treeid" value="${tree.id}" />
		</c:url>
		<p><a href='<c:out value="${treeURL}"/>'>
			<img class="iconButton" src="<fmt:message key="icons.back"/>" alt="Back"/>
			Return to complete tree view
		</a></p>
	</c:when>
</c:choose>

<display:table id="taxon" name="${taxa}" class="list">
	<%--display:column titleKey="taxonlabel.row.id">${taxon_rowNum}</display:column --%>
	<display:column property="id" title="ID" />
	<display:column property="taxonLabel" title="Taxon Label" />
	
	<display:column titleKey="taxon.ncbiTaxID">
		<c:if test="${taxon.taxonVariant!=null}">
			<c:set var="ncbiTaxoURL" value="${ncbiTaxonomyURL}${taxon.ncbiTaxID}"/>
			<a href="${ncbiTaxoURL}" target="_blank">${taxon.ncbiTaxID}</a>
		</c:if>
	</display:column>
	
	<display:column titleKey="taxon.uBioTaxID">
		<c:if test="${taxon.taxonVariant!=null}">
			<c:set var="ubioTaxoURL" value="${uBioTaxonomyURL}${taxon.taxonVariant.namebankID}"/>
			<a href="${ubioTaxoURL}" target="_blank">${taxon.taxonVariant.namebankID}</a>
		</c:if>
	</display:column>	
	
</display:table>