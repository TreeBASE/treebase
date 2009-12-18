<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="matrix.list.title"/></title>
<content tag="heading"><fmt:message key="matrix.list.title"/></content>
<body id="submissions"/>

<p>The table below shows a list of matrix data you've uploaded for your study.</p>

<form method="post">

<c:set var="counter" value="0"/>

<fieldset>
<legend>Matrices
<a href="#" class="openHelp" onclick="openHelp('matrixList')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>

<display:table name="requestScope.amatrixcollection.myList"
			   requestURI=""
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3">
	
	<display:column  titleKey="matrix.title">
		<spring:bind path="amatrixcollection.myList[${counter}].title">
			<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
			<input type="text" class="textCell" name="${status.expression}" value="<c:out value="${status.value}"/>" />
		</spring:bind>		
	</display:column>		
				
	<display:column  titleKey="matrix.description" sortable="false">
		<spring:bind path="amatrixcollection.myList[${counter}].description">
			<input type="hidden" name="_<c:out value="${status.expression}"/>"/>
			<input type="text" class="textCell" name="${status.expression}" value="<c:out value="${status.value}"/>" />
		</spring:bind>		
	</display:column>
		
	<display:column title="Matrix Kind" sortable="false">	
		<spring:bind path="amatrixcollection.myList[${counter}].kindDescription">	
       		<select name="${status.expression}" class="selectCell">
        		<c:forEach var="status1" items="${matrixKinds}">
        			<option value="${status1}" <c:if test="${status1 eq amatrixcollection.myList[counter].kindDescription}">selected="true"</c:if> >
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
		<spring:bind path="amatrixcollection.myList[${counter}].analyzed">
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
		url="/user/matrixRowList.html" 
		paramId="id" 
		paramProperty="id" 
		class="iconColumn" 
		headerClass="iconColumn">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.list"/>" 
				title="<fmt:message key="matrix.row.list"/>" 
				alt="<fmt:message key="matrix.row.list"/>"/>				
	</display:column>	
		
	<display:column  
		sortable="false"
		url="/user/downloadAMatrix.html" 
		paramId="matrixid" 
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
		paramId="matrixid" 
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
			url="/user/deleteAMatrix.html" 
			paramId="matrixid" 
			paramProperty="id"  
			class="iconColumn" 
			headerClass="iconColumn">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.delete"/>" 
				title="<fmt:message key="matrix.delete"/>" 
				alt="<fmt:message key="matrix.delete"/>"/>				
		</display:column>	
	</c:if>	
	
	<display:footer>
	  <%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
		<% request.setAttribute("isEditable","yes");%>
	<% } %>
  	  
  	  <c:if test="${publicationState eq 'NotReady'||isEditable eq 'yes'}">	
		<tr>
    	  <td colspan="8" align="center">
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

