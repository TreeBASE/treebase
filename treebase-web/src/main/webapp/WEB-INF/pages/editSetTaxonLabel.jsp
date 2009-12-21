<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="taxonlabel.change.title"/></title>
<content tag="heading">Edit Taxon Labels </content>
<body id="submissions"/>

<spring:bind path="atxnlabellist.myList.*">
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

<form method="post" name="dataform">

<c:set var="counter"   value="0"/>

<%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
		<% request.setAttribute("isEditable","yes");%>
	<% } %>
  	  
 <c:if test="${publicationState eq 'NotReady'||isEditable eq 'yes'}">
<fieldset>
<legend>Taxon label list editor
<a href="#" class="openHelp" onclick="openHelp('editSetTaxonLabel')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>
<display:table name="requestScope.atxnlabellist.myList"
			   requestURI=""
			   class="list"
			   id="taxon"
			   cellspacing="3"
			   cellpadding="3">	
			
	<display:column titleKey="taxonlabel.title">
			<spring:bind path="atxnlabellist.myList[${counter}].taxonLabel">
				<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
				<input type="text" class="textCell" name="${status.expression}" value="<c:out value="${status.value}"/>" />
			</spring:bind>
			<c:set var="counter" value="${counter+1}"/>
	</display:column>
								
	<display:footer>
		<tr>
    		<td align="center">
	        	<input type="submit" class="button" name="Update" value="<fmt:message key="button.update"/>" />
		        <input type="submit" class="button" name="_cancel" value="<fmt:message key="button.cancel"/>" />
    	    </td>
    	</tr>	
	</display:footer>
	
	 <display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>
</fieldset>
</c:if>
</form>


