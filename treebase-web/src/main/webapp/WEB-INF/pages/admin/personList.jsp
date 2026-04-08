<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="person.list.title"/></title>
<content tag="heading"><fmt:message key="person.list.title"/></content>
<body id="admin"/>

<div class="container py-5">
	<div class="alert alert-info d-flex align-items-start mb-4" role="alert">
		<i class="fa fa-info-circle me-3 mt-1"></i>
		<div>
			<p class="mb-0">The table below shows a list of TreeBASE persons based on your search.</p>
		</div>
	</div>

	<form method="post" action="<c:url value='/admin/personList.html'/>">
		<div class="card shadow-lg mb-4">
			<div class="card-header">
				<span class="fw-semibold"><i class="fa fa-users"></i> <fmt:message key="person.list.title"/></span>
			</div>
			<div class="card-body p-0">
				<div class="table-responsive">
					<display:table name="${personList}" 
								   requestURI=""
								   defaultsort="1"
								   class="table table-striped table-hover mb-0"
								   id="itemList"
								   export="true">
						
						<display:column property="id" title="Person ID" 
									sortable="true" paramId="id" paramProperty="id"/>
									
						<display:column property="lastName" title="Last Name" sortable="true"/>
									
						<display:column property="firstName" title="First Name" sortable="true"/>

						<display:column property="emailAddressString" title="Email" sortable="true"/>
							
						<display:setProperty name="export.pdf" value="true"/>	
						<display:setProperty name="basic.empty.showtable" value="true"/>
						
					</display:table>
				</div>
			</div>
		</div>
	</form>
</div>