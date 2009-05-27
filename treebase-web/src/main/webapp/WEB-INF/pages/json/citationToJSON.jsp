<%@ include file="/common/taglibs.jsp"%>
<c:if test="${citation == null}">null</c:if>
<c:if test="${citation != null}">{id:${citation.id}}</c:if>