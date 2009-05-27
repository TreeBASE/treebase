<%@ include file="/common/taglibs.jsp"%>
<c:set var="counter" value="${analysisStepCounter}"/>
<table align="center" width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<!-- input data -->
		<td width="27%">
			<c:set var="inputOutput" value="Input" scope="request"/>
			<jsp:include page="analyzedData.jsp"/>
		</td>
		
		<!-- connector arrow -->
		<td width="3%" style="text-align:center"><img src="<fmt:message key="icons.arrow_right"/>" alt="Input" width="16" height="16"/></td>
		
		<!-- algorithm -->
		<td width="40%"><jsp:include page="algorithm.jsp"/></td>
		
		<!-- connector arrow -->
		<td width="3%" style="text-align:center"><img src="<fmt:message key="icons.arrow_right"/>" alt="Output" width="16" height="16"/></td>
		
		<!-- output data -->
		<td width="27%">
			<c:set var="inputOutput" value="Output" scope="request"/>
			<jsp:include page="analyzedData.jsp"/>		
		</td>
	</tr>
</table>