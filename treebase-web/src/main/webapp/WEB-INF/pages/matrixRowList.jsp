<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("carriageReturn","\n"); %>

<title><fmt:message key="matrix.row.list.title"/></title>
<content tag="heading"><fmt:message key="matrix.row.list.title"/></content>
<body id="submissions"/>
<c:url var="uploadRowSegmentDataURL" value="/user/uploadRowSegmentData.html" />
<c:url var="viewAllRowSegmentDataURL" value="/user/viewAllRowSegmentData.html" />
<c:url var="exportRowSegmentTeplateURL" value="/user/exportRowSegmentTemplate.html" />

<p align="center">
	<a href="${exportRowSegmentTeplateURL}"><b style="font-size:125%">Export Row Segment Template</b></a>
</p>
<p align="center">
	<a href="${uploadRowSegmentDataURL}"><b style="font-size:125%">Upload Row Segment Data</b></a>
</p>
<p align="center">
	<a href="${viewAllRowSegmentDataURL}"><b style="font-size:125%">View Row Segment Data</b></a>
</p>

<p>The table below shows a list of rows for the selected matrix.</p>
<form method="post">
<fieldset>
<c:forEach var="matrixrowcommand" items="${matrixRowList}"  begin="0" end="0">
	<c:set var="formatInfoString" value="${matrixrowcommand.matrixRow.matrix.formatInfo}"/>
  	<legend>${matrixrowcommand.matrixRow.matrix.title}
  	<a href="#" class="openHelp" onclick="openHelp('matrixRowList')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>  	
  	</legend>
  	<%-- This is not a good way of getting the Matrix Title --Madhu- It is my bad attemp. --%>
  	<%-- I should work on it in the next round. --%>
  	
  	<c:out value="${matrixrowcommand.matrixRow.matrix.dimensionsInfo}"></c:out>
  	<br/>
  	
  	<c:out value="${fn:replace(formatInfoString, carriageReturn,'<br/>')}" escapeXml="false" />
  	<br/>
  	
	<c:forEach var = "charweightset" items = "${matrixrowcommand.matrixRow.matrix.weightSets}"> 
			WTSET: <c:out value="${charweightset.title}"/> = 
		<c:forEach var = "charweight" items = "${charweightset.charWeights}">
			<c:out value="${charweight.weightAndColumnAsString}"/>, 
		</c:forEach>
  		 <br/>
  	</c:forEach>
  
	<c:forEach var = "charset" items = "${matrixrowcommand.matrixRow.matrix.charSets}"> 
		<c:out value="${charset.nexusString}"/> 
		<br/>
	</c:forEach>
  
	<c:forEach var = "charpartition" items = "${matrixrowcommand.matrixRow.matrix.charPartitions}"> 
		<c:out value="${charpartition.nexusString}"/> 
		<br/>
	</c:forEach>
  
</c:forEach>

<!-- Matrix Row id Column now shows the ids that essential represent the index of the List. -->
<!-- It does not show the MatrixRowid in the database anymore.  -->

<display:table name="${matrixRowList}"
			   requestURI=""		   
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3">
				
	<display:column 
		property="matrixRow.taxonLabel.taxonLabel" 
		titleKey="matrix.row.taxon.label" 
		sortable="true"/>
				
	<display:column 
		sortable="false"
		url="/user/matrixRowSegmentList.html" 
		paramId="id" 
		paramProperty="matrixRow.id" 			
		class="iconColumn" 
		headerClass="iconColumn">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.list"/>" 
				title="<fmt:message key="matrix.row.segment.list"/>" 
				alt="<fmt:message key="matrix.row.segment.list"/>"/>				
	</display:column>
								
	<display:setProperty name="export.pdf" value="true" />
	<display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>
</fieldset>
</form>
