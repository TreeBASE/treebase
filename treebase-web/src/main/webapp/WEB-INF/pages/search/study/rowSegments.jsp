<%@ include file="/common/taglibs.jsp"%>

<%@ page import="org.cipres.treebase.domain.taxon.SpecimenLabel" %>

<%!
String specimenInfoXXX(SpecimenLabel sl) {
	  return "(specimen info)";
}
%>

<head>
<c:url var="studyURL" value="summary.html">
	<c:param name="id" value="${study.id}" />
</c:url>
<c:url var="matricesURL" value="matrices.html">
	<c:param name="id" value="${study.id}" />
</c:url>
<c:url var="matrixURL" value="matrix.html">
	<c:param name="id" value="${study.id}" />
	<c:param name="matrixid" value="${matrix.id}" />
</c:url>
<c:forEach var="row" items="${matrix.rowsReadOnly}" varStatus="status">
   <c:if test="${row.id == matrixrow.id}">
   	<c:set var="rownum" value="${status.count}"/>
   </c:if>
</c:forEach>

<title>A matrix row</title>
</head>


<body id="matrixrow">

<h2>Row ${rownum } of matrix ${matrix.id} 
<c:if test="${ not empty matrix.title }">(${matrix.title })</c:if>
</h2>


<p>Attached to study ${study.id } ("${study.citation.title }")</p>

<c:if test="${ not empty matrix.description }">
	<h3>${matrix.description}</h3>
</c:if>
<h3>Row Segments</h3>

<display:table name="${matrixrow.segmentsReadOnly}" requestURI=""
	class="list" id="seg" cellspacing="3" cellpadding="3" export="false">

	<display:column property="id" title="ID" sortable="true"
		style="text-align:left; width:10%" />
		
	<display:column property="title" title="Title" sortable="true"
		style="text-align:left; width:10%" />

	<display:column title="Position" sortable="true"
		style="text-align:left;width:20%">
		${seg.startIndex }&ndash;${seg.endIndex }
	</display:column>

	<display:column title="Specimen Information"
		sortable="true"
		style="text-align:left;  font-family:Courier; width:20%;">
		${seg.specimenInfo}
		</display:column>
	<%-- 
	<display:column title="Segment data" property="segmentData"
		sortable="false"
		style="text-align:left;  text-size:smaller font-family:Courier; width:10%;" />
 --%>

</display:table>


<p><a href='<c:out value="${studyURL}"/>'>Return to study summary</a></p>
<p><a href='<c:out value="${matricesURL}"/>'>Return to list of matrices for this study</a></p>
<p><a href='<c:out value="${matrixURL}"/>'>Return to this row's matrix</a></p>

</body>
