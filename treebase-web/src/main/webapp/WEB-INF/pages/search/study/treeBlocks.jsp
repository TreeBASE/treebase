

<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="treeblock.list.title"/></title>
<content tag="heading"><fmt:message key="treeblock.list.title"/></content>
<body id="submissions"/>

<p class="sub-class">The table below shows a list of tree blocks for a particular study.</p>

<spring:bind path="atreeblocklist.*">
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

<c:url var="phylowidgetMapURL" value="/user/directMapToPhyloWidget.html" />
<c:set var="counter"   value="0"/>

<display:table name="requestScope.atreeblocklist.myList"
			   requestURI=""
			   defaultsort="1"
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3"
			   style = "text-align:center"
			   pagesize = "10"
			   export = "true">

    
	<display:column property="id" titleKey="block.id" 
		url="/user/treeList.html" paramId="treeblockid" paramProperty="id"
				sortable="true" style="text-align:left; width:10%"/>
	
	<display:column  titleKey="block.title" 
				sortable="true" style="text-align:left; width:15%">
		<spring:bind path="atreeblocklist.myList[${counter}].title">
				<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
				<input type="text" size="25" name="${status.expression}" value="<c:out value="${status.value}"/>" />
		</spring:bind>	
		<c:set var="counter" value="${counter+1}"/>		
	</display:column>
	
	
	<display:column property="treeCount" titleKey="tree.count" 
				sortable="true" style="text-align:left; width:10%"/>
				
	<display:column titleKey="link.list" 
				sortable="false"
				url="/user/treeList.html" paramId="treeblockid" paramProperty="id"
				style="text-align:left;width:15%"> List Trees
	</display:column>
	
	<display:column titleKey="link.view" 
			sortable="true" 
				style="text-align:left;width:8%">
		<%--		
		<a href="<c:out value="${phylowidgetMapURL}"/><c:out value="?treeblockid="/><c:out value="${userList.id}" />" >View trees</a> 
		--%>
		<c:set var="url" value="${phylowidgetMapURL}?treeblockid=${userList.id}" />
		<a href="javascript:popupWithSizes('${url}',900,800,'1')">View trees</a>
	</display:column>
	
	<%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
		<% request.setAttribute("isEditable","yes");%>
	<% } %>
  	  
  	  <c:if test="${publicationState eq 'NotReady'||isEditable eq 'yes'}">				
		<display:column titleKey="link.delete" 
				sortable="false"
				url="/user/deleteATreeBlock.html" paramId="treeblockid" paramProperty="id"
				style="text-align:left;width:15%"> Delete Tree Block
		</display:column>	
	</c:if>
	
	<display:column titleKey="tree.download" sortable="false"
				url="/user/downloadATreeBlock.html" paramId="treeblockid" paramProperty="id"
				style="text-align:left;width:10%">This block
	</display:column>
	
	<display:footer>
	  <c:if test="${publicationState eq 'NotReady'}">
		<tr>
    		<td colspan=2 align="center">
	        	<input type="submit" class="button" name="Update" value="<fmt:message key="button.update"/>" />
	        	<input type="submit" class="button" name="_cancel" value="<fmt:message key="button.cancel"/>" />
        	</td>
    	</tr>
      </c:if>
	</display:footer>
    <display:setProperty name="export.pdf" value="true" />	
	<display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>
</form>

