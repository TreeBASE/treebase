<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="submission.list.title"/></title>
<content tag="heading"><fmt:message key="submission.list.title"/></content>
<body id="admin"/>

<p class="sub-class">The table below shows a list of Treebase submissions</p>

<form method="post">
<c:url var="deletePageURL" value="/user/deleteStudy.html?" />

<c:set var="counter"   value="0"/>
<display:table name="requestScope.asubmissionlist.myList" 
			   requestURI=""
			   defaultsort="1"
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3"
			   export = "true">
	
	<display:column property="id" title="ID" 
				sortable="true"
				url="/user/summary.html" paramId="id" paramProperty="id"/>
				
	<display:column  title="Submitter" 	sortable="true">
		<a href='mailto:${userList.submitter.emailAddressString}?subject=TreeBASE Submission S${userList.id}'>${userList.submitter.username}</a>
	</display:column>
				
	<display:column property="study.name" title="Study Name" sortable="true"/>
				
	<display:column property="study.notes" title="Study Notes" sortable="true"/>		
	
	<display:column property="createDate" title="Created" sortable="true"/>		

	<display:column property="lastModifiedDate" title="Last Modified" sortable="true"/>		

	<display:column title="Change Status">
	
		<spring:bind path="asubmissionlist.myList[${counter}].study.transientDescription">	
			<input type="radio" name="${status.expression}" value="${studyStatusTypes[1]}" <c:if test="${status.value eq studyStatusTypes[1]}">checked</c:if> >${studyStatusTypes[1]}<br/>
			<input type="radio" name="${status.expression}" value="${studyStatusTypes[2]}" <c:if test="${status.value eq studyStatusTypes[2]}">checked</c:if> >${studyStatusTypes[2]}<br/>
			<input type="radio" name="${status.expression}" value="${studyStatusTypes[3]}" <c:if test="${status.value eq studyStatusTypes[3]}">checked</c:if>/>${studyStatusTypes[3]}
		</spring:bind>	
		<c:set var="counter" value="${counter+1}"/>	
		
	</display:column>

	  <display:column titleKey="link.delete">
		<a href="<c:out value="${deletePageURL}"/><c:out value="submissionid="/><c:out value="${userList.id}" />" >Delete Study</a>
	  </display:column>
				
	<display:footer>
		<tr align="center">
    		<td colspan="8">
	        	<input type="submit" class="button" name="Update" value="<fmt:message key="button.update"/>" />
	        	<input type="submit" class="button" name="_cancel" value="<fmt:message key="button.cancel"/>" />
        	</td>
    	</tr>	
	</display:footer>

 
	<display:setProperty name="export.pdf" value="true" />	
	<display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>
</form>