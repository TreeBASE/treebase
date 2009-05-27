<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="data.list.title"/></title>
<content tag="heading">List of input and output data for selected analysis step </content>
<body id="submissions"/>

<p class="sub-class">The table below shows a list of the input and output data for <b>analysis step ${analysisStepMap['id']} - ${analysisStepMap['name']}</b></p>
<form method="post">
<c:set var="counter" value="0"/>

<display:table name="${analyzedDataList}"
			   requestURI=""
			   defaultsort="1"
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3">
	
	<display:column property="dataId" titleKey="data.id" 
				sortable="true" style="text-align:left; width:15%"/>
				
	<display:column property="inputOutputType" titleKey="data.input.output.type" 
				sortable="true" style="text-align:left; width:20%"/>
				
	<display:column property="dataType" titleKey="data.matrix.tree.type" 
				sortable="true" style="text-align:left; width: 20%"/>
				
	<display:column property="title" titleKey="data.notes" 
				sortable="true" style="text-align:left; width: 35%"/>

	<display:column titleKey="link.action">
		<c:url value="/user/updateAnalyzedDataList.html" var="url">
		<c:param name="id" value="${analyzedDataList[counter].id}"/>
		</c:url>
	<a href="<c:out value="${url}"/>">Delete</a>
	<c:set var="counter" value="${counter+1}"/>
	</display:column>
				
	<display:footer>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td colspan="4" align="center">
			<button type="button" style="margin-right: 5px"	
					onclick="location.href='<c:url value="/user/analyzedDataForm.html"/>'">
        			<fmt:message key="button.add.data"/>
    		</button>
		</td>
	</tr>
	</display:footer>
	
	 <display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>
</form>
