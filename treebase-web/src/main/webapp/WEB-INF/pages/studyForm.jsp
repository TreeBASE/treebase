<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="study.title"/></title>

<c:choose>
	<c:when test="${empty study.submission.id}">
		<content tag="heading">Create New Submission</content>
		<p>Please complete the following submission information </p>
	</c:when>
	<c:otherwise>
		<content tag="heading">Update Submission</content>
		<p>Please update the following submission information </p>
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
    	  <c:if test="${publicationState eq 'NotReady'}">
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
