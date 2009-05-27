<%@ include file="/common/taglibs.jsp"%>
{id:${treeBlock.id},title:'<c:out value="${treeBlock.title}"/>',treeList:[
	<c:forEach var="tree" items="${treeBlock.treeList}" varStatus="status_analysis"><%@ include file="treeToJSON.jsp" %>,</c:forEach>
]}