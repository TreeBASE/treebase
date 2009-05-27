<%@ include file="/common/taglibs.jsp"%>
{id:${analysisStep.id},name:'<c:out value="${analysisStep.name}"/>',validated:${analysisStep.validated},
analyzedData:[<c:forEach var="analyzedData" items="${analysisStep.dataSetReadOnly}" varStatus="status_analysis"><%@ include file="analyzedDataToJSON.jsp" %>,</c:forEach>]
}