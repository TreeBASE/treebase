<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="matrix.list.title"/></title>
<content tag="heading"><fmt:message key="matrix.list.title"/></content>
<body id="submissions"/>

<div class="container py-5">
    <div class="alert alert-info d-flex align-items-start mb-4" role="alert">
        <i class="fa fa-info-circle fa-lg me-3 mt-1"></i>
        <div>
            The table below shows a list of matrix data you've uploaded for your study.
        </div>
    </div>

    <form method="post">
        <c:set var="counter" value="0"/>
        
        <div class="card shadow-lg mb-4">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0">
                    <i class="fa fa-th me-2"></i>Matrices
                </h5>

	<tb:helpButton topic="matrixList"/>
            </div>
            <div class="card-body">
                <display:table name="requestScope.amatrixcollection.myList"
                               requestURI=""
                               class="table table-striped table-hover"
                               id="userList">
                    
                    <display:column titleKey="matrix.title">
                        <spring:bind path="amatrixcollection.myList[${counter}].title">
                            <input type="hidden" name="_<c:out value="${status.expression}"/>"/>
                            <input type="text" class="form-control form-control-sm" name="${status.expression}" value="<c:out value="${status.value}"/>" />
                        </spring:bind>		
                    </display:column>		
                                
                    <display:column titleKey="matrix.description" sortable="false">
                        <spring:bind path="amatrixcollection.myList[${counter}].description">
                            <input type="hidden" name="_<c:out value="${status.expression}"/>"/>
                            <input type="text" class="form-control form-control-sm" name="${status.expression}" value="<c:out value="${status.value}"/>" />
                        </spring:bind>		
                    </display:column>
                        
                    <display:column title="Matrix Kind" sortable="false">	
                        <spring:bind path="amatrixcollection.myList[${counter}].kindDescription">	
                            <select name="${status.expression}" class="form-select form-select-sm">
                                <c:forEach var="status1" items="${matrixKinds}">
                                    <option value="${status1}" <c:if test="${status1 eq amatrixcollection.myList[counter].kindDescription}">selected="true"</c:if> >
                                        <c:out value="${status1}"/>	
                                    </option>
                                </c:forEach>
                            </select>
                            <c:if test="${not empty status.errorMessage}">
                                <div class="invalid-feedback d-block"><c:out value="${status.errorMessage}"/></div>
                            </c:if>
                        </spring:bind>			
                    </display:column>
                                
                    <display:column title="Status" sortable="false" class="text-center">
                        <spring:bind path="amatrixcollection.myList[${counter}].analyzed">
                            <c:if test="${!status.value}">
                                <span class="badge bg-warning text-dark" title="<fmt:message key="analysis.step.data.notincluded"/>">
                                    <i class="fa fa-exclamation-triangle"></i> Not Analyzed
                                </span>
                            </c:if>
                            <c:if test="${status.value}">
                                <span class="badge bg-success" title="<fmt:message key="analysis.step.data.included"/>">
                                    <i class="fa fa-check-circle"></i> Analyzed
                                </span>
                            </c:if>
                        </spring:bind>	
                        <c:set var="counter" value="${counter+1}"/>	
                    </display:column>				
                                
                    <display:column title="Actions" sortable="false" class="text-center text-nowrap">
                        <div class="btn-group btn-group-sm" role="group">
                            <a href="<c:url value='/user/matrixRowList.html'><c:param name='id' value='${userList.id}'/></c:url>" 
                               class="btn btn-outline-primary" title="<fmt:message key="matrix.row.list"/>">
                                <i class="fa fa-list"></i>
                            </a>
                            <a href="<c:url value='/user/downloadAMatrix.html'><c:param name='matrixid' value='${userList.id}'/></c:url>" 
                               class="btn btn-outline-success" title="<fmt:message key="download.reconstructedfile"/>">
                                <i class="fa fa-download"></i>
                            </a>
                            <a href="<c:url value='/user/downloadANexusFile.html'><c:param name='matrixid' value='${userList.id}'/></c:url>" 
                               class="btn btn-outline-info" title="<fmt:message key="download.original"/>">
                                <i class="fa fa-file-text-o"></i>
                            </a>
                            <c:if test="${publicationState eq 'NotReady'}">
                                <a href="<c:url value='/user/deleteAMatrix.html'><c:param name='matrixid' value='${userList.id}'/></c:url>" 
                                   class="btn btn-outline-danger" title="<fmt:message key="matrix.delete"/>">
                                    <i class="fa fa-trash"></i>
                                </a>
                            </c:if>
                        </div>
                    </display:column>
                    
                    <display:footer>
                        <%if(request.isUserInRole("Admin") || request.isUserInRole("Associate Editor")){%>
                            <% request.setAttribute("isEditable","yes");%>
                        <% } %>
                        
                        <c:if test="${publicationState eq 'NotReady'||isEditable eq 'yes'}">	
                            <tr>
                                <td colspan="8" class="text-center py-3">
                                    <button type="submit" class="btn btn-primary me-2" name="Update">
                                        <i class="fa fa-save me-1"></i><fmt:message key="button.update"/>
                                    </button>
                                    <button type="submit" class="btn btn-secondary" name="_cancel">
                                        <i class="fa fa-times me-1"></i><fmt:message key="button.cancel"/>
                                    </button>
                                </td>
                            </tr>
                        </c:if>
                    </display:footer>	
                                                    
                    <display:setProperty name="export.pdf" value="true" />
                    <display:setProperty name="basic.empty.showtable" value="true"/>
                    
                </display:table>
            </div>
        </div>
    </form>
</div>

