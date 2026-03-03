<%@ include file="/common/taglibs.jsp"%>


<div class="container-fluid">
			<h2 class="text-center"><fmt:message key="submission.list.title"/></h2>
			<form method="post" action="<c:url value='/user/submissionList.html'/>">
				<c:url var="inProgressURL" value="/user/submissionList.html?method=inProgressSubmission" />
				<c:url var="deletePageURL" value="/user/deleteStudy.html?" />
				<c:url var="readyStateURL" value="/user/readyState.html?"/>
				<fieldset class="border rounded p-3 mb-4">
					<legend class="float-none w-auto px-2">Submissions
						<a href="#" class="openHelp ms-2" onclick="openHelp('submissionList')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
					</legend>
					<p class="mb-3">The table below shows a list of your Treebase submissions. Submissions can only be deleted after all trees and matrices are deleted first.</p>
					<div class="table-responsive">
						<display:table 
							name="${submissionList}" 
							requestURI=""
							defaultsort="1"
							class="table table-striped align-middle"
							id="userList"
							cellspacing="0"
							cellpadding="0">
							<display:column title="Submission ID" sortable="true" property="id"/>
							<display:column property="study.name" title="Study Name" sortable="true"/>
							<display:column property="study.notes" title="Study Notes" sortable="true"/>
							<display:column property="study.studyStatus.description" title="Status" sortable="true"/>
							<display:column title="Change Status" sortable="false">
								<c:if test="${userList.study.studyStatus.description eq 'In Progress'}">
									<a href="<c:out value="${readyStateURL}"/><c:out value="submissionid="/><c:out value="${userList.id}" />" class="btn btn-sm btn-outline-primary">Change to Ready State</a>
								</c:if>
							</display:column>
							<display:column sortable="false" url="/user/summary.html" paramId="id" paramProperty="id" class="iconColumn" headerClass="iconColumn">
								<img class="iconButton" src="<fmt:message key="icons.edit"/>" title="<fmt:message key="study.update.title"/>" alt="<fmt:message key="study.update.title"/>"/>
							</display:column>
							<display:column sortable="false" class="iconColumn" headerClass="iconColumn">
								<c:if test="${userList.study.studyStatus.description eq 'In Progress'}">
									<a href="<c:out value="${deletePageURL}"/><c:out value="submissionid="/><c:out value="${userList.id}" />" class="btn btn-sm btn-outline-danger">
										<img class="iconButton" src="<fmt:message key="icons.delete"/>" title="<fmt:message key="study.delete"/>" alt="<fmt:message key="study.delete"/>"/>
									</a>
								</c:if>
							</display:column>
							<display:footer>
								<tr>
									<td colspan="7" class="text-center">
										<b>Show Only </b>
										<select class="form-select d-inline-block w-auto mx-2" name="method" onChange="this.form.submit();">
											<option value="submissionsByUser">--- Please Select ---</option>
											<c:forEach var="type" items="${submissiontypes}">
												<option value="${type.value}" <c:if test="${type.value == method}">selected="selected"</c:if>>
													<c:out value="${type.label}"/>
												</option>
											</c:forEach>
										</select>
										-OR-
										<button type="button" class="btn btn-success btn-sm ms-2" onclick="location.href='<c:url value="/user/studyForm.html"/>?form=new'">
											<fmt:message key="button.new.submission"/>
										</button>
									</td>
								</tr>
							</display:footer>
							<display:setProperty name="basic.empty.showtable" value="true"/>
						</display:table>
					</div>
				</fieldset>
				<fieldset class="border rounded p-3">
					<legend class="float-none w-auto px-2">Dryad Import Result:
						<a href="#" class="openHelp ms-2" onclick="openHelp('dryadImport')"><img class="iconButton" src="<fmt:message key="icons.help"/>" /></a>
					</legend>
					<div class="text-center">
						<c:choose>
							<c:when test="${sessionScope.importStatus =='NOT FOUND'}"><c:out value="Sorry, we cannot find your data"/></c:when>
							<c:when test="${sessionScope.importStatus =='OK'}"><c:out value="Import finished"/></c:when>
							<c:when test="${sessionScope.importStatus =='FAILED'}"><c:out value="Sorry, One or more data file cannot parsed correctly, you may add your data via treebase interfaces"/></c:when>
						</c:choose>
					</div>
				</fieldset>
			</form>
		</div>