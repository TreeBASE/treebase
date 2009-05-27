<%@ include file="/common/taglibs.jsp"%>

<c:if test="${empty resultSet || resultSet.isAll }" var="isEmpty" scope="request"/>

<form id="taxonResultsAction" name="taxonResultsAction" method="post">
<input type="hidden" name="formName" value="taxonResultsAction">
<input type="hidden" name="action" id="action" value="">

<c:set var="mjdPageScope" value="set" scope="page"/>
<c:set var="mjdRequestScope" value="set" scope="request"/>
<c:set var="mjdSessionScope" value="set" scope="session"/>
<c:set var="mjdAppScope" value="set" scope="application"/>


<div id="searchResultsList">
<c:set var="overrideResults" value="${taxonSearchResults}" scope="request"/>
<jsp:include page="taxonList.jsp"/> 
</div>
</form>

<%--jsp:include page="taxonList.jsp"/--%> 

<jsp:include page="searchResultsListControls.jsp"/> 

<div id="output"></div>
  
<script type="text/javascript">
    
    function doAction(action) {
    	$('action').value = action;
    	$('resultsActionForm').submit();
    }
 
</script>

 
<%-- <jsp:include page="searchResultsConvert.jsp"/> --%>

