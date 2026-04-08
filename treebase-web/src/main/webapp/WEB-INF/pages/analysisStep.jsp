<%@ include file="/common/taglibs.jsp"%>
<c:set var="counter" value="${analysisStepCounter}"/>
<div class="card mb-3 border-light">
	<div class="card-body p-2">
		<div class="row align-items-center">
			<%-- Input data --%>
			<div class="col-md-4">
				<c:set var="inputOutput" value="Input" scope="request"/>
				<jsp:include page="analyzedData.jsp"/>
			</div>
			
			<%-- Connector arrow --%>
			<div class="col-md-1 text-center">
				<i class="fa fa-arrow-right text-primary fs-4"></i>
			</div>
			
			<%-- Algorithm --%>
			<div class="col-md-3">
				<jsp:include page="algorithm.jsp"/>
			</div>
			
			<%-- Connector arrow --%>
			<div class="col-md-1 text-center">
				<i class="fa fa-arrow-right text-primary fs-4"></i>
			</div>
			
			<%-- Output data --%>
			<div class="col-md-3">
				<c:set var="inputOutput" value="Output" scope="request"/>
				<jsp:include page="analyzedData.jsp"/>		
			</div>
		</div>
	</div>
</div>