<%@ include file="/common/taglibs.jsp"%>
{
	id:'<c:out value="${software.id}"/>',
	name:'<c:out value="${software.name}"/>',
	softwareVersion:'<c:out value="${software.softwareVersion}"/>',
	description:'<c:out value="${software.description}"/>',
	softwareURL:'<c:out value="${software.softwareURL}"/>',
}