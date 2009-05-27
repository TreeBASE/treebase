<%@ include file="/common/taglibs.jsp"%>

<%--This page is never shown alone, but always part of peopleForm.jsp
<title><fmt:message key="author.list.title"/></title>
<content tag="heading"><fmt:message key="author.list.title"/></content>
<body id="submissions"/>
--%>

<fieldset>
<legend>Matched ${PEOPLE}s
<a href="#" class="openHelp" onclick="openHelp('peopleMatchList')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>
<p>The table below shows a list of the possible ${PEOPLE}s based on search. Use the form above to add a new member.</p>

<c:set var="counter" value="0"/>
<c:set var="listlength" value ="${fn:length(peopleMatchList)}"/> 

<display:table name="${peopleMatchList}" 
			   requestURI=""
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3">
			   
	<display:column property="id" title="Id" 
				 style="text-align:left; width:5%"/>		   
	
	<display:column property="lastName" titleKey="user.lastname" 
				 style="text-align:left; width:10%"/>
		
	<display:column property="firstName" titleKey="user.firstname" 
				
				style="text-align:left; width: 10%"/>
		
	<display:column property="middleName" titleKey="user.middlename" 
				
				style="text-align:left; width: 10%"/>	
				
	<display:column property="emailAddressString" titleKey="user.emailaddressstring" 
				
				style="text-align:left; width: 30%"/>
				
	
	<!-- display:column titleKey="link.action" 
				sortable="true" style="text-align:left; width:15%"
				url="/user/authorForm.html?method=Delete" paramProperty="id" paramId="id" -->
				<!-- Delete -->
	<!-- /display:column -->	
	<c:if test="${publicationState eq 'NotReady'}">
		<display:column titleKey="link.action">
		
			<c:if test ="${PEOPLE eq 'Author'}">
				<c:url value="/user/authorSearchForm.html" var="url">
				<c:param name="method" value="Insert"/>
				<c:param name="id" value="${peopleMatchList[counter].id}"/>
				</c:url>
				<a href="<c:out value="${url}"/>">Insert</a>
			</c:if>
			<c:if test ="${PEOPLE eq 'Editor'}">
				<c:url value="/user/editorForm.html" var="url">
				<c:param name="method" value="Insert"/>
				<c:param name="id" value="${peopleList[counter].id}"/>
				</c:url>
				<a href="<c:out value="${url}"/>">Insert</a>
			</c:if>
		
		</display:column>
		
	</c:if>
	<display:setProperty name="basic.empty.showtable" value="true"/>
	<c:set var="counter" value="${counter+1}"/>
</display:table>
</fieldset>

<!--input type="Submit" name ="ss" value = "Update List" onSubmit="return extractIds();" /-->
