<%@ include file="/common/taglibs.jsp"%>
{
	id:${submission.id},
	submissionNumber:${submission.submissionNumber},
	submittedTaxonLabels:[
	<c:forEach var="taxonLabel" items="${submission.submittedTaxonLabelsReadOnly}" varStatus="status_analysis"><%@ include file="taxonLabelToJSON.jsp" %>,</c:forEach>	
	],
	submittedMatrices:[
	<c:forEach var="matrix" items="${submission.submittedMatricesReadOnly}" varStatus="status_analysis"><%@ include file="matrixToJSON.jsp" %>,</c:forEach>
	],
	submittedTreeBlocks:[
	<c:forEach var="treeBlock" items="${submission.submittedTreeBlocksReadOnly}" varStatus="status_analysis"><%@ include file="treeBlockToJSON.jsp" %>,</c:forEach>
	],
}