<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("carriageReturn","\n"); %>

<c:set var="currentSection" scope="request" value="Matrices"/>
<c:set var="headerPrefix" scope="request" value="Matrix ${matrix.id} of "/>
<jsp:include page="nav.jsp"/>

<c:url var="studyURL" value="summary.html">
<c:param name="id" value="${study.id}"/>
</c:url>
<c:url var="matricesURL" value="matrices.html">
<c:param name="id" value="${study.id}"/>
</c:url>
<c:url var="rowSegmentsTSV" value="rowSegmentsTSV.html">
	<c:param name="matrixid" value="${matrix.id}" />
</c:url>

<title>Matrix ${matrix.id} of study ${study.id}</title>
<body id="s-matrix"/>

<c:if test="${ not empty matrix.title }">
	<p><strong>Title</strong>: ${matrix.title }</p>
</c:if>

<c:if test="${ not empty matrix.description }">
	<p><strong>Description</strong>: ${matrix.description}</p>
</c:if>

<c:set var="rowSegmentCount" value="false" />

<c:forEach var="row" items="${matrix.rowsReadOnly}" varStatus="status">
	<c:if test="${fn:length(row.segmentsReadOnly) gt 0}">
		<c:set var="rowSegmentCount" value="true" />
	</c:if>
</c:forEach>

<c:if test="${rowSegmentCount}">
	<a href="${rowSegmentsTSV}">Download all Row Segment Metadata</a>
</c:if>

<h2>Rows</h2>

<c:if test="${empty matrix}">
  <p class="warning">
    Sorry, that is not a character matrix.
  </p>
</c:if>
<display:table name="${matrix.rowsReadOnly}" requestURI="" class="list"
	id="row" cellspacing="3" cellpadding="3" export="false">

	<display:column property="taxonLabel.taxonLabel"
		titleKey="matrix.row.taxon.label" sortable="true"
		style="text-align:left; width:20%" />

	<display:column title="Row Segments" sortable="false"
		style="text-align:left;width:10%">
		<c:set var="noSegments" value="${empty(row.segmentsReadOnly)}" />
	<c:if test="${noSegments}">
					&nbsp;(none)
				</c:if>
		<c:if test="${! noSegments}">
			<c:url var="rowSegURL" value="rowSegments.html">
				<c:param name="id" value="${study.id}" />
				<c:param name="matrixid" value="${matrix.id}" />
				<c:param name="matrixrowid" value="${row.id}" />
			</c:url>
			<a href="${rowSegURL }">View</a>
		</c:if>
	</display:column>

	<display:column title="Characters 1­&ndash;30" property="elementStringForDisplay"
		sortable="false"
		style="text-align:left;  font-family:Courier; width:40%;" />

</display:table>

<br/>
<h2>Columns</h2>

<c:set var="columns" value="${matrix.describedColumns}"/>
<c:if test="${ empty columns}">
<p>None of the columns has a description.</p>
</c:if>

<c:if test="${not empty columns }">

<display:table name="${matrix.describedColumns}"
			   requestURI=""		   
			   class="list"
			   id="col"
			   cellspacing="3"
			   cellpadding="3"
			   export = "false">


	<display:column 
				 style="text-align:center; width:15%" title="Column">
				 ${ col.columnOrder + 1}
				 </display:column>
	
	<display:column title="Character Description">
	<c:if test="${     empty col.character.description }">(none)</c:if>
	<c:if test="${ not empty col.character.description }">${col.character.description}</c:if>	
	</display:column>
	
</display:table></c:if>

</body>
