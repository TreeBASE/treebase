<%@ include file="/common/taglibs.jsp"%>

<%--This page is never shown alone, but always part of peopleForm.jsp
<title><fmt:message key="author.list.title"/></title>
<content tag="heading"><fmt:message key="author.list.title"/></content>
<body id="submissions"/>
--%>



<c:set var="counter" value="0"/>
<c:set var="listlength" value ="${fn:length(peopleList)}"/> 

<fieldset>
<legend>
${PEOPLE}s
<a href="#" class="openHelp" onclick="openHelp('peopleList')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>
<p>The table below shows a list of the ${PEOPLE}s for the submission. Use the form above to add a new member.</p>
<display:table name="${peopleList}" 
			   requestURI=""
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3">
			   
	<display:column property="id" title="Id" 
				 style="text-align:left; width:5%"/>			   			 		   
	
	<display:column property="lastName" titleKey="user.lastname"/>
		
	<display:column property="firstName" titleKey="user.firstname"/>
		
	<display:column property="middleName" titleKey="user.middlename"/>	
				
	<display:column property="emailAddressString" titleKey="user.emailaddressstring" />
					
	<c:if test="${publicationState eq 'NotReady'}">
		
		<display:column titleKey="author.order">
			<c:if test ="${counter > 0 && listlength > 1}">
				<input type="button" value=" UP " onclick="swapRowUp(this.parentNode.parentNode)">
			</c:if>
			<c:if test ="${counter < listlength-1  &&  listlength > 1}">
				<input type="button" value="DOWN" onclick="swapRowDown(this.parentNode.parentNode)">
			</c:if>
		</display:column>
		
		<display:column class="iconColumn" headerClass="iconColumn">
		
			<c:if test ="${PEOPLE eq 'Author'}">
				<c:url value="/user/authorForm.html" var="url">
				<c:param name="method" value="Delete"/>
				<c:param name="id" value="${peopleList[counter].id}"/>
				</c:url>
				<a href="<c:out value="${url}"/>">
					<img 
						class="iconButton" 
						src="<fmt:message key="icons.delete"/>" 
						title="<fmt:message key="author.delete"/>" 
						alt="<fmt:message key="author.delete"/>"/>					
				</a>
			</c:if>
			<c:if test ="${PEOPLE eq 'Editor'}">
				<c:url value="/user/editorForm.html" var="url">
				<c:param name="method" value="Delete"/>
				<c:param name="id" value="${peopleList[counter].id}"/>
				</c:url>
				<a href="<c:out value="${url}"/>">
					<img 
						class="iconButton" 
						src="<fmt:message key="icons.delete"/>" 
						title="<fmt:message key="author.delete"/>" 
						alt="<fmt:message key="author.delete"/>"/>					
				</a>
			</c:if>
		
		</display:column>		
		
	</c:if>
	<display:setProperty name="basic.empty.showtable" value="true"/>
	<c:set var="counter" value="${counter+1}"/>
</display:table>
</fieldset>
