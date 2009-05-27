<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="user.list.title"/></title>
<content tag="heading"><fmt:message key="user.list.title"/></content>
<body id="submissions"/>

<p class="sub-class">The table below shows a list of TreeBASE users based on your search</p>
<form method="post" action="<c:url value='/admin/userList.html'/>">

<c:url var="deletePageURL" value="/admin/adminDeletingUserStepTwo.html?" />

<display:table name="${userList}" 
			   requestURI=""
			   defaultsort="1"
			   class="list"
			   id="itemList"
			   cellspacing="3"
			   cellpadding="3"
			   export = "true">
	
	<display:column property="id" title="User ID" 
				sortable="true" style="text-align:center; width:10%"
				url="/admin/overrideUserProfile.html" paramId="id" paramProperty="id"/>
				
	<display:column property="username" title="Username" 
				sortable="true"
				style="text-align:left; width: 10%"/>
				
	<display:column property="lastName" title="Last Name" 
				sortable="true"
				style="text-align:left; width: 10%"/>
				
	<display:column property="firstName" title="First Name" 
				sortable="true"
				style="text-align:left; width: 10%"/>

	<display:column property="emailAddressString" title="Email" 
				sortable="true"
				style="text-align:left; width: 15%"/>

	<display:column property="roleDescription" title="Role" 
				sortable="true"
				style="text-align:left; width: 10%"/>		
	
	  <display:column titleKey="link.delete" 
				sortable="true" style="text-align:left; width:15%">
				<a href="<c:out value="${deletePageURL}"/><c:out value="userid="/><c:out value="${itemList.username}" />" >Delete User</a>
	  </display:column>
	
		
	<display:footer>
	<tr><td>&nbsp;</td></tr> 	
	</display:footer>
 
	<display:setProperty name="export.pdf" value="true" />	
	<display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>
</form>