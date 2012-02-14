<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="taxon.list.title"/></title>
<%-- content tag="heading"><fmt:message key="search.results.title.taxons"/></content>
<body id="taxons"/ --%>

<c:set var="ncbiTaxonomyURL" value="http://www.ncbi.nlm.nih.gov/Taxonomy/Browser/wwwtax.cgi?mode=Info&id=" />
<c:set var="uBioTaxonomyURL" value="http://www.ubio.org/browser/details.php?namebankID=" />

<display:table name="${resultSet.results}"
			   requestURI=""
			   class="list"
			   id="taxon"
			   cellspacing="3"
			   cellpadding="3"
			   export = "false">

	<display:column title="" sortable="true" class="checkBoxColumn noBreak">
		<c:url var="url" value="study/taxa.html">
			<c:param name="taxonid" value="${taxon.id}" />
			<%-- c:param name="id" value="${taxon.study.id}" / --%>
		</c:url>
		<input type="checkbox" id="s-${taxon.id }" name="selection" value="${taxon.id }" /> 
		Tx${taxon.id }
	</display:column>

				
	<display:column property="name" title="Taxon Name" 
		sortable="true"/>

	<display:column titleKey="taxon.uBioTaxID" class="noBreak">
		<c:if test="${ taxon.UBioNamebankId != null }">
			<c:set var="ubioTaxoURL" value="${uBioTaxonomyURL}${taxon.UBioNamebankId}"/>
			<a href="${ubioTaxoURL}" target="_blank">
			<img class="iconButton" src="<fmt:message key="icons.weblink"/>" />
			${taxon.UBioNamebankId}</a>
		</c:if>
	</display:column>
				
	<display:column titleKey="taxon.ncbiTaxID" class="noBreak">
		<c:if test="${ taxon.ncbiTaxId != null }">
			<c:set var="ncbiTaxoURL" value="${ncbiTaxonomyURL}${taxon.ncbiTaxId}"/>
			<a href="${ncbiTaxoURL}" target="_blank">
			<img class="iconButton" src="<fmt:message key="icons.weblink"/>" />
			${taxon.ncbiTaxId}</a>
		</c:if>
	</display:column>		
	
	<%-- display:column property="nStudies" title="Studies" 
		sortable="true" style="text-align:center; width:10%"/ --%>
		
	<display:footer>
		<tr>
			<!--  this id is important, because we might add additional buttons here -->
    		<td colspan="4" align="center" id="buttonContainer">
				<!--  insert additional controls here -->
        	</td>
    	</tr>
    </display:footer>
    <display:setProperty name="basic.empty.showtable" value="true"/>


</display:table>



