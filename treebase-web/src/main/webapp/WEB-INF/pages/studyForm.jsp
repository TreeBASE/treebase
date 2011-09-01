<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="study.title"/></title>

<c:choose>
	<c:when test="${empty study.submission.id}">
		<content tag="heading">Create New Submission</content>
		<p>Please provide a brief title for your study. Usually this is the same title as the title of your publication.</p>
		<p>The notes for your study are not for the public, but are there for your own benefit and for communicating with TreeBASE staff once your submission status is ready to be made public. </p>
		<p>If your submission is part of a sponsored research data management plan, please indicate this in the Notes so that TreeBASE staff know to provide added attention and to assist in making your submission fully compliant with the expectations of the sponsor. For more information, see the <a href="/treebase-web/dataMan.html" target="_blank">NSF Data Management Plan instructions</a>. </p>
	</c:when>
	<c:otherwise>
		<content tag="heading">Update Submission</content>
		<p>Please update the following submission information as needed.</p>
		<p>The notes for your study are not for the public, but are there for your own benefit and for communicating with TreeBASE staff once your submission status is ready to be made public. </p>
		<p>If your submission is part of a sponsored research data management plan, please indicate this in the Notes so that TreeBASE staff know to provide added attention and to assist in making your submission fully compliant with the expectations of the sponsor. For more information, see the <a href="/treebase-web/dataMan.html" target="_blank">NSF Data Management Plan instructions</a>. </p>
	</c:otherwise>
</c:choose>

<body id="submissions"/>

<spring:bind path="study.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">	
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<fmt:message key="icons.warn"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>


<form method="post">

<fieldset>
<legend>Submission Information
<a href="#" class="openHelp" onclick="openHelp('studyForm')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>

<table border="0" cellpadding="3">

    <tr>
        <th><fmt:message key="study.name"/>:</th>
        <td>
            <spring:bind path="study.name">
            <input 
            	class="textCell" 
            	style="width:400px" 
            	type="text" 
            	name="<c:out value="${status.expression}"/>" 
            	value="<c:out value="${status.value}"/>"/>           
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <th><fmt:message key="study.notes"/>:</th>
        <td>
            <spring:bind path="study.notes">
            <textarea
            	class="textCell" 
            	rows="4" 
            	style="width:400px" 
            	name="<c:out value="${status.expression}"/>">${status.value}</textarea>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
            </spring:bind>
        </td>
    </tr>
    
     <tr>
    	<th></th>
    	<td>
    	  <c:if test="${publicationState eq 'Ready' || publicationState eq 'Published'}">
    	   <Strong>For now, this study is read only.</Strong>
    	  </c:if>
    	  <%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
		<% request.setAttribute("isEditable","yes");%>
	<% } %>
  	  
  	  <c:if test="${publicationState eq 'NotReady'||isEditable eq 'yes'}">
    		<c:choose>
    			<c:when test="${study.id == null }">
    				<input type="submit" name="Submit" value="<fmt:message key="button.submit"/>"/>
    			</c:when>
    			<c:otherwise>
    				<input type="submit" name="Update" value="<fmt:message key="button.update"/>"/>
    				<!--input type="submit" name="Delete" value="<fmt:message key="button.delete"/>"/-->
    			</c:otherwise>
    		</c:choose>
    		<input type="reset" name="Reset" value="<fmt:message key="button.reset"/>"/>
	        <input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>"/>
	      </c:if>
    	</td>
    </tr>
    
    <tr>
    	<th></th>
    	<td>
    	</td>
    	<td>
    	
    	</td>
</table>
</fieldset>
</form>

<v:javascript formName="study" staticJavascript="false" cdata="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
