<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="person.list.title"/></title>
<content tag="heading"><fmt:message key="person.list.title"/></content>
<body id="admin"/>

<p class="sub-class">The table below shows a list of TreeBASE persons based on your search</p>
<form method="post" action="<c:url value='/admin/personList.html'/>">

<display:table name="${personList}" 
			   requestURI=""
			   defaultsort="1"
			   class="list"
			   id="itemList"
			   cellspacing="3"
			   cellpadding="3"
			   export = "true">
	
	<display:column property="id" title="Person ID" 
				sortable="true" style="text-align:center; width:10%" paramId="id" paramProperty="id"/>
				
				
	<display:column property="lastName" title="Last Name" 
				sortable="true"
				style="text-align:left; width: 10%"/>
				
	<display:column property="firstName" title="First Name" 
				sortable="true"
				style="text-align:left; width: 10%"/>

	<display:column property="emailAddressString" title="Email" 
				sortable="true"
				style="text-align:left; width: 15%"/>
		
	<display:footer>
	<tr><td>&nbsp;</td></tr>
 	
	</display:footer>
 
	<display:setProperty name="export.pdf" value="true" />	
	<display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>
</form>