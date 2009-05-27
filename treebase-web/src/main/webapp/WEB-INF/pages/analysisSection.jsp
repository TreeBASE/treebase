<%@ include file="/common/taglibs.jsp"%>
<title><fmt:message key="analysis.list.title"/></title>
<content tag="heading"><fmt:message key="analysis.list.title"/></content>
<body id="submissions"/>
<p>
	TreeBASE will only 
	publish matrices and trees that are listed with analysis entries. At a 
	minimum, each submission must have at least one analysis entry 
	containing at least one analysis step.
</p>
<fieldset>
<legend>List of Analyses
<a href="#" class="openHelp" onclick="openHelp('analysisSection')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>
<p>The table below shows a list of the analyses for <strong>submission ${studyMap['id']} - ${studyMap['name']}</strong>. To add an
analysis, click the <img src="<fmt:message key="icons.add"/>" class="iconButton"/> icon below. To delete an analysis, click its 
<img class="iconButton" src="<fmt:message key="icons.delete"/>" title="<fmt:message key="analysis.delete"/>" 
alt="<fmt:message key="analysis.delete"/>"/> icon. To add steps to the analysis, define input and output data and record metadata,
click the analysis's <img class="iconButton" src="<fmt:message key="icons.edit"/>" title="<fmt:message key="analysis.edit"/>" 
alt="<fmt:message key="analysis.edit"/>"/> icon.	
</p>
<a href="<c:url value="/user/analysisForm.html"/>">
<img src="<fmt:message key="icons.add"/>" class="iconButton"/></a> Add analysis
<c:set var="counter" value="1"/>
<display:table name="${studyCommand.analysisCommandList}"
			   requestURI=""
			   defaultsort="1"
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3">
				
	<display:column 				 
				titleKey="analysis.name" 
				sortable="true">				
				<c:if test='${userList.name != ""}'>
					<c:out value="${userList.name}"/>
				</c:if>
				<c:if test='${userList.name == ""}'>
					<c:out value="analysis ${counter}"/>
				</c:if>
				<c:set var="counter" value="${counter+1}"/>
				</display:column>
				
	<display:column 
				property="notes" 
				titleKey="analysis.notes" 
				sortable="true"/>
				
	<display:column
				sortable="false"
				class="iconColumn"
				headerClass="iconColumn">
				<c:if test="${userList.validated}">
				<img 
					class="iconButton" 
					src="<fmt:message key="icons.analyzed"/>" 
					title="<fmt:message key="analysis.validated"/>" 
					alt="<fmt:message key="analysis.validated"/>"/>				
				</c:if>
				<c:if test="${!userList.validated}">
				<img 
					class="iconButton" 
					src="<fmt:message key="icons.notanalyzed"/>" 
					title="<fmt:message key="analysis.notvalidated"/>" 
					alt="<fmt:message key="analysis.notvalidated"/>"/>					
				</c:if>
	</display:column>
				
	<display:column
				sortable="false"
				url="/user/analysisForm.html" 
				paramId="id" 
				paramProperty="id"
				class="iconColumn" 
				headerClass="iconColumn">
				<img 
					class="iconButton" 
					src="<fmt:message key="icons.edit"/>" 
					title="<fmt:message key="analysis.edit"/>" 
					alt="<fmt:message key="analysis.edit"/>"/>	
				</display:column>	
				
	<display:column
				class="iconColumn" 
				headerClass="iconColumn">
				<form 
					method="post" 
					action="analysisForm.html" 
					id="form${userList.id}"
					style="padding:0px;margin:0px">
					<img 
						onclick="$('form${userList.id}').submit()"
						class="iconButton" 
						src="<fmt:message key="icons.delete"/>" 
						title="<fmt:message key="analysis.delete"/>" 
						alt="<fmt:message key="analysis.delete"/>"/>
					<input type="hidden" name="id" value="${userList.id}"/>
					<input type="hidden" name="Delete" value="Delete"/>						
				</form>		
	</display:column>			
	
	 <display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>
</fieldset>