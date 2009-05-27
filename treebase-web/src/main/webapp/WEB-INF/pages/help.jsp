<%@ include file="/common/taglibs.jsp"%>
<%@ page contentType="text/plain" %>
<html>
	<head>
		<link rel="stylesheet" type="text/css" media="all" href="/treebase-web/styles/styles.css" />
		<title><fmt:message key="help.title"/></title>
	</head>
	<body id="help" style="padding: 20px">
		<c:if test="${! command.admin }">
			<c:if test="${empty command.helpText }">
			  <h2>Missing help message</h2>
			  <p>Sorry, there is no help available for topic '${command.helpTag }'</p>
			</c:if>		
			<c:out value="${command.helpText}" escapeXml="false"/>
		</c:if>
		<c:if test="${command.admin }">
			<form id="helpAdmin" method="post" action="/treebase-web/help.html">
				<fieldset>
					<legend><fmt:message key="help.admin.legend"><fmt:param value="${command.helpTag }"/></fmt:message></legend>
					<input type="hidden" name="helpTag" value="${command.helpTag}"/>
					<textarea style="width:100%;height:180px" name="newHelpText">${command.helpText }</textarea><br/>
					<input type="submit" name="action" value="Save"/>
				</fieldset>
			</form>
		</c:if>
	</body>
</html>