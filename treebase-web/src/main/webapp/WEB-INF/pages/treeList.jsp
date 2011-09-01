<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="tree.list.title"/></title>
<content tag="heading"><fmt:message key="tree.list.title"/></content>
<body id="submissions"/>

<c:forEach var="phyloTree" items="${atreecollection.myList}" begin="0" end="0"  >
 	<p>Table below shows a list of trees belonging to a <strong>Block with ID:  <c:out value ="${phyloTree.treeBlock.id}"/> and Title: <c:out value ="${phyloTree.treeBlock.title}"/></strong></p>
</c:forEach>
<form method="post">
<fieldset>
<legend>
Trees
<a href="#" class="openHelp" onclick="openHelp('treeList')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>

<c:url var="treeURL" value="/user/treeParserResult.html" />
<c:url var="phylowidgetMapURL" value="/user/directMapToPhyloWidget.html" />
<c:set var="counter"   value="0"/>
<c:set var="counterKind"   value="0"/>

<display:table name="requestScope.atreecollection.myList"
			   requestURI=""
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3">				
	
	<display:column  titleKey="tree.label" 
				sortable="true">
		<spring:bind path="atreecollection.myList[${counter}].label">
				<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
				<input type="text" class="textCell" name="${status.expression}" value="<c:out value="${status.value}"/>" />
		</spring:bind>			
	</display:column>
	
	<display:column  titleKey="tree.title" 
				sortable="true">
		<spring:bind path="atreecollection.myList[${counter}].title">
				<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
				<input type="text" class="textCell" name="${status.expression}" value="<c:out value="${status.value}"/>" />
		</spring:bind>			
	</display:column>
	
	<display:column title="Tree Type" sortable="true">
		<spring:bind path="atreecollection.myList[${counter}].typeDescription">
			<select name="${status.expression}" class="selectCell">
				<c:forEach var="status1" items="${treeTypes}">
        			<option value="${status1}" <c:if test="${status1 eq atreecollection.myList[counter].typeDescription}">selected="selected"</c:if> >
        				<c:out value="${status1}"/>	
        			</option>				
				</c:forEach>
			</select>
			<span class="fieldError"><c:out value="${status.errorMessage}"/></span>	
		</spring:bind>
	</display:column>	
	
	<display:column title="Tree Kind" sortable="true">	
		<spring:bind path="atreecollection.myList[${counter}].kindDescription">	
       	<select name="${status.expression}" class="selectCell">
        			<c:forEach var="status1" items="${treeKinds}">
        				<option value="${status1}" <c:if test="${status1 eq atreecollection.myList[counter].kindDescription}">selected="selected"</c:if> >
        					<c:out value="${status1}"/>	
        				</option>
        			</c:forEach>
        	</select>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>	
	</display:column>
	
	<display:column title="Tree Quality" sortable="true">	
		<spring:bind path="atreecollection.myList[${counter}].qualityDescription">	
       	<select name="${status.expression}" class="selectCell">
        			<c:forEach var="status1" items="${treeQuality}">
        				<option value="${status1}" <c:if test="${status1 eq atreecollection.myList[counter].qualityDescription}">selected="selected"</c:if> >
        					<c:out value="${status1}"/>	
        				</option>
        			</c:forEach>
        	</select>
            <span class="fieldError"><c:out value="${status.errorMessage}"/></span>
		</spring:bind>
	</display:column>		
	
	<display:column 		
		class="iconColumn" 
		headerClass="iconColumn" 
		sortable="false">
		<spring:bind path="atreecollection.myList[${counter}].analyzed">
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
		class="iconColumn" 
		headerClass="iconColumn">
		<c:set var="url" value="${phylowidgetMapURL}?treeid=${userList.id}" />
		<a href="javascript:popupWithSizes('${url}',1000,900,'1')">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.tree.edit"/>" 
				title="<fmt:message key="tree.edit"/>" 
				alt="<fmt:message key="tree.edit"/>"/>		
		</a>
	</display:column>				
	
	<display:column  
		sortable="false"
		url="/user/downloadATree.html" 
		paramId="treeid" 
		paramProperty="id" 			
		class="iconColumn" 
		headerClass="iconColumn">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.download.reconstructed"/>" 
				title="<fmt:message key="download.reconstructedfile"/>" 
				alt="<fmt:message key="download.reconstructedfile"/>"/>					
	</display:column>	
	
	<display:column 
		sortable="false"
		url="/user/downloadANexusFile.html"
		paramId="treeid" 
		paramProperty="id" 			
		class="iconColumn" 
		headerClass="iconColumn">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.download.original"/>" 
				title="<fmt:message key="download.original"/>" 
				alt="<fmt:message key="download.original"/>"/>				
	</display:column>
	
	<c:if test="${publicationState eq 'NotReady'}">					
		<display:column
			sortable="false"
			url="/user/deleteATree.html" 
			paramId="treeid" 
			paramProperty="id" 			
			class="iconColumn" 
			headerClass="iconColumn">
				<img 
					class="iconButton" 
					src="<fmt:message key="icons.delete"/>" 
					title="<fmt:message key="tree.delete"/>" 
					alt="<fmt:message key="tree.delete"/>"/>				
		</display:column>	
	</c:if>		
	
	<display:footer>
	  <%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
		<% request.setAttribute("isEditable","yes");%>
	<% } %>

	  <c:if test="${publicationState eq 'NotReady'||isEditable eq 'yes'}">
		<tr>
    		<td colspan="10" align="center">
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

