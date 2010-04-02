<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="tree.list.title"/></title>
<%--content tag="heading"><fmt:message key="search.results.title.trees"/></content>
<body id="trees"/ --%>

<script type="text/javascript">
		function openPhylowidget(tree_id)
		{
			 var realURL = "http://www.phylowidget.org/full/?tree='http://"+location.host+"/treebase-web/tree_for_phylowidget/"+"TB2:Tr"+tree_id+"'";
			 window.open(realURL,'myplwidget')
		}
          
</script>

<display:table name="${resultSet.results}"
			   requestURI=""
			   class="list"
			   id="tree"
			   cellspacing="3"
			   cellpadding="3"
			   export = "false">

	<display:column title="" sortable="true" class="checkBoxColumn">
		<c:url var="url" value="/tree_for_phylowidget/">
			<!--c:param name="treeid" value="${tree.id}" /-->
			<!--c:param name="id" value="${tree.study.id}" /-->
		</c:url>
	
		<input type="checkbox" id="s-${tree.id }" name="selection" value="${tree.id }" /> 
		<a href="javascript:void(0)" onClick="openPhylowidget(${tree.id})">Tr${tree.id}</a>
	</display:column>

				
 	<display:column property="label" titleKey="tree.label" sortable="true"/>				
	
	<display:column property="title" titleKey="tree.title" style="width:20%" sortable="true"/>
				
	<display:column property="treeType.description" title="Tree Type" sortable="true"/>
	
	<display:column property="treeKind.description" title="Tree Kind" sortable="true"/>
				
	<display:column property="treeQuality.description" title="Tree Quality" sortable="true"/>
	
	<display:column property="nTax" title="NTAX" sortable="true"/>

	<display:column title="Taxa">
		<c:url value="study/taxa.html" var="taxaURL">
			<c:param name="id">${tree.study.id}</c:param>
			<c:param name="treeid">${tree.id}</c:param>
		</c:url>
		<a href="${taxaURL}">View Taxa</a>
	</display:column>
	
	<display:column 
		sortable="false"
		class="iconColumn" 
		headerClass="iconColumn">
		<c:url value="/search/downloadATree.html" var="newTreeURL">
			<c:param name="id">${tree.study.id}</c:param>
			<c:param name="treeid">${tree.id}</c:param>
		</c:url>
		<a href="${newTreeURL}">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.download.reconstructed"/>" 
				title="<fmt:message key="download.reconstructedfile"/>" 
				alt="<fmt:message key="download.reconstructedfile"/>"/>				
		</a>
	</display:column>
	
	<display:column  
		sortable="false"
		class="iconColumn" 
		headerClass="iconColumn">
		<c:url value="/search/downloadANexusFile.html" var="originalTreeURL">
			<c:param name="id">${tree.study.id}</c:param>
			<c:param name="treeid">${tree.id}</c:param>		
		</c:url>
		<a href="${originalTreeURL}">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.download.original"/>" 
				title="<fmt:message key="download.original"/>" 
				alt="<fmt:message key="download.original"/>"/>		
		</a>
	</display:column>
		
	<display:column 
		sortable="false"
		class="iconColumn" 
		headerClass="iconColumn">
		<c:url var="url" value="/tree_for_phylowidget/">
			<!--c:param name="treeid" value="${tree.id}" /-->
			<!--c:param name="id" value="${tree.study.id}" /-->
		</c:url>
		<a href="javascript:void(0)" onClick="openPhylowidget(${tree.id})">		
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.list"/>" 
				title="<fmt:message key="tree.list.title"/>" 
				alt="<fmt:message key="tree.list.title"/>"/>
		</a>				
	</display:column>			
		
				<%--		
							
	<display:column titleKey="link.view" 
				sortable="false"
				style="text-align:left;width:7%">
						<c:url var="url" value="study/tree.html">
			<c:param name="treeid" value="${tree.id}" />
			<c:param name="id" value="${tree.study.id}" />
		</c:url>
		<a href="${url }">View details</a> 
	</display:column>
					 --%>
					 		
	<display:footer>
		<tr>
			<!--  this id is important, because we might add additional buttons here -->
    		<td colspan="11" align="center" id="buttonContainer">
				<!--  insert additional controls here -->
        	</td>
    	</tr>
    </display:footer>
    <display:setProperty name="basic.empty.showtable" value="true"/>

</display:table>



