<%@ include file="/common/taglibs.jsp"%>

<%--This page is never shown alone, but always part of peopleForm.jsp
<title><fmt:message key="author.list.title"/></title>
<content tag="heading"><fmt:message key="author.list.title"/></content>
<body id="submissions"/>
--%>

<c:set var="counter" value="0"/>
<c:set var="listlength" value ="${fn:length(peopleList)}"/> 

<div class="card shadow-lg mb-4">
	<div class="card-header d-flex justify-content-between align-items-center">
		<span class="fw-semibold">${PEOPLE}s</span>
		<tb:helpButton topic="peopleList"/>
	</div>
	<div class="card-body">
		<p class="text-muted mb-3">The table below shows a list of the ${PEOPLE}s for the submission. Use the form above to add a new member.</p>
		<div class="table-responsive">
			<display:table name="${peopleList}" 
						   requestURI=""
						   class="table table-striped table-hover"
						   id="userList"
						   cellspacing="0"
						   cellpadding="0">
						   
				<display:column property="id" title="Id" 
							 style="text-align:left; width:5%"/>			   			 		   
				
				<display:column property="lastName" titleKey="user.lastname"/>
					
				<display:column property="firstName" titleKey="user.firstname"/>
					
				<display:column property="middleName" titleKey="user.middlename"/>	
							
				<display:column property="emailAddressString" titleKey="user.emailaddressstring" />
							
				<c:if test="${publicationState eq 'NotReady'}">
					
					<display:column titleKey="author.order">
						<c:if test ="${counter > 0 && listlength > 1}">
							<button type="button" class="btn btn-sm btn-outline-secondary" onclick="swapRowUp(this.parentNode.parentNode)">
								<i class="fa fa-arrow-up"></i> Up
							</button>
						</c:if>
						<c:if test ="${counter < listlength-1  &&  listlength > 1}">
							<button type="button" class="btn btn-sm btn-outline-secondary" onclick="swapRowDown(this.parentNode.parentNode)">
								<i class="fa fa-arrow-down"></i> Down
							</button>
						</c:if>
					</display:column>
					
					<display:column class="iconColumn" headerClass="iconColumn">
					
						<c:if test ="${PEOPLE eq 'Author'}">
							<c:url value="/user/authorForm.html" var="url">
							<c:param name="method" value="Delete"/>
							<c:param name="id" value="${peopleList[counter].id}"/>
							</c:url>
							<a href="<c:out value="${url}"/>" class="btn btn-sm btn-outline-danger" title="<fmt:message key="author.delete"/>">
								<i class="fa fa-trash"></i>
							</a>
						</c:if>
						<c:if test ="${PEOPLE eq 'Editor'}">
							<c:url value="/user/editorForm.html" var="url">
							<c:param name="method" value="Delete"/>
							<c:param name="id" value="${peopleList[counter].id}"/>
							</c:url>
							<a href="<c:out value="${url}"/>" class="btn btn-sm btn-outline-danger" title="<fmt:message key="author.delete"/>">
								<i class="fa fa-trash"></i>
							</a>
						</c:if>
					
					</display:column>		
					
				</c:if>
				<display:setProperty name="basic.empty.showtable" value="true"/>
				<c:set var="counter" value="${counter+1}"/>
			</display:table>
		</div>
	</div>
</div>
