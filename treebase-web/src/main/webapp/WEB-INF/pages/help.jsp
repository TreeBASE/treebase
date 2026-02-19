<%@ include file="/common/taglibs.jsp"%>
<%@ page contentType="text/plain" %>
<html>
	<head>
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
		<title><fmt:message key="help.title"/></title>
	</head>
	<body class="p-3">
		<c:if test="${! command.admin }">
			<c:if test="${empty command.helpText }">
			  <h2>Missing help message</h2>
			  <p>Sorry, there is no help available for topic '${command.helpTag }'</p>
			</c:if>		
			<c:out value="${command.helpText}" escapeXml="false"/>
		</c:if>
		<c:if test="${command.admin }">
			<form id="helpAdmin" method="post" action="/treebase-web/help.html">
				<fieldset class="border p-3">
					<legend class="fw-bold"><fmt:message key="help.admin.legend"><fmt:param value="${command.helpTag }"/></fmt:message></legend>
					<input type="hidden" name="helpTag" value="${command.helpTag}"/>
					<textarea class="form-control mb-2" style="height:180px" name="newHelpText">${command.helpText }</textarea>
					<input class="btn btn-primary btn-sm" type="submit" name="action" value="Save"/>
				</fieldset>
			</form>
		</c:if>
	</body>
</html>