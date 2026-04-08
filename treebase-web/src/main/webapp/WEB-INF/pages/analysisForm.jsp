<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="analysis.form.title"/></title>
<content tag="heading"><fmt:message key="analysis.form.title"/></content>
<body id="submissions"/>

<div class="container py-5">
    <spring:bind path="analysis.*">
        <c:if test="${not empty status.errorMessages}">
            <div class="alert alert-danger" role="alert">
                <c:forEach var="error" items="${status.errorMessages}">
                    <i class="fa fa-exclamation-triangle me-2"></i>
                    <c:out value="${error}" escapeXml="false"/><br />
                </c:forEach>
            </div>
        </c:if>
    </spring:bind>

    <form method="post">
        <div class="card shadow-lg mb-4">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0">
                    <i class="fa fa-flask me-2"></i>Analysis Information
                </h5>
                <a href="#" class="openHelp btn btn-outline-secondary btn-sm" onclick="openHelp('analysisInfo')">
                    <i class="fa fa-question-circle me-1"></i>Help
                </a>
            </div>
            <div class="card-body">
                <c:if test="${publicationState eq 'NotReady'}">
                    <div class="alert alert-info d-flex align-items-start mb-4" role="alert">
                        <i class="fa fa-info-circle fa-lg me-3 mt-1"></i>
                        <div>
                            Fill in the analysis information for submission <strong>${studyMap['id']} - ${studyMap['name']}</strong>.
                        </div>
                    </div>
                </c:if>
                
                <div class="mb-3">
                    <label class="form-label fw-semibold"><fmt:message key="analysis.name"/></label>
                    <spring:bind path="analysis.name">
                        <input class="form-control" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <c:if test="${not empty status.errorMessage}">
                            <div class="invalid-feedback d-block"><c:out value="${status.errorMessage}"/></div>
                        </c:if>
                    </spring:bind>
                </div>
                
                <div class="mb-3">
                    <label class="form-label fw-semibold"><fmt:message key="analysis.notes"/></label>
                    <spring:bind path="analysis.notes">
                        <textarea rows="4" class="form-control" name="<c:out value="${status.expression}"/>">${status.value}</textarea>
                        <c:if test="${not empty status.errorMessage}">
                            <div class="invalid-feedback d-block"><c:out value="${status.errorMessage}"/></div>
                        </c:if>
                    </spring:bind>
                </div>
            </div>
            <div class="card-footer">
                <%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
                    <% request.setAttribute("isEditable","yes");%>
                <% } %>
                
                <c:if test="${publicationState eq 'NotReady'||isEditable eq 'yes'}">
                    <c:choose>
                        <c:when test="${analysis.id == null}">
                            <button type="submit" name="Submit" class="btn btn-primary">
                                <i class="fa fa-check me-1"></i><fmt:message key="button.submit"/>
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button type="submit" name="Update" class="btn btn-primary me-2">
                                <i class="fa fa-save me-1"></i><fmt:message key="button.update"/>
                            </button>
                            <button type="submit" name="Delete" class="btn btn-danger me-2">
                                <i class="fa fa-trash me-1"></i><fmt:message key="button.delete"/>
                            </button>
                        </c:otherwise>
                    </c:choose>
                    <button type="reset" name="Reset" class="btn btn-outline-secondary me-2">
                        <i class="fa fa-undo me-1"></i><fmt:message key="button.reset"/>
                    </button>
                    <button type="submit" name="_cancel" class="btn btn-secondary">
                        <i class="fa fa-times me-1"></i><fmt:message key="button.cancel"/>
                    </button>
                </c:if>
            </div>
        </div>
    </form>

    <c:if test="${! empty analysis.id}">
        <div class="card shadow-lg mb-4">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0">
                    <i class="fa fa-list-ol me-2"></i>Analysis Steps
                </h5>
                <div>
                    <a href="/treebase-web/user/analysisStepForm.html?analysis_id=${analysis.id}" class="btn btn-success btn-sm me-2">
                        <i class="fa fa-plus me-1"></i>Add Step
                    </a>
                    <a href="#" class="openHelp btn btn-outline-secondary btn-sm" onclick="openHelp('analysisSteps')">
                        <i class="fa fa-question-circle me-1"></i>Help
                    </a>
                </div>
            </div>
            <div class="card-body">
                <c:set var="counter" value="0"/>
                <display:table name="requestScope.analysis.analysisStepsReadOnly"
                               requestURI=""
                               class="table table-striped table-hover"
                               id="userList">	
                    
                    <display:column property="displayName" title="Analysis Step Name" sortable="true"/>	
                    
                    <display:column title="Actions" class="text-center text-nowrap">
                        <c:if test="${not empty analysis.analysisStepsReadOnly}">
                            <div class="btn-group btn-group-sm" role="group">
                                <spring:bind path="analysis.analysisStepsReadOnly[${counter}]">	
                                    <a href="/treebase-web/user/analysisStepForm.html?id=<c:out value="${status.value.id}"/>" 
                                       class="btn btn-outline-primary" title="Edit">
                                        <i class="fa fa-edit"></i>
                                    </a>
                                </spring:bind>
                                <spring:bind path="analysis.analysisStepsReadOnly[${counter}]">
                                    <form action="/treebase-web/user/analysisStepForm.html" method="POST" style="display:inline;padding:0;margin:0">
                                        <input type="hidden" name="id" value="<c:out value="${status.value.id}"/>" />
                                        <button type="submit" name="Delete" class="btn btn-outline-danger" title="Delete">
                                            <i class="fa fa-trash"></i>
                                        </button>
                                    </form>	
                                </spring:bind>
                            </div>
                        </c:if>		
                    </display:column>	
                                
                    <display:setProperty name="export.pdf" value="true" />	
                    <display:setProperty name="basic.empty.showtable" value="true"/>
                    <c:set var="counter" value="${counter+1}"/>
                </display:table>
            </div>
        </div>
    </c:if>
</div>