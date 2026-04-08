<%@ include file="/common/taglibs.jsp"%>
<% pageContext.setAttribute("carriageReturn","\n"); %>

<title><fmt:message key="upload.file.summary.title"/></title>
<content tag="heading"><fmt:message key="upload.file.summary.title"/></content>

<body id="submissions"/>

<div class="container py-5">
    <div class="alert alert-info d-flex align-items-start mb-4" role="alert">
        <i class="fa fa-info-circle fa-lg me-3 mt-1"></i>
        <div>
            <strong>File Upload Summary</strong>
            <p class="mb-0 mt-2">Review the results of your Nexus file upload below.</p>
        </div>
    </div>

    <form method="post">
        <div class="card shadow-lg mb-4">
            <div class="card-header">
                <h5 class="mb-0"><i class="fa fa-check-circle me-2"></i>Upload Results</h5>
            </div>
            <div class="card-body">
                <div class="row mb-3">
                    <div class="col-md-6">
                        <div class="d-flex align-items-center">
                            <i class="fa fa-th fa-2x text-primary me-3"></i>
                            <div>
                                <h6 class="mb-0">Matrices</h6>
                                <c:choose>
                                    <c:when test="${uploadMatrixCount == 1}">
                                        <span class="badge bg-success">1 matrix uploaded</span>
                                    </c:when>
                                    <c:when test="${uploadMatrixCount > 1}">
                                        <span class="badge bg-success">${uploadMatrixCount} matrices uploaded</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">No matrices</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="d-flex align-items-center">
                            <i class="fa fa-sitemap fa-2x text-success me-3"></i>
                            <div>
                                <h6 class="mb-0">Trees</h6>
                                <c:choose>
                                    <c:when test="${uploadTreeCount == 1}">
                                        <span class="badge bg-success">1 tree uploaded</span>
                                    </c:when>
                                    <c:when test="${uploadTreeCount > 1}">
                                        <span class="badge bg-success">${uploadTreeCount} trees uploaded</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">No trees</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
                
                <c:if test="${uploadMatrixCount + uploadTreeCount == 0}">
                    <div class="alert alert-warning d-flex align-items-center" role="alert">
                        <i class="fa fa-exclamation-triangle me-2"></i>
                        <div>No matrices or trees were uploaded from this file.</div>
                    </div>
                </c:if>
                
                <c:if test="${not empty uploadParserLog}">
                    <hr class="my-4">
                    <h6><i class="fa fa-file-text-o me-2"></i>Parser Log</h6>
                    <div class="bg-light p-3 rounded border" style="max-height: 300px; overflow-y: auto; font-family: monospace; font-size: 0.85rem;">
                        <c:out value="${fn:replace(uploadParserLog, carriageReturn,'<br/>')}" escapeXml="false"/>
                    </div>
                </c:if>
            </div>
            <div class="card-footer">
                <c:if test="${empty uploadParserLog}">
                    <p class="text-muted small mb-3">
                        <i class="fa fa-info-circle me-1"></i>
                        If you suspect that one or more blocks in your nexus file failed to parse properly, 
                        click "Show Parser Log" to examine the parser's log file.
                    </p>
                    <button type="submit" name="showLog" class="btn btn-outline-primary me-2">
                        <i class="fa fa-file-text-o me-1"></i><fmt:message key="button.parser.log"/>
                    </button>
                </c:if>
                <button type="submit" name="_cancel" class="btn btn-secondary">
                    <i class="fa fa-arrow-left me-1"></i>Back to Study
                </button>
            </div>
        </div>
    </form>
</div>