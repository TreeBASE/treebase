<%@ include file="/common/taglibs.jsp"%>
<head>
<title>Taxon Label Information</title>
<content tag="heading">Taxon Labels Information</content>

<body id="submissions"/>


<c:set var="ncbiTaxonomyURL" value="http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id=" />
<c:set var="uBioTaxonomyURL" value="http://www.ubio.org/browser/details.php?namebankID=" />
<form method="post" >

<fieldset>
<legend>Taxon Labels
<a href="#" class="openHelp" onclick="openHelp('taxonLabels')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>
Taxon labels must be in compliance with TreeBASE guidelines, and wherever possible they should be validated against 
names in <a href="http://www.ubio.org">uBIO</a> and <a href="http://www.ncbi.nlm.nih.gov/Taxonomy/">NCBI</a>.

<display:table name="${txnlabelset}"
			   requestURI=""			 
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3"
			   export="true">
			   
	<!-- this class name 'checkBoxColumn' is important, not just for styling,
	but also to add check/uncheck all behaviour  -->
	<%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
		<% request.setAttribute("isEditable","yes");%>
	<% } %>	  
	
	<c:if test="${editable||isEditable eq 'yes'}">
		<display:column class="checkBoxColumn">
			<input type="checkbox" name="validate" value="${userList.id}" title="Include/exclude from validation"
				<c:if test="${!userList.attemptedLinking}"> checked="checked"</c:if>
			/>
		</display:column>
	</c:if>
	
	<display:column property="taxonLabel" sortable="true" titleKey="taxonlabel.title" />
				
	<display:column sortable="true" property="taxonVariant.fullName" titleKey="taxon.name" />
				
	<display:column titleKey="taxon.ncbiTaxID" sortable="true">
		<c:if test="${userList.taxonVariant!=null}">
			<c:set var="ncbiTaxoURL" value="${ncbiTaxonomyURL}${userList.ncbiTaxID}"/>
			<a href="${ncbiTaxoURL}" target="_blank">${userList.ncbiTaxID}</a>
		</c:if>
	</display:column>
	
	<display:column titleKey="taxon.uBioTaxID" sortable="true">
		<c:if test="${userList.taxonVariant!=null}">
			<c:set var="ubioTaxoURL" value="${uBioTaxonomyURL}${userList.taxonVariant.namebankID}"/>
			<a href="${ubioTaxoURL}" target="_blank">${userList.taxonVariant.namebankID}</a>
		</c:if>
	</display:column>	
	
	<display:column
		class="iconColumn" 
		headerClass="iconColumn">
		<c:if test="${userList.attemptedLinking}">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.analyzed"/>" 
				title="<fmt:message key="taxonlabel.attemptedLinking"/>" 
				alt="<fmt:message key="taxonlabel.attemptedLinking"/>"/>		
		</c:if>
		<c:if test="${!userList.attemptedLinking}">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.notanalyzed"/>" 
				title="<fmt:message key="taxonlabel.attemptedLinking"/>" 
				alt="<fmt:message key="taxonlabel.attemptedLinking"/>"/>		
		</c:if>	
	</display:column>
				
	<c:if test="${editable||isEditable eq 'yes'}">	
		<display:column
			url="/user/editTaxonLabel.html" 
			paramId="taxonlabelid" 
			paramProperty="id" 			
			class="iconColumn" 
			headerClass="iconColumn">
					<img 
						class="iconButton" 
						src="<fmt:message key="icons.edit"/>" 
						title="<fmt:message key="taxonlabel.change.title"/>" 
						alt="<fmt:message key="taxonlabel.change.title"/>"/>					
		</display:column>
	</c:if>


	<c:if test="${editable||isEditable eq 'yes'}">
		<display:footer>
			<tr>

				<!--  this id is important, because we might add additional buttons here -->
    			<td colspan="7" align="center" id="buttonContainer">
	        		<input type="submit" class="button" name="Update" value="Update Multiple Taxon Labels" />	        
	        		<input type="submit" class="button" name="Validate" value="Validate Taxon Labels" />	        
        		</td>
    		</tr>
    	</display:footer>
    </c:if>
	
	<display:setProperty name="export.pdf" value="true" />
	<display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>

 </fieldset>
</form>
