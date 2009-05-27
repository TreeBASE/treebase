<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="tree.list.title"/></title>
<content tag="heading"><fmt:message key="tree.list.title"/></content>
<body id="submissions"/>

<p class="sub-class">The table below shows a list of all the trees you've uploaded for your study in <strong>Read Only Mode</strong></p>

<c:url var="viewAllTreesAsXML" value="/user/treeParserResult.html" />
<a href="<c:out value="${viewAllTreesAsXML}"/>" target = "_blank">View all the Trees related to your study in XML format</a> <br/>

<form method="post">

<display:table name="${readOnlyTreeList}"
			   requestURI=""
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3"
			   export = "true">
	
	<display:column property="label" titleKey="tree.label" 
				sortable="true" style="text-align:left; width:15%; vertical-align:top"/>
	
	<display:column property="title" titleKey="tree.title" 
				sortable="true" style="text-align:left; width:15%; vertical-align:top"/>
								
	
	<display:column property="newickForDisplay" titleKey="tree.newick.string" 
				sortable="false" style="text-align:left; width:70%"/>
		
	<display:setProperty name="export.pdf" value="true" />
	<display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>
</form>