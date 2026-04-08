		<div class="container">
			<h2><decorator:getProperty property="page.heading"/></h2>
			<%@ include file="/common/messages.jsp" %>
			<decorator:body/>
		</div>

		<%-- Help Panel Offcanvas --%>
		<jsp:include page="/common/helpPanel.jsp"/>