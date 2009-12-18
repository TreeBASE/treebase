

<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="treeblock.list.title"/></title>
<content tag="heading"><fmt:message key="treeblock.list.title"/></content>
<body id="submissions"/>

<p>The table below shows a list of tree blocks for a particular study.</p>

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
<fieldset>
<legend>
Tree blocks
<a href="#" class="openHelp" onclick="openHelp('treeBlockList')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>

<c:url var="phylowidgetMapURL" value="/user/directMapToPhyloWidget.html" />
<c:set var="counter"   value="0"/>

<display:table name="requestScope.atreeblocklist.myList"
			   requestURI=""
			   defaultsort="1"
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3">
	
	<display:column  titleKey="block.title" 
				sortable="true">
		<spring:bind path="atreeblocklist.myList[${counter}].title">
				<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
				<input type="text" class="textCell" name="${status.expression}" value="<c:out value="${status.value}"/>" />
		</spring:bind>	
	</display:column>
	
	
	<display:column property="treeCount" titleKey="tree.count" 
				sortable="true"/>
					
	<display:column 		
		class="iconColumn" 
		headerClass="iconColumn" 
		sortable="false">
		<spring:bind path="atreeblocklist.myList[${counter}].analyzed">
			<c:if test="${!status.value}">
				<img 
					class="iconButton" 
					src="<fmt:message key="icons.notanalyzed"/>" 
					title="<fmt:message key="analysis.step.data.notincluded"/>" 
					alt="<fmt:message key="analysis.step.data.notincluded"/>"/>			
			</c:if>
			<c:if test="${status.value}">
				<img 
					class="iconButton" 
					src="<fmt:message key="icons.analyzed"/>" 
					title="<fmt:message key="analysis.step.data.included"/>" 
					alt="<fmt:message key="analysis.step.data.included"/>"/>								
			</c:if>
		</spring:bind>	
		<c:set var="counter" value="${counter+1}"/>	
	</display:column>					
													
	<display:column 
		sortable="false"
		url="/user/treeList.html" 
		paramId="treeblockid" 
		paramProperty="id"  
		class="iconColumn" 
		headerClass="iconColumn">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.list"/>" 
				title="<fmt:message key="tree.list"/>" 
				alt="<fmt:message key="tree.list"/>"/>				
	</display:column>
	
	<display:column 
		sortable="false"  
		class="iconColumn" 
		headerClass="iconColumn">
		<c:set var="url" value="${phylowidgetMapURL}?treeblockid=${userList.id}" />
		<a href="javascript:popupWithSizes('${url}',900,800,'1')">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.tree.edit"/>" 
				title="<fmt:message key="tree.edit"/>" 
				alt="<fmt:message key="tree.edit"/>"/>			
		</a>
	</display:column>	
	
	<display:column 
		sortable="false"
		url="/user/downloadATreeBlock.html" 
		paramId="treeblockid" 
		paramProperty="id"  
		class="iconColumn" 
		headerClass="iconColumn">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.download.reconstructed"/>" 
				title="<fmt:message key="download.reconstructedfile"/>" 
				alt="<fmt:message key="download.reconstructedfile"/>"/>					
	</display:column>
	
	<c:if test="${publicationState eq 'NotReady'}">					
		<display:column
			sortable="false"
			url="/user/deleteATreeBlock.html" 
			paramId="treeblockid" 
			paramProperty="id"  
			class="iconColumn" 
			headerClass="iconColumn">
					<img 
						class="iconButton" 
						src="<fmt:message key="icons.delete"/>" 
						title="<fmt:message key="treeblock.delete"/>" 
						alt="<fmt:message key="treeblock.delete"/>"/>				
		</display:column>	
	</c:if>	
	
	<display:footer>
	  <%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
		<% request.setAttribute("isEditable","yes");%>
	<% } %>
  	  
  	  <c:if test="${publicationState eq 'NotReady'||isEditable eq 'yes'}">
		<tr>
    		<td colspan="7" align="center">
	        	<input type="submit" class="button" name="Update" value="<fmt:message key="button.update"/>" />
	        	<input type="submit" class="button" name="_cancel" value="<fmt:message key="button.cancel"/>" />
        	</td>
    	</tr>
      </c:if>
	</display:footer>
    <display:setProperty name="export.pdf" value="true" />	
	<display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>
</fieldset>
</form>

