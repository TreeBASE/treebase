<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="analysis.form.title"/></title>
<content tag="heading"><fmt:message key="analysis.form.title"/></content>
<body id="submissions"/>

<spring:bind path="analysis.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">	
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>


<form method="post">

<fieldset>
<legend>Analysis Information
<a href="#" class="openHelp" onclick="openHelp('analysisInfo')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>
<c:if test="${publicationState eq 'NotReady'}">
Fill in the analysis information for submission <strong>${studyMap['id']} - ${studyMap['name']}</strong>. 
</c:if>
<table border="0" cellpadding="3">

	<tr>
        <th><fmt:message key="analysis.name"/>:</th>
        <td>
            <spring:bind path="analysis.name">
            <input class="textCell" style="width:100%" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th><fmt:message key="analysis.notes"/>:</th>
        <td colspan="3">
            <spring:bind path="analysis.notes">
            <textarea rows="4" class="textCell" style="width:100%" name="<c:out value="${status.expression}"/>">${status.value}</textarea>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>

  	<tr>
    	<th></th>
    	<td>
    		<%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
		<% request.setAttribute("isEditable","yes");%>
	<% } %>
  	  
  	  <c:if test="${publicationState eq 'NotReady'||isEditable eq 'yes'}">
 			  <c:choose>
 				<c:when test="${analysis.id == null}">
 					<input type="submit" name="Submit" value="<fmt:message key="button.submit"/>" />
 				</c:when>
 				<c:otherwise>
 					<input type="submit" name="Update" value="<fmt:message key="button.update"/>" />
 					<input type="submit" name="Delete" value="<fmt:message key="button.delete"/>" />
 				</c:otherwise>
 			  </c:choose>
	          <input type="reset" name="Reset" value="<fmt:message key="button.reset"/>" />
	          <input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>"/>
	        </c:if>
	        
        </td>
    </tr>
</table>
</fieldset>
</form>

<c:if test="${! empty analysis.id}">
	<fieldset>
	<legend>Analysis steps
	<a href="#" class="openHelp" onclick="openHelp('analysisSteps')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
	</legend>
	<a href="/treebase-web/user/analysisStepForm.html?analysis_id=${analysis.id}">
	<img src="<fmt:message key="icons.add"/>" class="iconButton"/></a> Add step
	<c:set var="counter" value="0"/>
	<display:table name="requestScope.analysis.analysisStepsReadOnly"
				   requestURI=""
				   class="list"
				   id="userList"
				   cellspacing="3"
				   cellpadding="3">	
		
		<display:column property="displayName" title="Analysis step name" 
					sortable="true"/>	
		
		<display:column class="iconColumn" headerClass="iconColumn">
			<c:if test="${not empty analysis.analysisStepsReadOnly}">
				<spring:bind path="analysis.analysisStepsReadOnly[${counter}]">	
					<a href="/treebase-web/user/analysisStepForm.html?id=<c:out value="${status.value.id}"/>">
						<img src="<fmt:message key="icons.edit"/>" class="iconButton" />
					</a>
				</spring:bind>	
			</c:if>	
		</display:column>	
		
		
		<display:column class="iconColumn" headerClass="iconColumn">
			<c:if test="${not empty analysis.analysisStepsReadOnly}">
				<spring:bind path="analysis.analysisStepsReadOnly[${counter}]">
					<form action="/treebase-web/user/analysisStepForm.html" method="POST" style="padding:0px;margin:0px">
						<input type="hidden" name="id" value="<c:out value="${status.value.id}"/>" />
						<input type="image" border="0" src="<fmt:message key="icons.delete"/>" name="Delete" value="Delete"/>
					</form>	
				</spring:bind>	
			</c:if>		
		</display:column>	
						
	    <display:setProperty name="export.pdf" value="true" />	
		<display:setProperty name="basic.empty.showtable" value="true"/>
		<c:set var="counter" value="${counter+1}"/>
	</display:table>
	</fieldset>
</c:if>