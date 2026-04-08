

<%@ include file="/common/taglibs.jsp"%>

<title><fmt:message key="treeblock.list.title"/></title>
<content tag="heading"><fmt:message key="treeblock.list.title"/></content>
<body id="submissions"/>

<div class="container py-5">
    <div class="alert alert-info d-flex align-items-start mb-4" role="alert">
        <i class="fa fa-info-circle fa-lg me-3 mt-1"></i>
        <div>
            The table below shows a list of tree blocks for a particular study.
        </div>
    </div>

    <spring:bind path="atreeblocklist.*">
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
                    <i class="fa fa-sitemap me-2"></i>Tree Blocks
                </h5>
                <a href="#" class="openHelp btn btn-outline-secondary btn-sm" onclick="openHelp('treeBlockList')">
                    <i class="fa fa-question-circle me-1"></i>Help
                </a>
            </div>
            <div class="card-body">
                <c:url var="phylowidgetMapURL" value="/user/directMapToPhyloWidget.html" />
                <c:set var="counter" value="0"/>

                <display:table name="requestScope.atreeblocklist.myList"
                               requestURI=""
                               defaultsort="1"
                               class="table table-striped table-hover"
                               id="userList">
                    
                    <display:column titleKey="block.title" sortable="true">
                        <spring:bind path="atreeblocklist.myList[${counter}].title">
                            <input type="hidden" name="_<c:out value="${status.expression}"/>"/>
                            <input type="text" class="form-control form-control-sm" name="${status.expression}" value="<c:out value="${status.value}"/>" />
                        </spring:bind>	
                    </display:column>
                    
                    <display:column property="treeCount" titleKey="tree.count" sortable="true" class="text-center"/>
                                
                    <display:column title="Status" sortable="false" class="text-center">
                        <spring:bind path="atreeblocklist.myList[${counter}].analyzed">
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
                            <a href="<c:url value='/user/treeList.html'><c:param name='treeblockid' value='${userList.id}'/></c:url>" 
                               class="btn btn-outline-primary" title="<fmt:message key="tree.list"/>">
                                <i class="fa fa-list"></i>
                            </a>
                            <a href="${phylowidgetMapURL}?treeblockid=${userList.id}" 
                               class="btn btn-outline-success" title="<fmt:message key="tree.view"/>">
                                <i class="fa fa-eye"></i>
                            </a>
                            <a href="<c:url value='/user/downloadATreeBlock.html'><c:param name='treeblockid' value='${userList.id}'/></c:url>" 
                               class="btn btn-outline-info" title="<fmt:message key="download.reconstructedfile"/>">
                                <i class="fa fa-download"></i>
                            </a>
                            <c:if test="${publicationState eq 'NotReady'}">
                                <a href="<c:url value='/user/deleteATreeBlock.html'><c:param name='treeblockid' value='${userList.id}'/></c:url>" 
                                   class="btn btn-outline-danger" title="<fmt:message key="treeblock.delete"/>">
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
                                <td colspan="7" class="text-center py-3">
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

