<%@ include file="/common/taglibs.jsp"%>
{
	id:${taxonVariant.id},
	name:'<c:out value="${taxonVariant.name}"/>',
	namebankID:<c:out value="${taxonVariant.namebankID}"/>,
	fullName:'<c:out value="${taxonVariant.fullName}"/>',
	lexicalQualifier:'<c:out value="${taxonVariant.lexicalQualifier}"/>',
	<c:set var="taxon" value="${taxonVariant.taxon}"/>
	<c:if test="${taxon != null}">
	taxon:<%@ include file="taxonToJSON.jsp" %>
	</c:if>	
	
}