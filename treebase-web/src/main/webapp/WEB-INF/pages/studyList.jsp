<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="study.list.title"/></title>
<content tag="heading"><fmt:message key="study.list.title"/></content>
<body id="search"/>

<p class="sub-class">The table below shows a list of studies found </p>
<form method="post">
<display:table name="${studyList}" 
			   requestURI=""
			   defaultsort="1"
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3">
	
	<display:column property="id" title="Study ID" 
				sortable="true" style="text-align:left: width 10%"/>
				
	<display:column property="citation.title" title="Citation Title" 
				sortable="true" style="text-align:left; width 50%"/>
				
	<display:column property="notes" title="Notes" 
				sortable="true" style="text-align:left: width 20%"/>
				
	<display:column titleKey="link.view" 
				url="/searchStudy.html" paramId="id" paramProperty="id"
				style="text-align:left; width: 10%">
				Details
				</display:column>
				
	 <display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>
</form>
