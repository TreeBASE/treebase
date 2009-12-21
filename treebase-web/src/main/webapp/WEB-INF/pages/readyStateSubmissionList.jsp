<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="studies.ready.state"/></title>
<content tag="heading"><fmt:message key="studies.ready.state"/></content>
<body id="admin"/>

<p class="sub-class">The table below shows a list of all Studies in <strong>Ready</strong> State</p>
<form method="post">

<display:table name="${readyStateSubmissionList}" 
			   requestURI=""
			   defaultsort="1"
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3"
			   export = "true">
	
	<display:column property="id" title="ID" 
				sortable="true" style="text-align:left; width:8%"
				url="/user/summary.html" paramId="id" paramProperty="id"/>
				
	<display:column  title="Submitter" 	sortable="true"
				style="text-align:left; width: 10%">
				<a href='mailto:${userList.submitter.emailAddressString}?subject=TreeBASE Submission S${userList.id}'>${userList.submitter.username}</a>
	</display:column>
	
	<display:column property="study.studyStatus.description" title="Status" 
				sortable="true"
				style="text-align:left; width: 10%"/>
				
	<display:column property="study.name" title="Study Name" 
				sortable="true"
				style="text-align:left; width: 15%"/>
				
	<display:column property="study.notes" title="Study Notes" 
				sortable="true"
				style="text-align:left; width: 25%"/>		

	<display:column property="createDate" title="Created" 
				sortable="true"
				style="text-align:left; width: 8%"/>		

	<display:setProperty name="export.pdf" value="true" />	
	<display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>
</form>