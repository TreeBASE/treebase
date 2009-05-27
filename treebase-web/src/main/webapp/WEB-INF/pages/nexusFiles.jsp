<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="nav.nexusfiles"/></title>
<content tag="heading"><fmt:message key="nav.nexusfiles"/></content>
<body id="submissions"/>
<c:url var="nexusdownloadURL" value="/user/downloadANexusFile.html" />
<c:url var="nexusrctdownloadURL" value="/user/downloadANexusRCTFile.html" />
<p>The table below shows a list of all the nexus files for the current study.</p>
<form method="post">
<fieldset>
<legend>
Nexus files
<a href="#" class="openHelp" onclick="openHelp('nexusFiles')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
</legend>
<c:set var="counter"   value="0"/>
<display:table name="${nexusFileList}"
			   requestURI=""
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3">	

	<display:column title="File name" sortable="true">${userList}</display:column>		

	<display:column 
		class="iconColumn" 
		headerClass="iconColumn"
		sortable="false">
		<a href="<c:out value="${nexusrctdownloadURL}"/>?nexusfile=${userList}">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.download.reconstructed"/>" 
				title="<fmt:message key="download.reconstructedfile"/>" 
				alt="<fmt:message key="download.reconstructedfile"/>"/>					
		</a>
	</display:column>
		
	<display:column
		class="iconColumn" 
		headerClass="iconColumn"		
		sortable="false">
		<a href="<c:out value="${nexusdownloadURL}"/>?nexusfile=${userList}">
			<img 
				class="iconButton" 
				src="<fmt:message key="icons.download.original"/>" 
				title="<fmt:message key="download.original"/>" 
				alt="<fmt:message key="download.original"/>"/>					
		</a>
	</display:column>		
	
	<display:setProperty name="export.pdf" value="true" />
	<display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>
</fieldset>
</form>