<%@ include file="/common/taglibs.jsp"%>
{
	id:${analyzedData.id},inputOutput:'<c:out 
	value="${analyzedData.inputOutput}"/>',name:'<c:out 
	value="${analyzedData.displayName}"/>',dataType:'<c:out 
	value="${analyzedData.dataType}"/>',<c:if 
	test="${analyzedData.dataType eq 'matrix'}"><c:set var="matrix" value="${analyzedData.matrixData}"/>matrix:<%@ include file="matrixToJSON.jsp" %>,</c:if>	
	<c:if test="${analyzedData.dataType eq 'tree'}"><c:set var="tree" value="${analyzedData.treeData}"/>tree:<%@ include file="treeToJSON.jsp" %>,</c:if>
}