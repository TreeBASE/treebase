<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="citation.page.title"/></title>
<content tag="heading"><fmt:message key="citation.page.title"/></content>
<body id="submissions"/>

<spring:bind path="citation.*">
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

<form method="post" id="citationForm">

<input type="hidden" name="id" value="${citation.id}"/>
<input type="hidden" name="citation.citationType" value="${status.value}"/>
<p>Please complete the following citation publication information for <strong>submission ${studyMap['id']} - ${studyMap['name']}</strong></p>

<fieldset>
<legend>Citation Information
<a href="#" class="openHelp" onclick="openHelp('citationForm')"><img class="iconButton" src="<fmt:message key="icons.help"/>" alt="Help" /></a>
</legend>
<table border="0" cellpadding="2" cellspacing="2">
	<tr>
        <th align="right"><fmt:message key="citation.type"/>:</th>        
        <td>
        	<spring:bind path="citation.citationType">
        	<select name="${status.expression}" onchange="formSubmit(form)">
        		<option value="">--- Please Select ---</option>
        		<c:forEach var="type" items="${citationtypes}">
        			<option value="${type}" <c:if test="${type == citation.citationType}">selected="selected"</c:if> >
        				<c:out value="${type}"/>
        			</option>
        		</c:forEach>        		
        	</select>
        	</spring:bind>
        </td>        
        <th align="right"><fmt:message key="citation.publishyear"/>:</th>
        <td>
        	<spring:bind path="citation.publishYear">
	        	<input class="textCell" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" 
	        	        onchange="checkYear(this.form)"/>
	            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
        	</spring:bind>
        </td>        
        <th><fmt:message key="citation.publicationstatus"/>:</th>
        <td>
        	<spring:bind path="citation.citationStatusDescription">
	        	<select name="${status.expression}">
	        		<c:forEach var="status" items="${statuses}">
	        			<option value="${status}" <c:if test="${status == citation.citationStatusDescription}">selected="selected"</c:if> >
	        				<c:out value="${status}"/>	
	        			</option>
	        		</c:forEach>
	        	</select>
	            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
        	</spring:bind>
        </td>
    </tr>    
    <c:choose>
    	<c:when test="${citation.citationType == 'Book' }">
    		<jsp:include page="citationForm-book.jsp"/>
    	</c:when>
    	<c:when test="${citation.citationType == 'Book Section' }">
    		<jsp:include page="citationForm-booksection.jsp"/>
    	</c:when>
    	<c:when test="${citation.citationType == 'Article' }">
    		<jsp:include page="citationForm-article.jsp"/>
    	</c:when>
    </c:choose>    
  	<tr>
  	
  	<%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
		<% request.setAttribute("isEditable","yes");%>
	<% } %>
  	  
  	  <c:if test="${publicationState eq 'NotReady'|| publicationState eq 'Published' || isEditable eq 'yes'}">
    	<th></th>
    	<td colspan="5" align="center">    	  
    		<c:choose>
    			<c:when test="${empty citation.id}">
    				<input type="submit" name="Submit" value="<fmt:message key="button.submit"/>" />
    			</c:when>
    			<c:otherwise>
    				<input type="submit" name="Update" value="<fmt:message key="button.update"/>" />
    			</c:otherwise>
    		</c:choose>	        	        
	        <input type="reset" name="Reset" value="<fmt:message key="button.reset"/>" />
	        <input type="submit" name="_cancel" value="<fmt:message key="button.cancel"/>"/>
        </td>
      </c:if> 
    </tr>
</table>
</fieldset>

</form>

<script type="text/javascript">
//<![CDATA[
function formSubmit(form) {
	form.submit();
}
function checkYear(form) {

	var year = form.publishYear.value;
	var currentDate = new Date();
	var minYear = 1900;
	var maxYear = currentDate.getFullYear();
	var currentMonth = currentDate.getMonth();
	var currentYear = currentDate.getFullYear();
	var currentDay = currentDate.getDate();
	
	if (year.length != 4 || year ==0 || 
	    year < minYear || year > (maxYear+1)) {
	    if (currentMonth >= 9 || currentMonth == 1) {
	    	currentYear = currentYear + 1;
	    }
	    alert("Please enter a valid year");
	    form.publishYear.value=currentYear;
	}
}
//]]>
</script>

<v:javascript formName="citation" staticJavascript="false" cdata="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>
