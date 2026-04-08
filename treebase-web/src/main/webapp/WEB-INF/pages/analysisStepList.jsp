<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="analysis.step.list.title"/></title>
<content tag="heading"><fmt:message key="analysis.step.list.title"/></content>
<body id="submissions"/>

<div class="container py-5">
	<div class="card shadow-lg">
		<div class="card-header bg-primary text-white">
			<i class="fa fa-list-ol me-2"></i>
			<span>Analysis Steps</span>
		</div>
		<div class="card-body">
			<p class="text-muted mb-3">
				The table below shows a list of the analysis steps for <strong>analysis ${analysisMap['id']} - ${analysisMap['name']}</strong>
			</p>
			
			<form method="post">
				<display:table name="${analysisStepList}" 
							   requestURI=""
							   defaultsort="1"
							   class="table table-striped table-hover"
							   id="userList">
					
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
								style="text-align:left;width:20%">
						<span class="text-primary">Input & Output Data</span>
					</display:column>
					
					<display:setProperty name="basic.empty.showtable" value="true"/>
					
				</display:table>
			</form>
		</div>
		<div class="card-footer">
			<a href="<c:url value="/user/analysisStepForm.html"/>" class="btn btn-success">
				<i class="fa fa-plus-circle me-1"></i> <fmt:message key="button.new.analysis.step"/>
			</a>
		</div>
	</div>
</div>
