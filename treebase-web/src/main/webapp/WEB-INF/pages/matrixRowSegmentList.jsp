<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="matrix.row.segment.title"/></title>
<content tag="heading"><fmt:message key="matrix.row.segment.title"/></content>
<body id="submissions"/>

<p>The table below shows a list of matrix row segments for the matrix row selected</p>

<c:url var="urlMatrixRow" value="/user/matrixRowList.html">
     <c:param name="id" value="${MATRIX_ID}"/>
</c:url>


<form method="post">
<fieldset>
<legend>Matrix Row Segments
<a href="#" class="openHelp" onclick="openHelp('matrixRowSegmentList')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>
<display:table name="${matrixRowSegmentList}"
			   requestURI=""
			   defaultsort="1"
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3">
	
	<display:column property="title" titleKey="matrix.row.segment.title" 
				sortable="true"/>
				
	<display:column property="startIndex" titleKey="matrix.row.segment.startindex" 
				sortable="true"/>	
		
	<display:column property="endIndex" titleKey="matrix.row.segment.endindex" 
				sortable="true"/>			
	
	<c:if test="${publicationState eq 'NotReady'}">			
		<display:column 
			sortable="false"
			url="/user/matrixRowSegmentForm.html"
			paramId="id" 
			paramProperty="id" 		
			class="iconColumn" 
			headerClass="iconColumn">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.edit"/>" 
				title="<fmt:message key="matrix.row.segment.edit"/>" 
				alt="<fmt:message key="matrix.row.segment.edit"/>"/>				
		</display:column>
	</c:if>				

	<display:footer>
	  <c:if test="${publicationState eq 'NotReady'}">	
		<tr>
			<td colspan="4" align="center">
				<button type="button"	
					onclick="location.href='<c:url value="/user/matrixRowSegmentForm.html"/>'">
        			<fmt:message key="button.add.row.segment"/>
    			</button>	
				<button type="button"
					onclick="location.href='<c:out value="${urlMatrixRow}"/>'">
        			<fmt:message key="button.cancel"/>
    			</button>    				
			</td>
		</tr>
	  </c:if>
	</display:footer>
	
	<display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>
</fieldset>
</form>
