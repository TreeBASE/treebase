<%@ include file="/common/taglibs.jsp"%>
{
id:${study.id},
name:'<c:out value="${study.name}"/>',
<% pageContext.setAttribute("returnLineChar", "\r"); %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<c:set var="notes">
	${fn:replace(study.notes,returnLineChar, "\\r")}
</c:set>
<c:set var="notes">
	${fn:replace(notes,newLineChar, "\\n")}
</c:set>
notes:'<c:out value="${notes}"/>',
authors:[<c:forEach var="author" items="${study.authors}" varStatus="status_analysis">
{id:${author.id},fullNameCitationStyle:'<c:out value="${author.fullNameCitationStyle}"/>'},
</c:forEach>],
<c:set var="citation" value="${study.citation}"/>
citation:<%@ include file="citationToJSON.jsp" %>,
analyses:[<c:forEach var="analysis" items="${study.analysesReadOnly}" varStatus="status_analysis">{
	id:${analysis.id},
	name:'<c:out value="${analysis.name}"/>',
	validated:${analysis.validated},
	analysisSteps:[<c:forEach var="analysisStep" items="${analysis.analysisStepsReadOnly}" varStatus="status_analysis">
		<%@ include file="analysisStepToJSON.jsp" %>,
	</c:forEach>]
},
</c:forEach>],
nexusFileNames:[<c:forEach var="file" items="${study.nexusFileNames}">{id:'<c:out value="${file}"/>'},</c:forEach>]
}