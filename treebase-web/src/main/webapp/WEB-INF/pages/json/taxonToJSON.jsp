<%@ include file="/common/taglibs.jsp"%>
{
	id:${taxon.id},
	name:'<c:out value="${taxon.name}"/>',
	description:'<c:out value="${taxon.description}"/>',
	ncbiTaxId:'<c:out value="${taxon.ncbiTaxId}"/>',
	groupCode:'<c:out value="${taxon.groupCode}"/>',
	UBioNamebankId:'<c:out value="${taxon.UBioNamebankId}"/>',
	foreignLinks:[
	<c:forEach var="taxonLink" items="${taxon.foreignLinks}" varStatus="status_analysis"><%@ include file="taxonLinkToJSON.jsp" %>,</c:forEach>		
	]
}