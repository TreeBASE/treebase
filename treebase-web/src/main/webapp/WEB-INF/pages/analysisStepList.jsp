<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="analysis.step.list.title"/></title>
<content tag="heading"><fmt:message key="analysis.step.list.title"/></content>
<body id="submissions"/>

<p class="sub-class">The table below shows a list of the analysis steps for analysis for <b>analysis ${analysisMap['id']} - ${analysisMap['name']}</b></p>
<form method="post">

<display:table name="${analysisStepList}" 
			   requestURI=""
			   defaultsort="1"
			   class="list"
			   id="userList"
			   cellspacing="3"
			   cellpadding="3">
	
	<display:column property="order" titleKey="analysis.step.order" 
				sortable="true" style="text-align:center; width:5%"
				url="/user/analysisStepForm.html" paramId="id" paramProperty="id"/>
				
	<display:column property="name" titleKey="analysis.step.name" 
				sortable="true" style="text-align:left; width:20%"/>
				
	<display:column property="softwareInfo.name" titleKey="analysis.step.software.name" 
				sortable="true"
				style="text-align:left; width: 20%"/>
				
	<display:column property="algorithmType" titleKey="analysis.step.algorithm.type" 
				sortable="true"
				style="text-align:left; width: 20%"/>
				
	<display:column titleKey="link.view" 
				sortable="true"
				url="/user/analyzedDataList.html" paramId="id" paramProperty="id"
				style="text-align:left;width:20%">Input & Ouput Data
				</display:column>

	<display:footer>
	<tr><td>&nbsp;</td></tr>
	<tr>
		<td colspan="4" align="center">
			<button type="button" style="margin-right: 5px"	
					onclick="location.href='<c:url value="/user/analysisStepForm.html"/>'">
        			<fmt:message key="button.new.analysis.step"/>
    		</button>
		</td>
	</tr>
	</display:footer>
	
	 <display:setProperty name="basic.empty.showtable" value="true"/>
	
</display:table>
</form>
